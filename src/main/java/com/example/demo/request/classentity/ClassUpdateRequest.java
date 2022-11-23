package com.example.demo.request.classentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassUpdateRequest {
	
	private String code;
	
	private String className;

	private int subjectId;
}
