package com.example.demo.request.paper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaperCreateRequest {
	private int studentId;
	private int classId;
}
