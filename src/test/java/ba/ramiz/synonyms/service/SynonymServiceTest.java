package ba.ramiz.synonyms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ba.ramiz.synonyms.domain.Synonym;
import ba.ramiz.synonyms.domain.Word;
import ba.ramiz.synonyms.repository.SynonymRepository;
import ba.ramiz.synonyms.repository.WordRepository;

public class SynonymServiceTest {

    private SynonymService synonymService;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private SynonymRepository synonymRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        synonymService = new SynonymService(wordRepository, synonymRepository);
    }

    @Test
    void testEditWord() throws Exception {
        Word word = new Word("word");
        when(wordRepository.findByWord("word")).thenReturn(word);

        synonymService.editWord("word", "new_word");

        verify(wordRepository, times(1)).findByWord("word");
        verify(wordRepository, times(1)).save(any());
    }

    @Test
    void testEditWordNonexistentWord() {
        when(wordRepository.findByWord("word")).thenReturn(null);

        assertThrows(Exception.class, () -> {
            synonymService.editWord("word", "new_word");
        });

        verify(wordRepository, times(1)).findByWord("word");
        verify(wordRepository, times(0)).save(new Word("new_word"));
    }

    @Test
    void testAssignWordsNonexistentWords() {
        when(wordRepository.findByWord("word1")).thenReturn(null);
        when(wordRepository.findByWord("word2")).thenReturn(null);

        assertThrows(Exception.class, () -> {
            synonymService.assignWords("word1", "word2");
        });

        verify(synonymRepository, times(0)).save(new Synonym());
    }

    @Test
    void testDeassignWordsNonexistentWords() {
        when(wordRepository.findByWord("word1")).thenReturn(null);
        when(wordRepository.findByWord("word2")).thenReturn(null);

        assertThrows(Exception.class, () -> {
            synonymService.deassignWords("word1", "word2");
        });

        verify(synonymRepository, times(0)).deleteAll(new ArrayList<>());
    }
}
