package ba.ramiz.synonyms.domain;

import lombok.Data;

import java.util.List;

@Data
public class WordSynonymRequest {
    private String word1;
    private List<String> synonym;

    public WordSynonymRequest(String word1, List<String> synonym) {
        this.word1 = word1;
        this.synonym = synonym;
    }
}
