package com.example.demo.util;

import com.example.demo.entities.Student;
import com.example.demo.request.student.StudentCreateRequest;
import com.example.demo.request.student.StudentUpdateRequest;
import com.example.demo.response.student.StudentResponse;

public class StudentDTO {
	
	public static Student fromCreateRequest(StudentCreateRequest s) {
		return Student.builder()
				.name(s.getName())
				.email(s.getEmail())
				.phone(s.getPhone())
				.status(true)
				.inClass(false)
				.build();
	}
	public static Student fromUpdateRequest(StudentUpdateRequest s,int id, boolean inClass) {
		return Student.builder()
				.id(id)
				.name(s.getName())
				.email(s.getEmail())
				.phone(s.getPhone())
				.status(true)
				.inClass(inClass)
				.build();
	}
	
	public static StudentResponse getResponse(Student s) {
		return StudentResponse.builder()
				.id(s.getId())
				.name(s.getName())
				.email(s.getEmail())
				.phone(s.getPhone())
				.status(s.isStatus())
				.build();
	}
}
