package hangman;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        File dictionaryFile = null;
        int wordLength = 0;
        int numGuesses = 0;
        char userInput = 'a';

        try {
            dictionaryFile = new File(args[0]);
        } catch (Exception e) {
            System.err.println("Invalid dictionary file");
        }

        try {
            wordLength = Integer.parseInt(args[1]);
            numGuesses = Integer.parseInt(args[2]);
            if (wordLength < 2 || numGuesses < 1) {
                throw new Exception("wordLength must be greater than 1 and numGuesses must be greater than 0");
            }
        } catch (Exception e) {
            System.err.println("Invalid Arguments");
            System.err.println(e.getMessage());
        }

        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        evilHangmanGame.startGame(dictionaryFile, wordLength);

        while (numGuesses > 0) {
            if (numGuesses > 1) {
                System.out.println("You have " + numGuesses + " guesses left");
            }
            else {
                System.out.println("You have " + numGuesses + " guess left");
            }
            System.out.println("Used letters: " + evilHangmanGame.lettersGuessedSoFar());
            System.out.println("Word: " + evilHangmanGame.getPartiallyConstructedWord());
            System.out.println("Enter guess: ");
            try {
                userInput = keyboard.next(".").charAt(0);
            } catch (Exception e) {
                System.err.println("Invalid input! Must be a character");
            }
            try {
                evilHangmanGame.makeGuess(userInput);
            } catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                System.err.println("You already guessed the letter " + e.getGuess());
            }
            if (evilHangmanGame.getNumGuessesFound() > 1) {
                System.out.println("Yes, there are " + evilHangmanGame.getNumGuessesFound() + " " + userInput + "'s ");
            }
            else if (evilHangmanGame.getNumGuessesFound() == 1) {
                System.out.println("Yes, there is " + evilHangmanGame.getNumGuessesFound() + " " + userInput);
            }
            else {
                System.out.println("Sorry, there are no " + userInput + "'s");
            }
            numGuesses--;
        }

    }

}
