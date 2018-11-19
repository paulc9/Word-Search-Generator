import java.util.*;
import java.io.*;

public class WordSearch{

	public static void main(String[] args){
		int min = 0;
		Scanner in = new Scanner(System.in);
		ArrayList<String> wordList = new ArrayList<String>();

		// Word list input: no 2-letter words allowed.
		System.out.println("Type in words for the word search (minimum 3 letters)." +
						   "\nPress enter after each one." +
						   "\nEnter 0 once you are finished.");

		String word = in.nextLine();

		while (!word.equals("0")){
			if (word.length() < 3)
				System.out.println("Invalid word: needs to have 3+ letters");
			else{
				if (min < word.length())
					min = word.length();
				wordList.add(word);
			}

			word = in.nextLine();
		}

		// Size Input: must be at least the same # of chars as the largest word
		// to fit the word.
		System.out.println("\nType in the size of the wordsearch." +
						   "\nSmallest is the number of characters for the largest word." +
						   "\nWordsearch will be this long on both sides.");

		boolean valid = false;
		int size = 0;

		while (!valid){
			try{
				size = in.nextInt();
				if (size >= min)
					valid = true;
				else
					System.out.println("Size must be at least the number of characters for the largest word.");
			} catch (InputMismatchException e){
				System.out.println("Invalid input, please type in a number.");
			}
		}

		// Make wordsearch
		char[][] ws = new char[size][size];

		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				ws[i][j] = '?';
			}
		}

		for (String w: wordList)
			ws = setWord(ws, w, size);

		// Fill remaining spaces with random letters
		Random ran = new Random();
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				if (ws[i][j] == '?'){
					ws[i][j] = (char)(ran.nextInt(26) + 'a');
				}
			}
		}

		// Display wordsearch
		System.out.println("\nWordsearch:\n");
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				System.out.print(ws[i][j] + "  ");
			}
			System.out.println("\n");
		}
	}

	// Set word in the wordsearch.
	// 		First, it will randomly determine x and y for the starting position of the word.
	// 		Next, it will randomly determine in which of the 8 directions the word will be filled
	// 		from the start position. A number from 0 to 7 will be randomly selected, with 
	//		0 = North and going clockwise until 7 = North-west.
	//		If the word doesn't fit using these properties, a new start and direction are chosen.
	public static char[][] setWord(char[][] ws, String word, int wsSize){
		int c = 0, r = 0, dir, cChange = 0, rChange = 0;
		int wordLen = word.length() - 1;	// Minus 1 to account for current space
		Random ran = new Random();
		boolean valid = false;

		while (!valid){
			cChange = 0;
			rChange = 0;
			c = ran.nextInt(wsSize);
			r = ran.nextInt(wsSize);
			dir = ran.nextInt(8);

			switch (dir)
			{
				// NW
				case 7:
					cChange = -wordLen;
				// N
				case 0:
					rChange = -wordLen;
					break;
				// NE
				case 1:
					cChange = wordLen;
					rChange = -wordLen;
					break;
				// SE
				case 3:
					rChange = wordLen;
				// E
				case 2:
					cChange = wordLen;
					break;
				// SW
				case 5:
					cChange = -wordLen;
				// S
				case 4:
					rChange = wordLen;
					break;
				// E
				case 6:
					cChange = -wordLen;
					break;
				// Error catch
				default:
					System.out.println("Error with direction! Will retry...");
					cChange = wordLen + wsSize;
			}

			// Check if the word's letters will be out of bounds
			if (outOfBounds(c, cChange, wsSize) || outOfBounds(r, rChange, wsSize))
				continue;

			// Checks to make sure all characters added to the puzzle will be new characters
			// or characters shared by other words already placed.
			boolean retry = false;
			for (int i = 0; i < word.length(); i++){
				int dc = 0, dr = 0;
				if (i != 0){
					dc = i * (int)Math.signum((double)cChange);
					dr = i * (int)Math.signum((double)rChange);
				}

				char check = ws[r + dr][c + dc];
				if (check != '?' && check != word.charAt(i)){
					retry = true;
					break;
				}
			}
			if (retry)
				continue;

			valid = true;
		}

		// Adds word to puzzle, and returns the updated puzzle
		char[][] wsReturn = ws;

		for (int i = 0; i < word.length(); i++){
			int dc = 0, dr = 0;
			if (i != 0){
				dc = i * (int)Math.signum((double)cChange);
				dr = i * (int)Math.signum((double)rChange);
			}

			wsReturn[r + dr][c + dc] = word.charAt(i);
		}

		return wsReturn;
	}

	public static boolean outOfBounds(int start, int change, int boundSize){
		int answer = start + change;
		if (answer < 0 || answer >= boundSize)
			return true;
		return false;
	}
}