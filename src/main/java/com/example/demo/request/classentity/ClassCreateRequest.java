package com.example.demo.request.classentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCreateRequest {
	private String code;
	
	private String className;

	private int subjectId;
}
