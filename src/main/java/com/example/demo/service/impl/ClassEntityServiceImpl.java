package com.example.demo.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Messages;
import com.example.demo.entities.ClassEntity;
import com.example.demo.entities.Subject;
import com.example.demo.enums.ClassStatus;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.ClassEntityRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.request.classentity.ClassCreateRequest;
import com.example.demo.request.classentity.ClassUpdateRequest;
import com.example.demo.response.classes.ClassResponse;
import com.example.demo.service.ClassEntityService;
import com.example.demo.util.ClassEntityDTO;
import com.example.demo.util.DatetimeUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassEntityServiceImpl implements ClassEntityService {

	private final ClassEntityRepository repository;
	private final SubjectRepository subjectRepository;
	
	@Override
	public Page<ClassEntity> getAll(int skip, int limit, String name, int subjectId) {
		Page<ClassEntity> data = null;
		if (name != null && name != "") {
			if (subjectId != 0) {
				data = repository.findClassEntitiesBySubjectIdAndClassNameLike(subjectId, "%" + name + "%",
						PageRequest.of(skip, limit, Sort.by(Sort.Order.asc("className"),Sort.Order.desc("status"))));
			} else {
				data = repository.findClassEntitiesByClassNameLike("%" + name + "%",
						PageRequest.of(skip, limit,  Sort.by(Sort.Order.asc("className"),Sort.Order.desc("status"))));
			}
		} else {
			if (subjectId != 0) {
				data = repository.getAllBySubjectId(subjectId,
						PageRequest.of(skip, limit,  Sort.by(Sort.Order.asc("className"),Sort.Order.desc("status"))));
			} else {
				data = repository.findAll(PageRequest.of(skip, limit,  Sort.by(Sort.Order.asc("className"),Sort.Order.desc("status"))));
			}
		}
		List<ClassEntity> list = updateStatusListClass(data);
		PageImpl<ClassEntity> pageImpl = new PageImpl<>(list, data.getPageable(), data.getTotalElements());
		return pageImpl;
	}

	@Override
	public ClassResponse addNew(ClassCreateRequest e) {
		ClassResponse respone = null;
		ClassEntity fromCreateRequest = ClassEntityDTO.fromCreateRequest(e);
		try {
			respone = ClassEntityDTO.getRespone(repository.save(fromCreateRequest));
		} catch (ParseException e2) {
			// TODO: handle exception
			throw new CustomException(Messages.MSG_029);
		}
		return respone;
	}

	@Override
	public ClassResponse updateEntity(ClassUpdateRequest e, int id) {
		ClassResponse respone = null;
		Optional<ClassEntity> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + id);
		}
		ClassEntity fromUpdateRequest = ClassEntityDTO.fromUpdateRequest(e, findById.get());

		try {
			respone = ClassEntityDTO.getRespone(repository.save(fromUpdateRequest));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			throw new CustomException(Messages.MSG_029);
		}
		return respone;
	}

	@Override
	public ClassResponse findById(int id) {
		ClassResponse respone = null;
		Optional<ClassEntity> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + id);
		}
		try {
			respone = ClassEntityDTO.getRespone(findById.get());
		} catch (ParseException ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
		return respone;
	}

	@Override
	public void deleteEntity(int id) {
		Optional<ClassEntity> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + id);
		}
		ClassEntity entity = findById.get();
		if (!entity.getStatus().equals(ClassStatus.CREATED)) {
			throw new CustomException(Messages.MSG_025);
		}
		repository.delete(entity);
	}

	@Override
	public List<ClassResponse> getData() {
		List<ClassEntity> all = repository.findClassEntitiesByStatusNot(ClassStatus.ENDED);
		return ClassEntityDTO.getListResponse(all);
	}

	@Override
	public void endClass(int id) {
		Optional<ClassEntity> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_013 + id);
		}
		ClassEntity entity = findById.get();
		entity.setStatus(ClassStatus.ENDED);
		repository.save(entity);
	}

	@Override
	public List<ClassEntity> getAllBySubjectId(int id) {
		// TODO Auto-generated method stub
		List<ClassEntity> list = repository.findAllBySubjectId(id);

		return list;
	}

	private List<ClassEntity> updateStatusListClass(Page<ClassEntity> data) {
		return data.toList().stream().map(t -> {
			ClassEntity entity = ClassEntity.builder().id(t.getId()).className(t.getClassName())
					.startDate(t.getStartDate()).endDate(t.getEndDate()).subjectId(t.getSubjectId())
					.status(updateStatus(t)).build();
			repository.save(entity);
			return entity;
		}).toList();
	}

	private static ClassStatus updateStatus(ClassEntity c) {
		ClassStatus result = null;
		if (!c.getStatus().equals(ClassStatus.ENDED)) {
			try {
				Date endDate = DatetimeUtil.fromStringtoDate(c.getEndDate());
				Date startDate = DatetimeUtil.fromStringtoDate(c.getStartDate());
				if (DatetimeUtil.NOW.after(endDate)) {
					result = ClassStatus.ENDED;
				} else if (DatetimeUtil.NOW.after(startDate) && DatetimeUtil.NOW.before(endDate)) {
					result = ClassStatus.ONGOING;
				} else {
					if (c.getPapers().size() > 0) {
						result = ClassStatus.ONGOING;
					} else {
						result = ClassStatus.CREATED;
					}

				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new CustomException(Messages.MSG_029);
			}
			return result;
		}
		return ClassStatus.ENDED;
	}

	@Override
	public Page<ClassEntity> getDataPage(int skip, int limit, String name, int subjectId) {
		Page<ClassEntity> data = null;
		if (name != null && name != "") {
			if (subjectId != 0) {
				data = repository.findClassEntitiesBySubjectIdAndClassNameLikeAndStatusNot(subjectId, "%" + name + "%",
						ClassStatus.ENDED, PageRequest.of(skip, limit,  Sort.by(Sort.Order.asc("className"))));
			} else {
				data = repository.findClassEntitiesByClassNameLikeAndStatusNot("%" + name + "%", ClassStatus.ENDED,
						PageRequest.of(skip, limit,  Sort.by(Sort.Order.asc("className"))));
			}
		} else {
			if (subjectId != 0) {
				data = repository.getAllBySubjectIdAndStatusNot(subjectId, ClassStatus.ENDED,
						PageRequest.of(skip, limit, Sort.by(Sort.Order.asc("className"))));
			} else {
				data = repository.findClassEntitiesByStatusNot(ClassStatus.ENDED,
						PageRequest.of(skip, limit,  Sort.by(Sort.Order.asc("className"))));
			}
		}
		List<ClassEntity> list = updateStatusListClass(data);
		PageImpl<ClassEntity> pageImpl = new PageImpl<>(list, data.getPageable(), data.getTotalElements());
		return pageImpl;
	}

	@SuppressWarnings("resource")
	@Override
	public void importFile(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<ClassEntity> list = new ArrayList<ClassEntity>();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			if (i > 0) {
				XSSFRow row = sheet.getRow(i);
				String className = row.getCell(1).getStringCellValue();
				if ("".equals(className)) {
					break;
				}
				String subjectName = row.getCell(4).getStringCellValue();
				Optional<Subject> subjectOtp = subjectRepository.findBySubjectName(subjectName);
				if (subjectOtp.isEmpty()) {
					throw new CustomException(Messages.MSG_041);
				}
				
				ClassEntity entity = ClassEntity.builder()
				.className(className)
				.startDate(row.getCell(2).getStringCellValue())
				.endDate(row.getCell(3).getStringCellValue())
				.subjectId(subjectOtp.get().getId())
				.status(ClassStatus.CREATED)
				.build();
				list.add(entity);
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
