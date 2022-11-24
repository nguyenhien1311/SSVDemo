package com.example.demo.request.classentity;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCreateRequest {
	@NotBlank(message = "Class code must no tbe empty")
	private String code;
	@NotBlank(message = "Class name must no tbe empty")
	private String className;
	@NotBlank(message = "Subject Id must no tbe empty")
	private int subjectId;
}
