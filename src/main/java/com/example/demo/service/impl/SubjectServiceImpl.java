package com.example.demo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Messages;
import com.example.demo.entities.Subject;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.request.subject.SubjectCreateRequest;
import com.example.demo.request.subject.SubjectUpdateRequest;
import com.example.demo.response.subject.SubjectResponse;
import com.example.demo.service.SubjectService;
import com.example.demo.util.SubjectDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

	private final SubjectRepository repository;
	
	@Override
	public Page<Subject> getAll(int skip, int limit,String name) {
		Page<Subject> data = null;
		if (name != null && name != "") {
			data = repository.findSubjectsBySubjectNameLikeIgnoreCase("%" + name + "%",
					PageRequest.of(skip, limit, Sort.by(Sort.Order.desc("status"),Sort.Order.asc("subjectName")) ));
		} else {
			data = repository.findAll(PageRequest.of(skip, limit, Sort.by(Sort.Order.desc("status"),Sort.Order.asc("subjectName")) ));
		}
		return data;
	}

	@Override
	public void insertSubject(SubjectCreateRequest request) {
		// TODO Auto-generated method stub
		repository.save(SubjectDTO.fromCreateRequest(request));
	}

	@Override
	public void updateSubject(SubjectUpdateRequest request,int id) {
		// TODO Auto-generated method stub
		Optional<Subject> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_014 + id);
		}
		repository.save(SubjectDTO.fromUpdateRequest(request,id));
	}

	@Override
	public SubjectResponse getById(int id) {
		Optional<Subject> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_014 + id);
		}
		return SubjectDTO.getResponse(findById.get());
	}

	@Override
	public boolean changeStatus(int id) {
		// TODO Auto-generated method stub
		Optional<Subject> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_014 + id);
		}
		Subject subject = findById.get();
		subject.setStatus(!subject.isStatus());
		if (repository.save(subject)!= null)
			return true;
		return false;
	}

	@Override
	public List<Subject> getData() {
		return repository.findSubjectsByStatusIsTrue();
	}

	@Override
	public void importFile(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<Subject> list = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

		XSSFSheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			if (i > 0) {
				XSSFRow row = sheet.getRow(i);
				Subject subject = Subject.builder()
				.subjectName(row.getCell(1).getStringCellValue())
				.status(true)
				.build();
				list.add(subject);
			}
		}
		try {
			repository.saveAll(list);
			System.out.println("insert completed");
		} catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			throw new CustomException(Messages.MSG_037);
		}
	}

}
