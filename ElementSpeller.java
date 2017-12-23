import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ElementSpeller {

	// chemical symbol, element name
	private HashMap<String, String> dictionary;

	public ElementSpeller() {
		System.out.println("Welcome to the Element Speller!! Use standard periodic table? Y/N");
		Scanner sc = new Scanner(System.in);
		dictionary = new HashMap<String, String>();
		boolean valid = false;
		while (!valid) {
			String answer = sc.nextLine();
			if (answer.toUpperCase().equals("Y")) {
				makeDictionary("periodic_table.txt");
				System.out.println("\nMaking standard table...");
				valid = true;
			} else if (answer.toUpperCase().equals("N")) {
				System.out.println("\nMaking table with fictional compounds...");
				makeDictionary("fictional_elements.txt");
				valid = true;
			} else
				System.out.println("\nInvalid input. Please try again.");
		}
	}

	private void makeDictionary(String fileName) {
		String line = null;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			while ((line = bufferedReader.readLine()) != null) {
				String[] element = line.split("	");
				dictionary.put(element[0], element[1]);
			}
			bufferedReader.close();
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	public void spellWord() {
		String wordToSpell = "";
		System.out.println("What word do you want to spell? Any non-letter character will be removed.");
		Scanner sc = new Scanner(System.in);
		String answer = sc.nextLine();
		wordToSpell = answer.replaceAll("[^a-zA-Z]", "");
		System.out.println("\nFinding chemical symbol representation for \"" + wordToSpell + "\"...");
		findChemicals(wordToSpell);
	}

	public void findChemicals(String word) {
		ArrayList<String> symbols = findHelper(word, new ArrayList<String>());
		if (symbols != null) {
			System.out.println("\nWord: " + symbols);
			StringBuilder sb = new StringBuilder();
			for (String symbol : symbols) {
				sb.append(dictionary.get(symbol));
				sb.append(", ");
			}
			if (sb.length() > 2)
				sb.delete(sb.length() - 2, sb.length() - 1);
			System.out.println("Elements: " + sb.toString());
		}
		else
			System.out.println("Doesn't look like there exists a chemical combination for that word (yet).");
		Scanner sc = new Scanner(System.in);
		boolean valid = false;
		while (!valid) {
			System.out.println("\nTry another word? Y/N");
			String answer = sc.nextLine();
			if (answer.toUpperCase().equals("Y")) {
				valid = true;
				System.out.println();
				spellWord();
			} else if (answer.toUpperCase().equals("N"))
				valid = true;
		}
		sc.close();
		System.out.println("\nThanks for playing!");
	}

	private ArrayList<String> findHelper(String word, ArrayList<String> symbolsSoFar) {
		if (word.length() == 0)
			return symbolsSoFar;
		for (String symbol : dictionary.keySet()) {
			if (symbol.length() <= word.length()) {
				if (word.substring(0, symbol.length()).toLowerCase().equals(symbol.toLowerCase())) {
					symbolsSoFar.add(symbol);
					if (findHelper(word.substring(symbol.length()), symbolsSoFar) != null)
						return symbolsSoFar;
					symbolsSoFar.remove(symbol);
				}
			}
		}
		return null;
	}
}
