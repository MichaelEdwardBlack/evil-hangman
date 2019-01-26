package hangman;

import java.io.File;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> words = new HashSet<>();
    private Set<Character> lettersGuessed = new TreeSet<>();
    private String wordGroup;
    private int wordLength;
    private int guessesFound = 0;

    public boolean playerHasWon() {
        for (int i = 0; i < wordLength; i++) {
            if (!Character.isAlphabetic(wordGroup.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void printEndGameMessage() {
        if (playerHasWon()) {
            System.out.println("You Win!");
            System.out.println("Correct word: " + wordGroup);
        }
        else {
            System.out.println("You lose!");
            System.out.println("The word was: " + words.toArray()[0]);
        }
    }


    public String lettersGuessedSoFar() {
        return lettersGuessed.toString();
    }

    public String getPartiallyConstructedWord() {
        return this.wordGroup;
    }

    public int getNumGuessesFound() {
        return this.guessesFound;
    }

    private void setNumGuessesFound(String wordGroupAfter) {
        for (int i = 0; i < this.wordGroup.length(); i++) {
            if (this.wordGroup.charAt(i) != wordGroupAfter.charAt(i)) {
                guessesFound++;
            }
        }
    }

    public void startGame(File dictionary, int wordLength) {
        Scanner scanner = null;
        StringBuilder buildGroupKey = new StringBuilder();
        this.wordLength = wordLength;

        try {
            scanner = new Scanner(dictionary);
        } catch (Exception e) {
            System.err.println("Something went wrong reading in the dictionary file");
        }

        for (int i = 0; i < wordLength; i++) {
            buildGroupKey.append('-');
        }
        this.wordGroup = buildGroupKey.toString();

        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() == wordLength) {
                this.words.add(word.toLowerCase());
            }
        }

        scanner.close();
    }

    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        String wordGroupAfter = "";
        this.guessesFound = 0;
        if (Character.isUpperCase(guess)) {
            guess += 32; //make the char lowercase
        }
        if (lettersGuessed.add(guess)) {
            Map<String, Set<String>> wordGroups = partitionList(guess);
            wordGroups = getLargestGroups(wordGroups);

            if (wordGroups.size() > 1) {
                if (wordGroups.containsKey(this.wordGroup)) {
                    this.words = wordGroups.get(this.wordGroup);
                    return wordGroups.get(this.wordGroup);
                }
                else {
                    wordGroups = getFewestGuessedLettersGroups(wordGroups);
                    if (wordGroups.size() > 1) {
                        wordGroups = getRightMostLettersGroup(wordGroups);
                    }
                }
            }

            // Only one group left in the map
            for (String group : wordGroups.keySet()) {
                wordGroupAfter = group;
                this.words = wordGroups.get(group);
            }
            setNumGuessesFound(wordGroupAfter);
            this.wordGroup = wordGroupAfter;
            return words;
        }
        else throw new GuessAlreadyMadeException(guess);
    }

    private Map<String, Set<String>> partitionList(char guess) {
        StringBuilder group = new StringBuilder();
        Map<String, Set<String>> wordGroups = new HashMap<>();

        for (String word : this.words) {
            group.delete(0, group.length()); // clear the group
            group.append(this.wordGroup); // start with last known letters guessed
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    group.replace(i,i + 1, Character.toString(guess));
                }
            }
            wordGroups.putIfAbsent(group.toString(), new HashSet<>());
            wordGroups.get(group.toString()).add(word);
        }

        return wordGroups;
    }

    private Map<String, Set<String>> getLargestGroups(Map<String, Set<String>> wordGroups) {
        int maxSize = 0;
        // this loop gets the max size
        for (String group : wordGroups.keySet()) {
            if (wordGroups.get(group).size() >= maxSize) {
                maxSize = wordGroups.get(group).size();
            }
        }

        // this loop removes groups that are less than the max size
        for (String group : new ArrayList<>(wordGroups.keySet())) {
            if (wordGroups.get(group).size() < maxSize) {
                wordGroups.remove(group);
            }
        }

        return wordGroups;
    }

    private Map<String, Set<String>> getFewestGuessedLettersGroups(Map<String, Set<String>> wordGroups) {
        int minNumLettersGuessed = this.wordLength;
        int count;

        for (String group : wordGroups.keySet()) {
            count = 0;
            for (int i = 0; i < group.length(); i++) {
                if (Character.isAlphabetic(group.charAt(i))) {
                    count++;
                }
            }
            minNumLettersGuessed = (count < minNumLettersGuessed ? count : minNumLettersGuessed);
        }

        for (String group : new ArrayList<>(wordGroups.keySet())) {
            count = 0;
            for (int i = 0; i < group.length(); i++) {
                if (Character.isAlphabetic(group.charAt(i))) {
                    count++;
                }
            }
            if (count < minNumLettersGuessed) {
                wordGroups.remove(group);
            }
        }

        return wordGroups;
    }

    private Map<String, Set<String>> getRightMostLettersGroup(Map<String, Set<String>> wordGroups) {
        int rightMostIndex = this.wordLength - 1;
        Map<String, Set<String>> tempMap = new HashMap<>();

        while (wordGroups.size() > 1) {
            for (String group : wordGroups.keySet()) {
                if (Character.isAlphabetic(group.charAt(rightMostIndex))) {
                    tempMap.put(group, wordGroups.get(group));
                }
            }
            if (tempMap.size() > 0) {
                wordGroups = new HashMap<>(tempMap);
            }
            tempMap.clear();
            rightMostIndex--;
        }

        return wordGroups;
    }
}
