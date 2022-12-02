package com.example.demo.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.example.demo.entities.ClassEntity;
import com.example.demo.helper.PaperExcelExporter;
import com.example.demo.request.classentity.ClassCreateRequest;
import com.example.demo.request.classentity.ClassUpdateRequest;
import com.example.demo.response.RootResponse;
import com.example.demo.response.classes.ClassResponse;
import com.example.demo.response.classes.ListClassResponse;
import com.example.demo.response.classes.PageClassResponse;
import com.example.demo.response.paper.ListPaperResponse;
import com.example.demo.response.paper.PaperResponse;
import com.example.demo.service.ClassEntityService;
import com.example.demo.service.PaperService;
import com.example.demo.util.DatetimeUtil;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/class")
@RequiredArgsConstructor
public class ClassController extends AppController {

	private final ClassEntityService service;
	private final PaperService paperService;

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@GetMapping("/")
	public RootResponse getAll(@RequestParam(name = "skip", required = false, defaultValue = "0") int skip,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "subjectId", required = false, defaultValue = "0") int subjectId,
			@RequestParam(name = "key", required = false) String name) {
		Page<ClassEntity> list = service.getAll(skip, limit, name, subjectId);
		PageClassResponse response = PageClassResponse.builder().pages(list).build();
		return sucess(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@GetMapping("/data")
	public RootResponse getDataList() {
		List<ClassResponse> data = service.getData();
		ListClassResponse response = ListClassResponse.builder().list(data).build();
		return sucess(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/data/student")
	public RootResponse getDataPage(@RequestParam(name = "skip", required = false, defaultValue = "0") int skip,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "subjectId", required = false, defaultValue = "0") int subjectId,
			@RequestParam(name = "key", required = false) String name) {
		Page<ClassEntity> data = service.getDataPage(skip, limit, name, subjectId);
		PageClassResponse response = PageClassResponse.builder().pages(data).build();
		return sucess(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@GetMapping("/{id}")
	public RootResponse findById(@PathVariable(name = "id") int id) {
		ClassResponse findById = service.findById(id);
		return sucess(findById);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/{id}/excel")
	public void exportPaper(HttpServletResponse response, @PathVariable(name = "id") int id) throws IOException {

		List<PaperResponse> list = paperService.getAllByClassId(id);
		ClassResponse findById = service.findById(id);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String currentDateTime = dateFormatter.format(DatetimeUtil.NOW);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + findById.getClassName() + "_Papers_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		response.setContentType("application/octet-stream");

		PaperExcelExporter exporter = new PaperExcelExporter(list);

		exporter.export(response);
	}

	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping("/{id}")
	public ResponseEntity<RootResponse> deleteClass(@PathVariable(name = "id") int id) {
		service.deleteEntity(id);
		RootResponse response = RootResponse.builder().message(Messages.MSG_021).build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Secured({ "ROLE_ADMIN" })
	@PatchMapping("/{id}")
	public ResponseEntity<?> endClass(@PathVariable(name = "id") int id) {
		service.endClass(id);
		return ResponseEntity.ok(null);
	}

	@Secured({ "ROLE_ADMIN" })
	@PutMapping("/{id}")
	public RootResponse updateClass(@RequestBody ClassUpdateRequest request, @PathVariable("id") int id) {
		ClassResponse entity = service.updateEntity(request, id);
		return sucess(entity);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@PostMapping("/")
	public RootResponse addNewClass(@RequestBody ClassCreateRequest request) {
		ClassResponse addNew = service.addNew(request);
		return sucess(addNew);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@GetMapping("/{id}/paper")
	public RootResponse viewPapers(@PathVariable(name = "id") int id) {
		List<PaperResponse> list = paperService.getAllByClassId(id);
		ListPaperResponse response = ListPaperResponse.builder().papers(list).build();
		return sucess(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@PostMapping("/import")
	public ResponseEntity<RootResponse> importFile(@RequestParam("file") MultipartFile file) throws IOException {
		service.importFile(file);
		RootResponse response = RootResponse.builder().code(200).message(Messages.MSG_039).build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
