package ba.ramiz.synonyms.domain;

import javax.persistence.*;

@Entity
@Table(name = "synonym")
public class Synonym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int word1;
    private int word2;

    public Synonym() {
    }

    public Synonym(int word1, int word2) {
        this.word1 = word1;
        this.word2 = word2;
    }

    public Synonym(int id, int word1, int word2) {
        this.id = id;
        this.word1 = word1;
        this.word2 = word2;
    }

    public int getWord1() {
        return word1;
    }

    public void setWord1(int word1) {
        this.word1 = word1;
    }

    public int getWord2() {
        return word2;
    }

    public void setWord2(int word2) {
        this.word2 = word2;
    }
}
