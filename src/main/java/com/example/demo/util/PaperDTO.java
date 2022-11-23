package com.example.demo.util;

import com.example.demo.entities.Paper;
import com.example.demo.response.paper.PaperResponse;

public class PaperDTO {
	public static PaperResponse getResponse(Paper p) {
		return PaperResponse.builder()
				.id(p.getId())
				.mark(p.getMark())
				.className(p.getClassEntity().getClassName())
				.studentName(p.getStudent().getName())
				.isPass(p.getStatus())
				.build();
	}
}
