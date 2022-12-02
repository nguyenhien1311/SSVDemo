package com.example.demo.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Messages;
import com.example.demo.entities.User;
import com.example.demo.helper.PaperExcelExporter;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.request.user.UserUpdateRequest;
import com.example.demo.response.RootResponse;
import com.example.demo.response.paper.ListPaperResponse;
import com.example.demo.response.paper.PaperResponse;
import com.example.demo.response.user.PageUserReponse;
import com.example.demo.response.user.UserResponse;
import com.example.demo.service.UserService;
import com.example.demo.util.DatetimeUtil;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController extends AppController {
	private final UserService service;

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@GetMapping("/")
	public RootResponse getAll(@RequestParam(name = "key", required = false) String key,
			@RequestParam(name = "skip", required = false, defaultValue = "0") int skip,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "role", required = false, defaultValue = "1") int role) {

		Page<User> list = service.getAll(skip, limit, key, role);
		PageUserReponse build = PageUserReponse.builder().pages(list).build();
		return sucess(build);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/{id}")
	public RootResponse getById(@PathVariable("id") int id) {
		UserResponse user = service.getById(id);
		return sucess(user);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@PutMapping("/{id}")
	public ResponseEntity<RootResponse> update(@PathVariable("id") int id,
			@Valid @RequestBody UserUpdateRequest request) {
		boolean result = service.updateUser(id, request);
		String msg = result ? Messages.MSG_002 : Messages.MSG_030;
		RootResponse response = RootResponse.builder().code(200).message(msg).build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@PostMapping()
	public ResponseEntity<RootResponse> create(@Valid @RequestBody UserCreateRequest request) {
		boolean addUser = service.addUser(request);
		String result = addUser ? Messages.MSG_001 : Messages.MSG_038;
		RootResponse response = RootResponse.builder().code(200).message(result).build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping("/{id}")
	public ResponseEntity<RootResponse> delete(@PathVariable("id") int id, @RequestParam("roleId") int roleId) {
		boolean result = service.deleteUSer(roleId, id);
		String msg = result ? Messages.MSG_021 : Messages.MSG_032;
		RootResponse response = RootResponse.builder().code(200).message(msg).build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/{id}/paper")
	public RootResponse getPaper(@PathVariable("id") int id) {
		List<PaperResponse> listPaper = service.getListPaper(id);
		ListPaperResponse response = ListPaperResponse.builder().papers(listPaper).build();
		return sucess(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/{id}/excel")
	public void export(HttpServletResponse response, @PathVariable("id") int id) throws IOException {

		List<PaperResponse> list = service.getListPaper(id);
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String currentDateTime = dateFormatter.format(DatetimeUtil.NOW);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename = Report_Papers_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		response.setContentType("application/octet-stream");

		PaperExcelExporter exporter = new PaperExcelExporter(list);

		exporter.export(response);
	}

	@Secured({ "ROLE_SUPERVISOR", "ROLE_ADMIN" })
	@PostMapping("/import")
	public ResponseEntity<RootResponse> importFile(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "num", required = false, defaultValue = "1") int num) throws IOException {
		service.importFile(file, num);
		RootResponse response = RootResponse.builder().code(200).message(Messages.MSG_036).body(null).build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
