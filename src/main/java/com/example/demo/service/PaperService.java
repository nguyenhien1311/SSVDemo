package com.example.demo.service;

import java.util.List;

import com.example.demo.request.paper.PaperCreateRequest;
import com.example.demo.response.paper.PaperResponse;

public interface PaperService {

	List<PaperResponse> getAllByClassId(int classId);
	
	PaperResponse getByStudentIdAndClassId(int studentId,int classId);
	
	boolean savePaper(int id, int mark);
	
	List<PaperResponse> getAllByStudentId(int studentId);
	
	boolean createPaper(PaperCreateRequest request);
}
