package com.yuseop.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yuseop.blog.model.User;

import lombok.Data;
import lombok.Getter;

//Spring security가 로그인 요청 가로채서 로그인 진행 완료 후, UserDetails 타입의 오브젝트를
//Spring security의 고유의 세션저장소에 저장 -> 그때 저장되는 것이 PrincipalDetail
@Getter
public class PrincipalDetail implements UserDetails{
	private User user;	//컴포지션 (객체를 품고 있는 것) <-> extends해서 User를 들고오는 것은 상속

	public PrincipalDetail(User user) {
		this.user = user;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	//계정이 만료되지 않았는지 리턴 (true: 만료 안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//계정이 잠기지 않았는지 리턴 (true: 잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//비밀번호가 만료되지 않았는지 리턴 (true: 만료 안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//계정 활성화 되어 있는지 리턴 (true: 활성화 됨)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	//계정이 갖고 있는 권한 목록을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		//ROLE_USER - 앞에 ROLE 꼭 붙여주기
		collectors.add(()->{ return "ROLE_" + user.getRole();} );
		
		return collectors;
	}
	
}
