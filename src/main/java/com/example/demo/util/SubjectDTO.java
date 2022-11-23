package com.example.demo.util;

import com.example.demo.entities.Subject;
import com.example.demo.request.subject.SubjectCreateRequest;
import com.example.demo.request.subject.SubjectUpdateRequest;
import com.example.demo.response.subject.SubjectResponse;

public class SubjectDTO {
	public static SubjectResponse getResponse(Subject s) {
		return SubjectResponse.builder()
				.id(s.getId())
				.name(s.getSubjectName())
				.status(s.isStatus())
				.build();
	}
	
	public static Subject fromCreateRequest(SubjectCreateRequest request) {
		return Subject.builder()
				.subjectName(request.getName())
				.status(true)
				.build();
	}

	public static Subject fromUpdateRequest(SubjectUpdateRequest request,int id) {
		return Subject.builder()
				.id(id)
				.subjectName(request.getName())
				.status(true)
				.build();
	}
}
