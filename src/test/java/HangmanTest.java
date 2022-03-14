import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class HangmanTest {

    static Random random;
    static Hangman hangman;
    int requestedLength;

    @BeforeAll
    public static void setupClass() {
        random = new Random();
        hangman = new Hangman();
        hangman.loadWords();
    }

    @BeforeEach
    public void setupTest() {
        requestedLength = random.nextInt(5) + 6;
        hangman.score = 0;
    }

    @Test
    void test1_alphabetCountInAWord() {
        String word = "pizza";
        char alphabet = 'a';

        int count = hangman.countAlphabet(word, alphabet);
        assertEquals(1, count);
    }

    @Test
    void test2_lengthOfFetchedWordRandom() {
        String word = hangman.fetchWord(requestedLength);
        assertTrue(requestedLength == word.length());
    }

    @Test
    void test3_uniquenessOfFetchedWord() {
        Set<String> usedWordsSet = new HashSet<>();
        int round = 0;
        String word = null;

        while (round < 100) {
             requestedLength = random.nextInt(6) + 5;
             word = hangman.fetchWord(requestedLength);
             assertTrue(usedWordsSet.add(word));
             round++;
        }
    }

    @Test
    void test4_fetchClueBeforeAnyGuess() {
        String clue = hangman.fetchClue("pizza");
        assertEquals("-----", clue);
    }

    @Test
    void test5_fetchClueAfterCorrectGuess() {
        String clue = hangman.fetchClue("pizza");
        String newClue = hangman.fetchClue("pizza", clue, 'a');

        assertEquals("----a", newClue);
    }

    @Test
    void test6_fetchClueAfterIncorrectGuess() {
        String clue = hangman.fetchClue("pizza");
        String newClue = hangman.fetchClue("pizza", clue, 'w');

        assertEquals("-----", newClue);
    }

    @Test
    void test7_whenInvalidGuessThenFetchClueThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> hangman.fetchClue("pizza", "-----", '1')
                );
    }

    @Test
    void test8_whenInvalidGuessThenFetchClueThrowsExceptionWithMessage() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> hangman.fetchClue("pizza", "-----", '1')
        );
        assertEquals("Invalid character", e.getMessage());
    }

    @Test
    void test9_remainingTrialsBeforeAnyGuess() {
        hangman.fetchWord(requestedLength);
        assertEquals(Hangman.MAX_TRIALS, hangman.remainingTrials);
    }

    @Test
    void test10_remainingTrialsAfterOneGuess() {
        hangman.fetchWord(requestedLength);
        hangman.fetchClue("pizza", "-----", 'a');
        assertEquals(Hangman.MAX_TRIALS - 1, hangman.remainingTrials);
    }

    @Test
    void test11_scoreBeforeAnyGuess() {
        hangman.fetchWord(requestedLength);
        assertEquals(0, hangman.score);
    }

    @Test
    void test12_scoreAfterCorrectGuess() {
        String word = "pizza";
        String clue = "-----";
        char guess = 'a';

        hangman.fetchClue(word, clue, guess);

        assertEquals((double)Hangman.MAX_TRIALS / word.length(), hangman.score);
    }

    @Test
    void test13_scoreAfterIncorrectGuess() {
        String word = "pizza";
        String clue = "-----";
        char guess = 'x';

        hangman.fetchClue(word, clue, guess);

        assertEquals(0 , hangman.score);
    }

    @Test
    void test14_savaScoreUsingMockDB() {
        MockScoreDB mockScoreDB = mock(MockScoreDB.class);
        Hangman hangman = new Hangman(mockScoreDB);
        when(mockScoreDB.writeScore("apple",10)).thenReturn(true);

        assertTrue(hangman.saveWordScore("apple", 10));

    }
}