package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Messages;
import com.example.demo.constant.ResponseCode;
import com.example.demo.entities.ClassEntity;
import com.example.demo.entities.Subject;
import com.example.demo.request.subject.SubjectCreateRequest;
import com.example.demo.request.subject.SubjectUpdateRequest;
import com.example.demo.response.RootResponse;
import com.example.demo.response.classes.ListClassRawResponse;
import com.example.demo.response.subject.ListSubjectResponse;
import com.example.demo.response.subject.PageSubjectResponse;
import com.example.demo.response.subject.SubjectResponse;
import com.example.demo.service.ClassEntityService;
import com.example.demo.service.SubjectService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subject")
public class SubjectController  extends AppController{
	private final SubjectService service;
	private final ClassEntityService entityService;

	@Secured({"ROLE_SUPERVISOR","ROLE_ADMIN"})
	@GetMapping("/")
	public RootResponse getAll(@RequestParam(name = "skip", required = false, defaultValue = "0") int skip,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "key", required = false) String name) {
		Page<Subject> all = service.getAll(skip,limit,name);
		PageSubjectResponse response = PageSubjectResponse.builder()
		.pages(all)
		.build();
		return sucess(response);
	}
	@Secured({"ROLE_SUPERVISOR","ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/data")
	public RootResponse getData(){
		List<Subject> list = service.getData();
		ListSubjectResponse response = ListSubjectResponse.builder()
		.list(list)
		.build();
		return sucess(response);
	}
	@Secured({"ROLE_SUPERVISOR","ROLE_ADMIN"})
	@GetMapping("/{id}")
	public RootResponse getById(@PathVariable("id")int id) {
		SubjectResponse all = service.getById(id);
		return sucess(all);
	}
	@Secured({"ROLE_SUPERVISOR","ROLE_ADMIN"})
	@GetMapping("/{id}/classes")
	public RootResponse getDataById(@PathVariable("id")int id) {
		List<ClassEntity> all = entityService.getAllBySubjectId(id);
		ListClassRawResponse response = ListClassRawResponse.builder()
		.list(all)
		.build();
		return sucess(response);
	}
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/")
	public ResponseEntity<RootResponse> insertSubject(@RequestBody SubjectCreateRequest request){
		service.insertSubject(request);
		 RootResponse response = RootResponse.builder()
				.code(ResponseCode.CREATED)
				.message(Messages.MSG_001)
				.build();
		 return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}")
	public ResponseEntity<RootResponse> updateSubject(
			@PathVariable("id")Integer id
			,@RequestBody SubjectUpdateRequest request){
		service.updateSubject(request,id);
		 RootResponse response = RootResponse.builder()
				.code(ResponseCode.CREATED)
				.message(Messages.MSG_002)
				.build();
		 return ResponseEntity.status(HttpStatus.OK).body(response);
	} 
	@Secured({"ROLE_ADMIN"})
	@PatchMapping("/{id}")
	public ResponseEntity<RootResponse> changeStatus(@PathVariable("id")int id){
		boolean changeStatus = service.changeStatus(id);
		String result = changeStatus ? "Completed" : "failed";
		 RootResponse response = RootResponse.builder()
				.code(ResponseCode.OK)
				.message(Messages.MSG_006 + result)
				.build();
		 return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/import")
	public ResponseEntity<RootResponse> importFile(@RequestParam("file")MultipartFile file) throws IOException{
		service.importFile(file);
		RootResponse response = RootResponse.builder()
				.code(200)
				.message(Messages.MSG_040)
				.body(null)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
