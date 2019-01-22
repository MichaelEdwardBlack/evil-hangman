package hangman;

import java.io.File;
import java.util.Set;

public class EvilHangmanGame implements IEvilHangmanGame {
    Set<String> words = new HashSet<String>();
    Set<char> lettersGuessed = new HashSet<char>();

    @SuppressWarnings("serial")
    public static class GuessAlreadyMadeException extends Exception {
    }

    public void startGame(File dictionary, int wordLength) {
        File dictionaryFile = new File(dictionary);
        Scanner scanner = new Scanner(dictionaryFile);

        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() == wordLength) {
                this.words.add(word.toLowerCase());
            }
        }
    }

    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException;

    public String lettersGuessedSoFar() {
        return lettersGuessed.toString();
    }

}
