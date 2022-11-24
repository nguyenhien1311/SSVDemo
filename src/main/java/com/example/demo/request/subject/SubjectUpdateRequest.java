package com.example.demo.request.subject;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubjectUpdateRequest {
	@NotBlank(message = "Subject name must not be empty")
	private String name;
}
