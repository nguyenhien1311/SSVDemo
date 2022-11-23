package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject,Integer>{
	Page<Subject> findSubjectsBySubjectNameLikeIgnoreCase(String name ,Pageable pageable);
	
	List<Subject> findSubjectsByStatusIsTrue();
}
