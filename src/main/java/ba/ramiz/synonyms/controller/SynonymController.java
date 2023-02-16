package ba.ramiz.synonyms.controller;

import ba.ramiz.synonyms.domain.WordSynonymRequest;
import ba.ramiz.synonyms.service.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/synonym")
public class SynonymController {

    @Autowired
    private SynonymService synonymService;

    public SynonymController(SynonymService synonymService) {
        this.synonymService = synonymService;
    }

    //    @PreAuthorize("hasAuthority('SCOPE_writer')")
    @PostMapping("/add")
    public ResponseEntity<String> addWordWithSynonyms(@RequestBody WordSynonymRequest request) {
        try {
            if (request.getWord1() == null || request.getWord1().isBlank() || request.getSynonym() == null || request.getSynonym().isEmpty()) {
                return new ResponseEntity<>("Request is missing some data", HttpStatus.BAD_REQUEST);
            }
            synonymService.addWordAndSynonyms(request.getWord1(), request.getSynonym());
            return new ResponseEntity<>("Successfully saved data", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving words: " + request.getWord1() + ", " + request.getSynonym() + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    @PreAuthorize("hasAuthority('SCOPE_writer', 'SCOPE_reader')")
    @GetMapping("/get/{word}")
    public ResponseEntity<List<String>> getSynonyms(@PathVariable String word) {
        List<String> result = synonymService.findWordAndSynonyms(word);
        if (result == null) {
            return ResponseEntity.notFound().header("message", "There are no results with word: " + word).build();
        }
        return ResponseEntity.ok(result);
    }

    //    @PreAuthorize("hasAuthority('SCOPE_writer')")
    @DeleteMapping("/delete/{word}")
    public ResponseEntity<String> deleteWord(@PathVariable String word) {
        try {
            synonymService.deleteWordAndSynonyms(word);
            return new ResponseEntity<>("Successfully deleted data for word: " + word, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting data for word: " + word + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PreAuthorize("hasAuthority('SCOPE_writer')")
    @PutMapping("/edit/{word}")
    public ResponseEntity<String> editWord(@PathVariable String word, @RequestParam String newWord) {
        try {
            if (word == null || newWord == null) {
                return new ResponseEntity<>("Request is missing some data", HttpStatus.BAD_REQUEST);
            }
            synonymService.editWord(word, newWord);
            return new ResponseEntity<>("Successfully edited data", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error editing word: " + word, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    @PreAuthorize("hasAuthority('SCOPE_writer')")
    @PostMapping("/assign/{word1}/{word2}")
    public ResponseEntity<String> assign(@PathVariable String word1, @PathVariable String word2) {
        try {
            // Both words need to be provided if we want to assign them to each other
            if (word1 == null || word2 == null) {
                return new ResponseEntity<>("Request is missing some data", HttpStatus.BAD_REQUEST);
            }
            synonymService.assignWords(word1, word2);
            return new ResponseEntity<>("Successfully assigned data", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error assigning words: " + word1 + " and " + word2 + ". " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    @PreAuthorize("hasAuthority('SCOPE_writer')")
    @PostMapping("/deassign/{word1}/{word2}")
    public ResponseEntity<String> deassign(@PathVariable String word1, @PathVariable String word2) {
        try {
            // Both words need to be provided if we want to deassign them from each other
            if (word1 == null || word2 == null) {
                return new ResponseEntity<>("Request is missing some data", HttpStatus.BAD_REQUEST);
            }
            synonymService.deassignWords(word1, word2);
            return new ResponseEntity<>("Successfully deassigned data", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deassigning words: " + word1 + " and " + word2 + ". " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
