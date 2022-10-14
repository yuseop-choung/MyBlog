package com.yuseop.blog.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuseop.blog.model.Board;
import com.yuseop.blog.model.RoleType;
import com.yuseop.blog.model.User;
import com.yuseop.blog.repository.BoardRepository;
import com.yuseop.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌.
//(=IoC를 해준다 = 메모리에 대신 띄워준다.)
@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Transactional
	public void 글쓰기(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly=true)
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly=true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
				});
	}
	
	@Transactional
	public void 글삭제하기(int id) {
		System.out.println("글삭제하기 id: " + id);
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)	
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.");
				});	//영속화시키기
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		//해당 함수로 종료 시(=Service 종료 시), 트랜잭션 종료. 이때 더티체킹 -> 자동 업데이트 = DB Flush
	}
}
