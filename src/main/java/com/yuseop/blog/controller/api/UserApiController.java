package com.yuseop.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuseop.blog.config.auth.PrincipalDetail;
import com.yuseop.blog.dto.ResponseDto;
import com.yuseop.blog.model.RoleType;
import com.yuseop.blog.model.User;
import com.yuseop.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@PostMapping("/auth/signupProc")
	public ResponseDto<Integer> save(@RequestBody User user) {	//username, pw, email
		System.out.println("UserApiController: save 호출");
		//실제로 DB에 insert를 하고 아래에서 return이 되면 된다.
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) {	//RequestBody는 json 데이터 만들기 위함
		userService.회원수정(user);
		//여기서는 트랜잭션이 종료되기에 DB 값은 변경됨
		//하지만 세션값은 변경되지 않은 상태이기에 우리가 직접 세션값을 변경해줄 것
		
		//세션 등록
		Authentication authentication = 
				authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
