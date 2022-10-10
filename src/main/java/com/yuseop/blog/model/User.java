package com.yuseop.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity	//User class가 MySQL에 테이블이 생성된다.
//@DynamicInsert
public class User {
	//id와 createDate는 비워놔도 자동으로 값이 들어가 저장될 것!!

	@Id	//Primary key
	//프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;	//시퀀스, auto_increment
	
	@Column(nullable=false, length=30)	//null이 될 수 없고, 최대 길이 30
	private String username;	//아이디
	
	@Column(nullable=false, length=100)	//비번 -> 해쉬 => 비밀번호 암호화 하여 DB에 저장
	private String password;	//비밀번호
	
	@Column(nullable=false, length=50)
	private String email;	//이메일
	
	//DB는 RoleType이라는 것이 없다.
	@Enumerated(EnumType.STRING)
	private RoleType role;	//Enum 쓰는 것이 좋다 -> 데이터에 Domain을 만들어 줄 수 있음
	//ADMIN, USER
	
	@CreationTimestamp	//시간 자동 입력
	private Timestamp createDate;	//가입 시간
	
}
