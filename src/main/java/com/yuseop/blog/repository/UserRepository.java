package com.yuseop.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuseop.blog.model.User;

//해당 Jpa 레파지토리는 User 테이블이 관리하는 레파지토리이며,
//User 테이블의 Primary Key는 Integer형이다.
//Data Access Object
//자동으로 bean 등록이 된다.
//@Repository	//생략 가능하다.
public interface UserRepository extends JpaRepository<User, Integer>{
	//JPA Naming 쿼리
	
	//SELECT * FROM user WHERE username=?1 AND password=?2
	User findByUsernameAndPassword(String username, String password);
	
	//@Query(value="SELECT * FROM user WHERE username=?1 AND password=?2", nativeQuery=true)
	//User login(String username, String password);
}
