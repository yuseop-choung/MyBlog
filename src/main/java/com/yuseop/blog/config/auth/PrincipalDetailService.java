package com.yuseop.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yuseop.blog.model.User;
import com.yuseop.blog.repository.UserRepository;

@Service		//bean 등록
public class PrincipalDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	//Spring이 로그인 요청을 가로챌 때 username, password 변수 2개 가로채는데
	//password 부분 처리는 알아서 함.
	//우리가 username이 DB에 있는지만 이 함수에서 확인해야함.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.: " + username);
				});
		return new PrincipalDetail(principal);	//시큐리티 세션에 유저 정보 저장
	}
}
