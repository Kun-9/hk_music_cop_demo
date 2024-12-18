package com.hk_music_cop.demo.integration.member.repository;

import com.hk_music_cop.demo.global.security.common.Role;
import com.hk_music_cop.demo.member.dto.request.JoinRequest;
import com.hk_music_cop.demo.member.dto.response.MemberResponse;
import com.hk_music_cop.demo.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	// 테스트 전역 변수
	List<JoinRequest> testData;
	List<Long> seqValues;

	@BeforeEach
	void setup() {
		testData = Arrays.asList(
				new JoinRequest("Test1", "testData1", "1234"),
				new JoinRequest("Test2", "testData2", "1234"),
				new JoinRequest("Test3", "testData3", "1234")
		);

		seqValues = new ArrayList<>();

		testData.forEach(m -> seqValues.add(memberRepository.join(m)));
	}

	@DisplayName("회원 생성")
	@Test
	void join() {
		// given
		JoinRequest joinRequest = new JoinRequest("신동근", "userId1", "pass1");

		// when
		Long result = memberRepository.join(joinRequest);

		// then
		Assertions.assertThat(result).isNotEqualTo(null);
	}


	@Test
	@DisplayName("이름 중복 가입 불가")
	void joinDup() {
		// given
		JoinRequest joinRequest1 = new JoinRequest("신동근", "userId1", "pass1");
		JoinRequest joinRequest2 = new JoinRequest("신동근", "userId2", "pass1");
		JoinRequest joinRequest3 = new JoinRequest("임시1", "userId1", "pass1");

		// when
		memberRepository.join(joinRequest1);

		// then
		assertThrows(DuplicateKeyException.class, () -> {
			memberRepository.join(joinRequest2);
			memberRepository.join(joinRequest3);
		});
	}


	@Test
	@DisplayName("고유Id로 멤버 객체 찾기")
	void findByMemberId() {
		// given
		JoinRequest targetMember = testData.get(0);


		// when
		MemberResponse findMember = memberRepository.findByMemberId(seqValues.get(0));

		// then
		assertThat(findMember)
				.satisfies(member -> {
					assertThat(member.getName()).isEqualTo(targetMember.getName());
					assertThat(member.getUserId()).isEqualTo(targetMember.getUserId());
					assertThat(member.getPassword()).isEqualTo(targetMember.getPassword());
				});
	}


	@DisplayName("userId로 사용자 조회")
	@Test
	void findByUserId() {
		// given
		JoinRequest targetMember = testData.get(0);

		// when
		MemberResponse findMember = memberRepository.findByUserId(targetMember.getUserId());

		// then
		assertThat(findMember)
				.satisfies(member -> {
					assertThat(member.getName()).isEqualTo(targetMember.getName());
					assertThat(member.getUserId()).isEqualTo(targetMember.getUserId());
					assertThat(member.getPassword()).isEqualTo(targetMember.getPassword());
				});

	}

	@DisplayName("모든 사용자 조회")
	@Test
	void findAll() {
		// given
		List<Tuple> exceptedValues = testData.stream()
				.map(data -> tuple(data.getName(), data.getUserId(), data.getPassword()))
				.toList();


		// when
		List<MemberResponse> findData = memberRepository.findAll();

		// then

		assertThat(findData)
				.extracting("name", "userId", "password")
				.contains(
						exceptedValues.toArray(new Tuple[0])
				);

	}

	@DisplayName("join + findById 통합")
	@Test
	void joinAndFind() {
		// given
		JoinRequest joinRequest = new JoinRequest("신동근", "userId1", "pass1");

		// when
		memberRepository.join(joinRequest);

		// then
		MemberResponse findMember = memberRepository.findByUserId("userId1");

		assertThat(findMember)
				.satisfies(member -> {
					assertThat(member.getUserId()).isEqualTo(joinRequest.getUserId());
					assertThat(member.getPassword()).isEqualTo(joinRequest.getPassword());
				});
	}


	@DisplayName("사용자 역할 조회")
	@Test
	void findRoleByMemberId() {

		// given
		List<String> roleByMemberId = memberRepository.findRolesByMemberId(5L);
		List<String> expectedRole = List.of(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name());


		// when
		MemberResponse member = memberRepository.findByUserId("id1").withRoles(roleByMemberId);


		// then
		assertThat(member.getRoles())
				.containsExactlyInAnyOrder(expectedRole.toArray(String[]::new));

	}


}