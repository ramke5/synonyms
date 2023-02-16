package ba.ramiz.synonyms.repository;

import ba.ramiz.synonyms.domain.Synonym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynonymRepository extends JpaRepository<Synonym, Integer> {

    @Query("SELECT s FROM Synonym s WHERE s.word1 =:id")
    List<Synonym> findByWord1Id(int id);

    @Query("SELECT s FROM Synonym s WHERE s.word2 =:id")
    List<Synonym> findByWord2Id(int id);

    @Query("SELECT s FROM Synonym s WHERE s.word1 =:id1 and s.word2 =:id2")
    List<Synonym> findByWord1AndWord2Ids(int id1, int id2);
}