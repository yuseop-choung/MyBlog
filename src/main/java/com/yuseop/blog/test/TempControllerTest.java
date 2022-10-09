package com.yuseop.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {
	
	//http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		//파일 리턴 시 기본 경로: src/main/resources/static
		//return "home.html" = src/main/resources/statichome.html -> home 앞에 /를 붙여야된다
		//리턴명: /home.html
		//풀네임: src/main/resources/static/home.html
		return "/home.html";
	}
	
	//http://localhost:8000/blog/temp/img
	@GetMapping("/temp/img")
	public String tempImg() {
		return "/cafe.jpg";
	}
	
	
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		//prefiex: /WEB_INF/views/
		//suffix: .jsp
		//풀네임: /WEB_INF/views/리턴값.jsp
		return "test";
	}
}
