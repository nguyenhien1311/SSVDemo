package com.example.demo.request.classentity;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassUpdateRequest {
	@NotBlank(message = "Start date must not be empty")
	private Date startDate;
	@NotBlank(message = "End date must not be empty")
	private Date endDate;
	@NotBlank(message = "Class name must not be empty")
	private String className;

}
