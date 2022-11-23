package com.example.demo.service;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Student;
import com.example.demo.request.student.StudentCreateRequest;
import com.example.demo.request.student.StudentUpdateRequest;
import com.example.demo.response.student.StudentResponse;

public interface StudentService {
	Page<Student> getAll(int skip, int limit,String name);
	
	StudentResponse findById(int id);
	
	StudentResponse addEntity(StudentCreateRequest s);
	
	StudentResponse updateEntity(StudentUpdateRequest s, int id);
	
	void deleteEntity(int id);
}
