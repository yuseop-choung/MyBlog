package com.yuseop.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuseop.blog.model.Board;
import com.yuseop.blog.model.User;

public interface BoardRepository extends JpaRepository<Board, Integer>{
}
