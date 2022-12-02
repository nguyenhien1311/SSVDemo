package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Subject;
import com.example.demo.request.subject.SubjectCreateRequest;
import com.example.demo.request.subject.SubjectUpdateRequest;
import com.example.demo.response.subject.SubjectResponse;

public interface SubjectService {
	Page<Subject> getAll(int skip, int limit,String name);
	
	void insertSubject(SubjectCreateRequest request);
	void updateSubject(SubjectUpdateRequest request,int id);
	
	SubjectResponse getById(int id);
	
	List<Subject> getData();
	
	boolean changeStatus(int id);

	void importFile(MultipartFile file)  throws IOException;
}
