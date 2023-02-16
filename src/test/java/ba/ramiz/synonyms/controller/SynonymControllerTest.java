package ba.ramiz.synonyms.controller;

import ba.ramiz.synonyms.domain.WordSynonymRequest;
import ba.ramiz.synonyms.service.SynonymService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SynonymControllerTest {

    @Mock
    private SynonymService synonymService;

    @InjectMocks
    private SynonymController synonymController;

    @Test
    void addWordWithSynonyms_validRequest_returnsCreated() {
        // arrange
        // Create a list of synonyms
        List<String> synonyms = new ArrayList<>();
        synonyms.add("joyful");
        synonyms.add("smiling");

// Create a new instance of the WordSynonymRequest class
        WordSynonymRequest request = new WordSynonymRequest("word", synonyms);

        // act
        ResponseEntity<String> response = synonymController.addWordWithSynonyms(request);

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void addWordWithSynonyms_missingData_returnsBadRequest() {
        // arrange
        List<String> synonyms = new ArrayList<>();
        synonyms.add("joyful");
        synonyms.add("smiling");
        WordSynonymRequest request = new WordSynonymRequest("", synonyms);

        // act
        ResponseEntity<String> response = synonymController.addWordWithSynonyms(request);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void fetchSynonyms_existingWord_returnsOk() {
        // arrange
        String word = "happy";
        List<String> synonyms = Arrays.asList("joyful", "ecstatic");
        when(synonymService.findWordAndSynonyms(word)).thenReturn(synonyms);

        // act
        ResponseEntity<List<String>> response = synonymController.getSynonyms(word);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(synonyms, response.getBody());
    }

    @Test
    void fetchSynonyms_nonExistingWord_returnsNotFound() {
        // arrange
        String word = "random";
        when(synonymService.findWordAndSynonyms(word)).thenReturn(null);

        // act
        ResponseEntity<List<String>> response = synonymController.getSynonyms(word);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        assertEquals("There are no results with word: " + word, response.getHeaders().get("message").get(0));
    }

    @Test
    void deleteWord_existingWord_returnsOk() {
        // arrange
        String word = "happy";

        // act
        ResponseEntity<String> response = synonymController.deleteWord(word);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted data for word: " + word, response.getBody());
    }

    @Test
    void deleteWord_nonExistingWord_returnsInternalServerError() throws Exception {
        // arrange
        String word = "random";
        doThrow(new RuntimeException("Word not found")).when(synonymService).deleteWordAndSynonyms(word);

        // act
        ResponseEntity<String> response = synonymController.deleteWord(word);

        // assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting data for word: " + word + ": Word not found", response.getBody());
    }
}
