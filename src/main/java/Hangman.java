import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hangman {

    public int remainingTrials;
    public int score = 0;
    Set<String> usedWordSet = new HashSet<>();
    List<String> wordsList = new ArrayList<>();
    public static final int MAX_TRIALS = 10;
    MockScoreDB mockScoreDB;

    public Hangman() {}
    public Hangman(MockScoreDB mockScoreDB) {
        this.mockScoreDB = mockScoreDB;
    }

    /**countAlphabet takes a word and an alphabet
     * and returns the number of times the alphabet
     * appears in the word
     * @param word
     * @param alphabet
     * @return
     */
    public int countAlphabet(String word, char alphabet) {
        int result = 0;

        for(char c: word.toCharArray()){
            if (c == alphabet) result++;
        }
        return result;
    }

    public String fetchWord(int requestedLength) {
        String result = null;
        remainingTrials = MAX_TRIALS;

        for (String word : wordsList) {
            if (word.length() != requestedLength) continue;
            else if (usedWordSet.add(word)) {
                result = word;
                break;
            }
        }
        return result;
    }

    /**
     * Reads each line that has a word from a file
     * and adds each word to the wordsList
     */
    public void loadWords() {
        String word = null;
        try (BufferedReader br = new BufferedReader(new FileReader("WordSource.txt"))) {
            while ((word = br.readLine()) != null){
                wordsList.add(word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fetchClue(String word) {
        //transform all characters into dashes
        String result = "";
        for(char c : word.toCharArray()) result += "-";
        return result;
    }

    /** fetchClue takes a word, its clue with some dashes,
     * and a character guessed by the player.
     * It returns a new clue that may or may not be different
     * from the clue depending on wheter
     * the word contains the guessed character or not
     * @param word
     * @param clue
     * @param guess
     * @return
     */
    public String fetchClue(String word, String clue, char guess) {
        remainingTrials--;

        if (guess >= 'A' && guess <= 'Z') guess += 32;

        if (guess < 'a' || guess > 'z') throw new IllegalArgumentException("Invalid character");

        String result = "";
        for(int i = 0; i < word.length(); i++) {
            if (guess == word.charAt(i) && guess != clue.charAt(i)) {
                result += guess;
                score += (double)MAX_TRIALS / word.length();
            }
            else result += clue.charAt(i);
        }

        //System.out.println("tentativas restantes: " + MAX_TRIALS);
        return result;
    }

    public boolean saveWordScore(String word, double score) {
        return mockScoreDB.writeScore(word, score);
    }
}
