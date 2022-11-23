package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ClassEntity;
import com.example.demo.enums.ClassStatus;

@Repository
public interface ClassEntityRepository extends JpaRepository<ClassEntity, Integer>{
	Page<ClassEntity> findClassEntitiesByClassNameLike(String name ,Pageable pageable);
	
	List<ClassEntity> findClassEntitiesByStatusNot(ClassStatus status); 
	
	Page<ClassEntity> findClassEntitiesBySubjectId(int id,Pageable pageable);
	
	List<ClassEntity> findAllBySubjectId(int id);
}
