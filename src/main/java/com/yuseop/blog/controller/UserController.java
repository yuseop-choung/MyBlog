package com.yuseop.blog.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuseop.blog.config.auth.PrincipalDetail;
import com.yuseop.blog.model.KakaoProfile;
import com.yuseop.blog.model.OAuthToken;
import com.yuseop.blog.model.User;
import com.yuseop.blog.service.UserService;

//인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/* 허용
//그냥 주소가 /이면 index.jsp 허용
//static 이하 /js/*, /css/*, /img/* 허용

@Controller
public class UserController {
	
	@Value("${seop.key}")
	private String seopKey;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/auth/signupForm")
	public String signupForm() {
		return "user/signupForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}

	@GetMapping("/auth/kakao/callback")
	// ResponseBody 어노테이션: Data를 리턴해주는 컨트롤러 함수가 된다.
	public String kakaoCallback(String code) {

		// POST 방식으로 key-value 데이터 요청 (카카오쪽으로)
		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		// 헤더에 어떤 형식의 데이터를 전송할지 알려줌
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "90c0edf0f9171851120e9d7e16253946");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		System.out.println(kakaoTokenRequest);

		// Http 요청하기 -> Post 방식으로 -> 그리고 response 변수의 응답 받기
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", // 토큰 발급 요청 주소
				HttpMethod.POST, // 요청 메서드가 무엇인지
				kakaoTokenRequest, // http body 데이터, http header 값
				String.class // 응답받을 타입
		);

		// ===============================================================//

		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("카카오 액세스 토큰: " + oauthToken.getAccess_token());

		// ===============================================================//

		RestTemplate rt2 = new RestTemplate();
		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		// 헤더에 어떤 형식의 데이터를 전송할지 알려줌
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);
		// Http 요청하기 -> Post 방식으로 -> 그리고 response 변수의 응답 받기
		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, // 요청 메서드가
																												// 무엇인지
				kakaoProfileRequest, String.class // 응답받을 타입
		);

		// ===============================================================//

		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		//User Object: username, password, email
		System.out.println("카카오 프로필 정보 확인: " + kakaoProfile.getId() + " " + kakaoProfile.getKakao_account().getEmail());

		// ===============================================================//
		
		System.out.println("블로그서버 유저네임: " + kakaoProfile.getKakao_account().getEmail() 
				+ "_" + kakaoProfile.getId());
		System.out.println("블로그서버 이메일: " + kakaoProfile.getKakao_account().getEmail());
		System.out.println("블로그서버 패스워드: " + seopKey);
		
		User user = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
				.password(seopKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
		
		//기존 가입자인지 비가입자인지 체크
		User checkedUser = userService.회원찾기(user.getUsername());
		
		if(checkedUser.getUsername() == null)	 {
			//비가입자 -> 회원가입
			System.out.println("새로운 회원입니다.");
			userService.회원가입(user);
		}
		Authentication authentication = 
				authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken
						(user.getUsername(), seopKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}

	@GetMapping("/user/updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalDetail principal) {
		return "user/updateForm";
	}
}
