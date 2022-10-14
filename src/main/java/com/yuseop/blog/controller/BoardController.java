package com.yuseop.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.yuseop.blog.config.auth.PrincipalDetail;
import com.yuseop.blog.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@GetMapping({"", "/"})
	public String index(Model model, @PageableDefault(size=3, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {	//model = jsp에서는 request 정보
		model.addAttribute("boards", boardService.글목록(pageable));
		
		//	/WEB-INF/views/index.jsp
		return "index";
		//Controller는 리턴 시 viewResolver 작동
		//작동 시 해당 인덱스 페이지로 모델의 정보를 들고 이동
		//viewResolver는 리턴 index에 앞뒤로 prefix + suffix 붙여줌
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/detail";
	}
	
	@GetMapping("/board/{id}/editForm")
	public String editForm(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/editForm";
	}
	
	//USER 권한이 필요
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
}
