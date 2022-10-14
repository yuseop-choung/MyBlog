package com.yuseop.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	//auto_increment
	private int id;
	
	@Column(nullable=false, length=100)
	private String title;
	
	@Lob	//대용량 데이터
	private String content;	//섬머노트 라이브러리 사용. <html>태그가 섞여서 디자인됨.
	
	private int count;	//조회수
	
	//연관관계 -> Many = Board, One = User => 한명의 유저는 여러 게시글 작성
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")	//DB만들어질 때 user는 userId라는 필드로 만들어짐
	private User user;	//DB는 오브젝트를 저장할 수 없다. foreign key. 자바는 오브젝트 저장 가능.
	
	//mappedBy: 연관관계의 주인이 아니다. FK가 아니기에 DB에 컬럼 생성 x. join문을 통해 값 얻기 위함.
	//mappedBy='해당 객체 내의 필드 이름'
	@OneToMany(mappedBy="board", fetch=FetchType.EAGER)
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;
}
