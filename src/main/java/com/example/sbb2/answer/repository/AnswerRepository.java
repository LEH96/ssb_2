package com.example.sbb2.answer.repository;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    @Transactional
    @Modifying
    @Query(value = "alter table answer auto_increment = 1", nativeQuery = true)
    void clearAutoIncrement();

    Page<Answer> findAnswersByQuestion(Question question, Pageable pageable);
}
