package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Paper;

public interface PaperRepository extends JpaRepository<Paper, Integer> {
	List<Paper> findAllPapersByClassId(int id);
	List<Paper> findPapersByUserId(int id);
	Optional<Paper> findPaperByUserIdAndClassId(int uid,int classId);
}
