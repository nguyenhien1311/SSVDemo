package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.ClassEntity;
import com.example.demo.request.classentity.ClassCreateRequest;
import com.example.demo.request.classentity.ClassUpdateRequest;
import com.example.demo.response.classes.ClassResponse;

public interface ClassEntityService {
	
	Page<ClassEntity>	getAll(int skip, int limit,String name, int subjectId);
	
	List<ClassResponse> getData();
	 Page<ClassEntity> getDataPage(int skip, int limit, String name, int subjectId);
    
    ClassResponse addNew(ClassCreateRequest e);
    
	ClassResponse updateEntity(ClassUpdateRequest e, int id);
    
	List<ClassEntity> getAllBySubjectId(int id);
	
    ClassResponse findById(int id);
    
    void endClass(int id);
    
    void deleteEntity(int id);

	void importFile(MultipartFile file) throws IOException;
}