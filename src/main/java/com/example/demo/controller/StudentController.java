package com.example.demo.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Messages;
import com.example.demo.constant.ResponseCode;
import com.example.demo.entities.Student;
import com.example.demo.helper.PaperExcelExporter;
import com.example.demo.request.student.StudentCreateRequest;
import com.example.demo.request.student.StudentUpdateRequest;
import com.example.demo.response.RootResponse;
import com.example.demo.response.paper.ListPaperResponse;
import com.example.demo.response.paper.PaperResponse;
import com.example.demo.response.student.PageStudentResponse;
import com.example.demo.response.student.StudentResponse;
import com.example.demo.service.PaperService;
import com.example.demo.service.StudentService;
import com.example.demo.util.DatetimeUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
@CrossOrigin(origins = "http://localhost:8081")
public class StudentController extends AppController{

	private final StudentService service;
	private final PaperService paperService;

	@GetMapping("")
	public RootResponse getAll(@RequestParam(name = "skip", required = false, defaultValue = "0") int skip,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "key", required = false) String name) {
		Page<Student> list = service.getAll(skip, limit, name);
		PageStudentResponse response = PageStudentResponse.builder()
		.pages(list)
		.build();
		return sucess(response);
	}

	@GetMapping("/{id}")
	public RootResponse findById(@PathVariable(name = "id") int id) {
		StudentResponse response = service.findById(id);
		return sucess(response);
	}

	@PostMapping("")
	public RootResponse addEntity(@Valid @RequestBody StudentCreateRequest s) {
		StudentResponse entity = service.addEntity(s);
		return sucess(entity);
	}

	@PutMapping("/{id}")
	public RootResponse updateEntity(@PathVariable(name = "id") int id, @RequestBody StudentUpdateRequest s) {
		StudentResponse entity = service.updateEntity(s, id);
		return sucess(entity);
	}

	@PatchMapping("/{id}")
	public RootResponse deleteEntity(@PathVariable(name = "id") int id) {
		service.deleteEntity(id);
		return RootResponse.builder()
				.code(ResponseCode.OK)
				.message(Messages.MSG_021)
				.build();
	}
	

	@GetMapping("/{id}/paper/{classId}")
	public RootResponse viewPaper(@PathVariable(name="id")int id,
			@PathVariable(name = "classId") int classId){
		PaperResponse list = paperService.getByStudentIdAndClassId(id,classId);
		return sucess(list);
	}
	@GetMapping("/{id}/paper")
	public RootResponse viewPapers(@PathVariable(name="id")int id){
		List<PaperResponse> list = paperService.getAllByStudentId(id);
		ListPaperResponse response = ListPaperResponse.builder()
		.papers(list)
		.build();
		return sucess(response);
	}
	
	@GetMapping("/{id}/excel")
	public void exportData(HttpServletResponse response,
			@PathVariable(name="id")int id) throws IOException{
		List<PaperResponse> list = paperService.getAllByStudentId(id);
		StudentResponse findById = service.findById(id);
		response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(DatetimeUtil.NOW);
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+findById.getName()+ "_Papers_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        PaperExcelExporter exporter = new PaperExcelExporter(list);
        
        exporter.export(response);
	}
	
}
