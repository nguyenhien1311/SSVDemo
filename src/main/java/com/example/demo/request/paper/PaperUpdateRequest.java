package com.example.demo.request.paper;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaperUpdateRequest {

	@NotBlank(message = "Practice mark must not be empty")
	private int practice;
	@NotBlank(message = "Theory mark must not be empty")
	private int theory;
}
