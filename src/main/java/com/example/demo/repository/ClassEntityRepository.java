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
	Page<ClassEntity> findClassEntitiesByClassNameLikeAndStatusNot(String name ,ClassStatus status,Pageable pageable);
	
	List<ClassEntity> findClassEntitiesByStatusNot(ClassStatus status); 
	Page<ClassEntity> findClassEntitiesByStatusNot(ClassStatus status,Pageable pageable); 
	
	Page<ClassEntity> findClassEntitiesBySubjectId(int id,Pageable pageable);
	
	Page<ClassEntity> findClassEntitiesBySubjectIdAndClassNameLike(int id, String name,Pageable pageable);
	Page<ClassEntity> findClassEntitiesBySubjectIdAndClassNameLikeAndStatusNot(int id, String name,ClassStatus status,Pageable pageable);
	
	List<ClassEntity> findAllBySubjectId(int id);
	Page<ClassEntity> getAllBySubjectId(int id,Pageable pageable);
	
	Page<ClassEntity> getAllBySubjectIdAndStatusNot(int id,ClassStatus status,Pageable pageable);
}
