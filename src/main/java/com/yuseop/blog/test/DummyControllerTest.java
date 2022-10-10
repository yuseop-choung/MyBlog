package com.yuseop.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuseop.blog.model.RoleType;
import com.yuseop.blog.model.User;
import com.yuseop.blog.repository.UserRepository;

@RestController		//html 파일이 아닌 data를 리턴해주는 restcontroller
public class DummyControllerTest {
	
	//class가 메모리에 올라갈 때 이 변수도 메모리에 같이 올라간다.
	//의존성 주입(DI)
	@Autowired
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String deleteUser(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		
		return "삭제되었습니다. id: " + id;
	}
	
	
	//save 함수는 id를 전달하지 않으면 insert
	//save 함수는 id를 전달하면 해당 id에 대한 데이터 있으면 update,
	//save 함수는 id를 전달하면 해당 id에 대한 데이터 없으면 insert
	
	@Transactional
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		//json 데이터를 요청 -> spring이 java object로 변환하여 받음 (MessageConverter 의 jackson)
		//이 때 필요한 @ -> RequestBody
		System.out.println("id: " + id);
		System.out.println("pw: " + requestUser.getPassword());
		System.out.println("email: " + requestUser.getEmail());
		
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
//		userRepository.save(user);
		
		//더티 체킹
		return user;
	}
	
	
	//http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	//한 페이지 당 2건의 데이터를 리턴받아 출력
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id",direction=Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	
	//{id}: 주소로 파라미터를 전달 받을 수 있음.
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user/4을 찾으면 내가 DB에서 못찾게되면 user가 null이 된다
		// 이 경우, return null; 이 되는데 프로그램에 문제 생기게 된다
		// Optional로 User 객체를 감싸서 가져올테니 null인지 아닌지 판단하여 return
		
		//람다식 ver
//		User user = userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("해당 사용자는 없습니다.");
//		});
		
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException> () {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}
		});
		
		//요청 -> 웹 브라우저
		//user 객체 = 자바 오브젝트
		//변환 (웹 브라우저가 이해할 수 있는 데이터로) => json
		//spring boot => MessageConverter라는 것이 응답 시 자동 작동하여 변환
		//자바 오브젝트 리턴 시 MessageConverter가 jackson라이브러리 호출하여
		//user 오브젝트를 json으로 변환하여 브라우저에게 전달
		return user;
	}
	
	
	//http://localhost:8000/blog/dummy/join (요청)
	//http의 body에 username, password, email 데이터를 갖고 (요청)
	@PostMapping("/dummy/join")
	public String join(User user) {	//key=value (약속된 규칙)
		System.out.println("id = " + user.getId());
		System.out.println("username = " + user.getUsername());
		System.out.println("password = " + user.getPassword());
		System.out.println("email = " + user.getEmail());
		System.out.println("role = " + user.getRole());
		System.out.println("createDate = " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		
		return "회원가입이 완료되었습니다.";
	}
}
