package com.example.sbb2.question.entity;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@ToString
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 200)
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;
    @CreatedDate
    private LocalDateTime createDate;
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    // OneToMany에는 객체 초기화 꼭 해줄것!
    private List<Answer> answerList = new ArrayList<>();
    @ManyToOne
    private SiteUser author;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    // OneToMany 변수 있으면 무조건 만들어주는게 좋다
    public void addAnswer(Answer answer) {
        answer.setQuestion(this);
        answerList.add(answer); //안해주면 nullPointerException 발생, 자동으로 연결되지 않음
    }
}
