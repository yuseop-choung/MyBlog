package com.yuseop.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
