package com.example.sbb2.question.repository;

import com.example.sbb2.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Optional<Question> findBySubject(String subject);

    //@Modifying // 아래 쿼리 어노테션이 실행되는 것이 Select가 아닌 쿼리문이면 다 붙어야함
    //nativeQuery : // nativeQuery = true 여야 MySQL 쿼리문법 사용 가능
    @Query(value = "alter table question auto_increment = 1", nativeQuery = true)
    void clearAutoIncrement();

    Optional<Question> findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);
}
