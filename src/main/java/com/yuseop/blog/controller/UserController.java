package com.yuseop.blog.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.yuseop.blog.config.auth.PrincipalDetail;

//인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/* 허용
//그냥 주소가 /이면 index.jsp 허용
//static 이하 /js/*, /css/*, /image/* 허용

@Controller
public class UserController {
	
	@GetMapping("/auth/signupForm")
	public String signupForm() {
		return "user/signupForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalDetail principal) {
		return "user/updateForm";
	}
}
