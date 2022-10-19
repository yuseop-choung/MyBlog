package com.yuseop.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuseop.blog.model.RoleType;
import com.yuseop.blog.model.User;
import com.yuseop.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌.
//(=IoC를 해준다 = 메모리에 대신 띄워준다.)
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public void 회원가입(User user) {
		String rawPassword = user.getPassword();						//원본 비밀번호
		String encPassword = encoder.encode(rawPassword);		//해쉬 비밀번호
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
	
	@Transactional
	public void 회원수정(User user) {
		//수정 시에는 영속성컨텍스트에 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
		//Select를 해서 user 오브젝트를 DB로부터 가져와서 영속화시킴 -> 영속화 오브젝트 변경 후 자동 업데이트
		User persistance = userRepository.findById(user.getId())
				.orElseThrow(()->{
					return new IllegalArgumentException("회원 찾기 실패");
				});
		
		//Validation Check
		if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}
		
		//회원 수정 함수 종료시 = 서비스 종료 = 트랜젝션 종료 = 자동 commit
		//영속화된 persistance 객체의 변화가 감지되면 더티체킹이 되어 update문을 자동으로 날림.
	}
	
	@Transactional(readOnly=true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
		return user;
	}
}
