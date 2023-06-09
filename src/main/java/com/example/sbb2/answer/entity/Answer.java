package com.example.sbb2.answer.entity;

import com.example.sbb2.question.entity.Question;
import com.example.sbb2.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@ToString
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @CreatedDate
    private LocalDateTime createDate;
    @ManyToOne
    @ToString.Exclude // ToString 제외
    private Question question;
    @ManyToOne
    private SiteUser author;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    @ManyToMany
    //Set은 중복을 허용하지 않는 자료형
    private Set<SiteUser> voter;
}
