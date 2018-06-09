package ru.mozlab.example.reverseword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.mozlab.example.reverseword.domain.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
// Проверить работу H2 в mysql моде с on duplicate key
    @Modifying
    @Query(value="insert into word(word, count) values(:word, 1) on duplicate key update count=count+1",
                 nativeQuery=true)
    public void save(@Param("word") String word);
};