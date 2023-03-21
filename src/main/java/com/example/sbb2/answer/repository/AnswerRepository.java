package com.example.sbb2.answer.repository;

import com.example.sbb2.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    @Query(value = "alter table answer auto_increment = 1", nativeQuery = true)
    void clearAutoIncrement();
}
