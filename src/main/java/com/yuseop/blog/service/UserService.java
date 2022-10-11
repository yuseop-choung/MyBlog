package com.yuseop.blog.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuseop.blog.model.User;
import com.yuseop.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌.
//(=IoC를 해준다 = 메모리에 대신 띄워준다.)
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public int signup(User user) {
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
}
