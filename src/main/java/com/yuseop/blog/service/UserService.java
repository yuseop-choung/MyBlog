package com.yuseop.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuseop.blog.model.User;
import com.yuseop.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌.
//(=IoC를 해준다 = 메모리에 대신 띄워준다.)
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public int 회원가입(User user) {
		//전체가 성공 시 commit, 전체 중 하나라도 실패 시 rollback
		try {
			//정상 insert 시
			userRepository.save(user);
			return 1;
		} catch (Exception e) {
			//insert 오류 시
			e.printStackTrace();
			System.out.println("UserService: 회원 가입 ():  " + e.getMessage());
		}
		return -1;
	}
	
	@Transactional(readOnly=true)//select 시 트랜젝션 시작, 서비스 종료 시 트랜젝션 종료 (정합성)
	public User 로그인(User user) {
		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
		//return userRepository.login(user.getUsername(), user.getPassword());
	}
}
