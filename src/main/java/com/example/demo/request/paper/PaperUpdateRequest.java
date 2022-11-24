package com.example.demo.request.paper;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaperUpdateRequest {
	@NotBlank(message = "Mark must no tbe empty")
	private int mark;
}
