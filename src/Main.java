

public class Main {
    public static void main(String[] args) {
        File dictionaryFile = new inputFile();
        int wordLength = 0;
        int numGuesses = 0;
        char userInput;

        try {
            dictionaryFile = args[0];
            wordLength = args[1];
            numGuesses = args[2];
        } catch (Exception e) {
            System.err.println("Invalid arguments:\n Usage: java [program] dictionary wordLength guesses");
        }

        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        evilHangmanGame.startGame(dictionaryFile, wordLength);

        while (numGuesses > 0) {
            System.out.println(numGuesses + " guess(es) remaining");
            System.out.println(evilHangmanGame.lettersGuessedSoFar());
            while !((userInput >= 'a' && userInput <= 'z') || (userInput >= 'A' || userInput <= 'Z')) {
                ;
            }
        }

    }

}
