package com.example.sbb2.question.entity;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @LazyCollection(LazyCollectionOption.EXTRA) //answerList.size() 함수가 실행될 때 select count로 가져오게 함 (성능향상)
    // OneToMany에는 객체 초기화 꼭 해줄것!
    private List<Answer> answerList = new ArrayList<>();
    @ManyToOne
    private SiteUser author;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    @ManyToMany
    //Set은 중복을 허용하지 않는 자료형
    private Set<SiteUser> voter;
    private Integer view = 0;

    // OneToMany 변수 있으면 무조건 만들어주는게 좋다
    public void addAnswer(Answer answer) {
        answer.setQuestion(this);
        answerList.add(answer); //안해주면 nullPointerException 발생, 자동으로 연결되지 않음
    }
}
