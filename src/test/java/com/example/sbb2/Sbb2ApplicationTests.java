package com.example.sbb2;

import com.example.sbb2.answer.entity.Answer;
import com.example.sbb2.answer.repository.AnswerRepository;
import com.example.sbb2.question.entity.Question;
import com.example.sbb2.question.repository.QuestionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Sbb2ApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;

	@BeforeEach // 각 테스트 케이스가 실행되기 전에 수행되는 메서드
	void beforeEachSetting() {
		questionRepository.deleteAll(); //질문 테이블 모든 데이터 삭제;
		questionRepository.clearAutoIncrement(); //다음 삽입때 ID가 1번부터 시작되도록 설정
		answerRepository.deleteAll();
		answerRepository.clearAutoIncrement();

		Question q1 = new Question();
		q1.setSubject("sbb1");
		q1.setContent("sbb1 content");
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("sbb2");
		q2.setContent("sbb2 content");
		questionRepository.save(q2);

		Answer a1 = new Answer();
		a1.setContent("ssb2 answer");
		a1.setQuestion(q2);
		answerRepository.save(a1);
	}

//	@Test
//	void testJpa() {
//		Question q1 = new Question();
//		q1.setSubject("sbb1");
//		q1.setContent("sbb1 content");
//		questionRepository.save(q1);
//
//		Question q2 = new Question();
//		q2.setSubject("sbb2");
//		q2.setContent("sbb2 content");
//		questionRepository.save(q2);
//	}

	@Test
	void testJpaFindAll() {
		List<Question> all = questionRepository.findAll();
		assertEquals(2, all.size());
		assertEquals("sbb1 content",all.get(0).getContent());
	}

	@Test
	void testJpaFindById() {
		Optional<Question> oq = questionRepository.findById(1);
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb1", q.getSubject());
		}
	}

	@Test
	void testJpaFindBySubject() {
		Optional<Question> oq = questionRepository.findBySubject("sbb1");
		if(oq.isPresent()){
			Question q = oq.get();
			assertEquals("sbb1 content", q.getContent());
		}
	}

	@Test
	void testJpaFindBySubjectAndContent(){
		Optional<Question> oq = questionRepository.findBySubjectAndContent("sbb1","sbb1 content");
		if(oq.isPresent()){
			Question q = oq.get();
			assertEquals(1, q.getId());
		}
	}

	@Test
	void testJpaFindBySubjectLike(){
		List<Question> qList = questionRepository.findBySubjectLike("sbb%");
		assertEquals("sbb1", qList.get(0).getSubject());
	}

	@Test
	void testJpaUpdateQuestion(){
		Optional<Question> oq = questionRepository.findById(1);
		if(oq.isPresent()){
			Question q = oq.get();
			q.setSubject("sbb1 another content");
			assertEquals("sbb1 another content", q.getSubject());
		}
	}

	@Test
	void testJpaDeleteQuestion(){
		assertEquals(2, questionRepository.count());
		questionRepository.deleteById(2);
		assertEquals(1, questionRepository.count());
	}

	@Test
	void testJpaCreateAnswer() {
		Optional<Question> oq = questionRepository.findById(1);
		if(oq.isPresent()){
			Question q = oq.get();

			Answer a = new Answer();
			a.setContent("ssb1 answer");
			a.setQuestion(q);
			answerRepository.save(a);
		}
	}

	@Test
	void testJpaSelectAnswer(){
		Optional<Answer> oa = answerRepository.findById(1);
		if(oa.isPresent()){
			Answer a = oa.get();
			assertEquals("ssb2 answer", a.getContent());
		}
	}

	@Transactional
	@Test
	void testJpaFindAnswerConnection(){
		Optional<Question> oq = questionRepository.findById(2);
		if (oq.isPresent()){
			Question q = oq.get();
			List<Answer> answerList = q.getAnswerList();
			assertEquals(1, answerList.size());
		}
	}
}
