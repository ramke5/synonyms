package ba.ramiz.synonyms.service;

import ba.ramiz.synonyms.domain.Synonym;
import ba.ramiz.synonyms.domain.Word;
import ba.ramiz.synonyms.repository.SynonymRepository;
import ba.ramiz.synonyms.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SynonymService {
    private final WordRepository wordRepository;
    private final SynonymRepository synonymRepository;

    public SynonymService(WordRepository wordRepository, SynonymRepository synonymRepository) {
        this.wordRepository = wordRepository;
        this.synonymRepository = synonymRepository;
    }

    public void addWordAndSynonyms(String word1, List<String> synonymRequest) {
        //For every synonym in the list
        for (String synReq : synonymRequest) {
            //Add check if words are already synonyms inserted
            Word word1FromDb = wordRepository.findByWord(word1);
            Word word2FromDb = wordRepository.findByWord(synReq);
            //If word1 does not exist insert
            if (word1FromDb == null) {
                Word newWord1 = wordRepository.save(new Word(word1));
                word1FromDb = newWord1;
            }
            //If word2 does not exist - insert
            if (word2FromDb == null) {
                Word newWord2 = wordRepository.save(new Word(synReq));
                //newWord2 will now have set Id property
                word2FromDb = newWord2;
            }
            //Get all synonmys for word1 and word2
            var synonymsFromWord1 = getSynonymsIds(word1);
            var synonymsFromWord2 = getSynonymsIds(synReq);

            //Insert word1 and word2 as synonyms
            Synonym synonym = new Synonym();
            synonym.setWord1(word1FromDb.getId());
            synonym.setWord2(word2FromDb.getId());
            synonymRepository.save(synonym);

            //Go through first list and insert synonyms (items from first list - word2)
            if (synonymsFromWord1 != null && synonymsFromWord1.size() > 0) {
                for (Integer item : synonymsFromWord1) {
                    Synonym as = new Synonym();
                    as.setWord1(item);
                    as.setWord2(word2FromDb.getId());
                    synonymRepository.save(as);
                }
            }

            //Go through second list and insert synonyms (items from second list - word1)
            if (synonymsFromWord2 != null && synonymsFromWord2.size() > 0) {
                for (Integer item : synonymsFromWord2) {
                    Synonym as = new Synonym();
                    as.setWord1(item);
                    as.setWord2(word1FromDb.getId());
                    synonymRepository.save(as);
                }
            }
        }
    }

    private List<Integer> getSynonymsIds(String word) {
        Word wordFromDb = wordRepository.findByWord(word);
        if (wordFromDb == null) {
            return null;
        }
        //If word exists get all synonyms from DB
        List<Synonym> synonymList1 = synonymRepository.findByWord1Id(wordFromDb.getId());
        List<Synonym> synonymList2 = synonymRepository.findByWord2Id(wordFromDb.getId());

        synonymList1.addAll(synonymList2);
        List<Integer> result = new ArrayList<>();

        for (Synonym syn : synonymList1) {
            int id;
            if (syn.getWord1() == wordFromDb.getId()) {
                id = syn.getWord2();
            } else {
                id = syn.getWord1();
            }
            result.add(id);
        }
        return result;
    }

    public List<String> findWordAndSynonyms(String word) {
        Word wordFromDb = wordRepository.findByWord(word);
        if (wordFromDb == null) {
            return null;
        }

        //If word exists get all synonyms from DB
        List<Synonym> synonymList1 = synonymRepository.findByWord1Id(wordFromDb.getId());
        List<Synonym> synonymList2 = synonymRepository.findByWord2Id(wordFromDb.getId());

        synonymList1.addAll(synonymList2);

        List<String> result = new ArrayList<>();

        for (Synonym item : synonymList1) {
            int idToSearch;
            if (item.getWord1() == wordFromDb.getId()) {
                idToSearch = item.getWord2();
            } else {
                idToSearch = item.getWord1();
            }
            Optional<Word> wordFromWordsTbl = wordRepository.findById(idToSearch);
            if (wordFromWordsTbl.isPresent()) {
                result.add(wordFromWordsTbl.get().getWord());
            }
        }
        return result;
    }

    public void deleteWordAndSynonyms(String word) throws Exception {
        //Get word from DB, if word doesn't exist - there is nothing to be deleted
        Word existingWord = wordRepository.findByWord(word);
        if (existingWord == null) {
            throw new Exception();
        }
        //Get synonyms of the word, and remove them all, then remove the word.
        List<Synonym> synonymList1 = synonymRepository.findByWord1Id(existingWord.getId());
        List<Synonym> synonymList2 = synonymRepository.findByWord2Id(existingWord.getId());
        synonymList1.addAll(synonymList2);
        if (synonymList1 != null) {
            synonymRepository.deleteAll(synonymList1);
        }
        wordRepository.delete(existingWord);
    }

    public void editWord(String word, String newWord) throws Exception {
        //Add check if word exists
        Word wordFromDb = wordRepository.findByWord(word);
        //If word does not exist throw exception - as there is nothing to be edited
        if (wordFromDb == null) {
            throw new Exception();
        }
        wordFromDb.setWord(newWord);
        wordRepository.save(wordFromDb);
    }

    public void assignWords(String word1, String word2) throws Exception {
        //Add check if both words exists
        Word word1FromDb = wordRepository.findByWord(word1);
        Word word2FromDb = wordRepository.findByWord(word2);
        //If words do not exist throw exception - as there is nothing to be assigned
        if (word1FromDb == null || word2FromDb == null) {
            throw new Exception();
        }
        //Insert word1 and word2 as synonyms
        Synonym synonym = new Synonym();
        synonym.setWord1(word1FromDb.getId());
        synonym.setWord2(word2FromDb.getId());
        synonymRepository.save(synonym);
    }

    public void deassignWords(String word1, String word2) throws Exception {
        //Add check if both words exists
        Word word1FromDb = wordRepository.findByWord(word1);
        Word word2FromDb = wordRepository.findByWord(word2);
        //If words do not exist throw exception - as there is nothing to be deassigned
        if (word1FromDb == null || word2FromDb == null) {
            throw new Exception();
        }
        //Get synonym of word1
        List<Synonym> listA = synonymRepository.findByWord1AndWord2Ids(word1FromDb.getId(), word2FromDb.getId());
        List<Synonym> listB = synonymRepository.findByWord1AndWord2Ids(word2FromDb.getId(), word1FromDb.getId());
        listA.addAll(listB);
        //Words are not synonyms and can not be deassigned
        if (listA.size() == 0) {
            throw new Exception();
        }
        synonymRepository.deleteAll(listA);
    }
}
