package ba.ramiz.synonyms.repository;

import ba.ramiz.synonyms.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
    Word findByWord(String word);
}