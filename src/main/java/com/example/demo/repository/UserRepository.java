package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findUserByUsername(String username);
	
	Page<User> findUsersByRoles_Id(@Param("id")int roleId,Pageable pageable);
	Page<User> findUsersByRoles_IdAndFullNameLikeIgnoreCase(@Param("id")int roleId,String name,Pageable pageable);
}
