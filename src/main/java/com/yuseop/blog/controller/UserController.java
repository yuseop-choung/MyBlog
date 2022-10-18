package com.yuseop.blog.controller;


import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.yuseop.blog.model.OAuthToken;

//인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/* 허용
//그냥 주소가 /이면 index.jsp 허용
//static 이하 /js/*, /css/*, /img/* 허용

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
	
	@GetMapping("/auth/kakao/callback")
	//ResponseBody 어노테이션: Data를 리턴해주는 컨트롤러 함수가 된다.
	public @ResponseBody String kakaoCallback(String code) {
		
		//POST 방식으로 key-valye 데이터 요청 (카카오쪽으로)
		RestTemplate rt = new RestTemplate();
//		rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//		rt.setErrorHandler(new DefaultResponseErrorHandler() {
//			public boolean hasError(ClientHttpResponse response) throws IOException {
//				HttpStatus statusCode = response.getStatusCode();
//				return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
//			}
//		});
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		rt.getMessageConverters().add(converter);
		
		//HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		//헤더에 어떤 형식의 데이터를 전송할지 알려줌
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		//HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id ", "90c0edf0f9171851120e9d7e16253946");
		params.add("redirect_uri ", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		//HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
				new HttpEntity<>(params, headers);
		System.out.println(kakaoTokenRequest);
		
		//Http 요청하기 -> Post 방식으로 -> 그리고 response 변수의 응답 받기
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",		//토큰 발급 요청 주소
				HttpMethod.POST,		//요청 메서드가 무엇인지
				kakaoTokenRequest, 		//http body 데이터, http header 값
				String.class						//응답받을 타입
		);
		System.out.println(response);
		
		//Gson, Json Simple, ObjectMapper
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
		
		
		
		RestTemplate rt2 = new RestTemplate();
		//HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		//헤더에 어떤 형식의 데이터를 전송할지 알려줌
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		//HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = 
				new HttpEntity<>(headers2);
		//Http 요청하기 -> Post 방식으로 -> 그리고 response 변수의 응답 받기
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com",		//토큰 발급 요청 주소
				HttpMethod.POST,		//요청 메서드가 무엇인지
				kakaoProfileRequest, 		//http body 데이터, http header 값
				String.class						//응답받을 타입
		);
		
		return response.getBody();
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalDetail principal) {
		return "user/updateForm";
	}
}
