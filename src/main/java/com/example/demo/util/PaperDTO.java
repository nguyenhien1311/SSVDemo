package com.example.demo.util;

import com.example.demo.entities.Paper;
import com.example.demo.response.paper.PaperResponse;

public class PaperDTO {
	public static PaperResponse getResponse(Paper p) {
		return PaperResponse.builder()
				.id(p.getId())
				.theory(p.getTheory())
				.practice(p.getPractice())
				.className(p.getClassEntity().getClassName())
				.studentName(p.getUser().getFullName())
				.isPass(p.getStatus())
				.build();
	}
}
