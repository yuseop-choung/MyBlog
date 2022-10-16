package com.yuseop.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.yuseop.blog.config.auth.PrincipalDetailService;

//아래 세 개는 세트라고 생각하자!
//빈 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration				//빈등록 (IoC 관리)
@EnableWebSecurity
//Security 필터 등록 = 스프링 시큐리티가 활성화 되어있는데 어떤 설정을 해당 파일에서 하겠다는 것
@EnableGlobalMethodSecurity(prePostEnabled = true)	//특정 주소로 접근 시 구너한 및 인증 미리 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean		//IoC가 됨 -> 함수가 리턴하는 값을 Spring이 관리 -> 필요시마다 가져와서 사용
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	//Security 대신 로그인해줘서 password 가로채기 하는데
	//해당 password가 무엇으로 해쉬가 되어서 가입되었는지 알아야
	//같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교 가능.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf()	.disable()							//csrf 토큰 비활성화 (테스트 시에만 걸어두는 게 좋음)
		.authorizeRequests()				//요청이 들어오면	(=if)
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/img/**")		//() 안에 속한, auth쪽 페이지로 들어오면
				.permitAll()								//허가
				.anyRequest()						//위 요청이 아닌 다른 요청들은 (=else)
				.authenticated()						//인증이 되어야 한다. (로그인되어야함)
			.and()
				.formLogin()
				.loginPage("/auth/loginForm")	//인증이 되지 않은 경우 무조건 로그인 페이지로.
				.loginProcessingUrl("/auth/loginProc")	//스프링 시큐리티가 해당 주소로 요청오는 로그인 가로채서 대신 로그인
				.defaultSuccessUrl("/");		//로그인 정상 시 메인 페이지로 이동
	}
}
