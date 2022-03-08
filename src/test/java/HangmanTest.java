import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HangmanTest {

    @Test
    void test_alphabetCountInAWord() {
        String word = "pizza";
        char alphabet = 'a';

        Hangman hangman = new Hangman();

        int count = hangman.countAlphabet(word, alphabet);

        assertEquals(1, count);
    }

    @Test
    void test_lengthOfFetchedWordRandom() {
        Random random = new Random();
        int requestLength = random.nextInt(6) + 5;
        Hangman hangman = new Hangman();
        hangman.loadWords();
        String word = hangman.fetchWord(requestLength);

        assertTrue(requestLength == word.length());
    }

    @Test
    void test_uniquenessOfFetchedWord() {
        Random random = new Random();
        int requestedLength = 0;
        Set<String> usedWordsSet = new HashSet<>();
        int round = 0;
        String word = null;
        Hangman hangman = new Hangman();
        hangman.loadWords();

        while (round < 100) {
             requestedLength = random.nextInt(6) + 5;
             word = hangman.fetchWord(requestedLength);
             round++;
             assertTrue(usedWordsSet.add(word));
        }
    }

    @Test
    void test_fetchClueBeforeAnyGuess() {
        Hangman hangman = new Hangman();

        String clue = hangman.fetchClue("pizza");

        assertEquals("-----", clue);
    }

    @Test
    void test_fetchClueAfterCorrectGuess() {
        Hangman hangman = new Hangman();

        String clue = hangman.fetchClue("pizza");

        String newClue = hangman.fetchClue("pizza", clue, 'a');

        assertEquals("----a", newClue);
    }

    @Test
    void test_fetchClueAfterIncorrectGuess() {
        Hangman hangman = new Hangman();

        String clue = hangman.fetchClue("pizza");

        String newClue = hangman.fetchClue("pizza", clue, 'w');

        assertEquals("-----", newClue);
    }
}