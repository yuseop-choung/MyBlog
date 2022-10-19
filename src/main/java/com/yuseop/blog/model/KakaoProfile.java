package com.yuseop.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class KakaoProfile {

	public Long id;
	public String connected_at;
	public Properties properties;
	public KakaoAccount kakao_account;

	@Data
	@JsonIgnoreProperties(ignoreUnknown=true)
	public class Properties {
		public String nickname;
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown=true)
	public class KakaoAccount {
		//public Boolean profileNicknameNeedsAgreement;
		public Profile profile;
		//public Boolean hasEmail;
		//public Boolean emailNeedsAgreement;
		//public Boolean isEmailValid;
		//public Boolean isEmailVerified;
		public String email;
		
		@Data
		@JsonIgnoreProperties(ignoreUnknown=true)
		public class Profile {
			public String nickname;
		}
	}
}