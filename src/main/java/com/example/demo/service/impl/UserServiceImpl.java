package com.example.demo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Messages;
import com.example.demo.entities.Paper;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.PaperRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.request.user.TokenCheckRequest;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.request.user.UserUpdateRequest;
import com.example.demo.response.paper.PaperResponse;
import com.example.demo.response.user.LoginResponse;
import com.example.demo.response.user.RefreshTokenResponse;
import com.example.demo.response.user.UserResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import com.example.demo.util.UserDTO;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repository;
	private final RoleRepository roleRepository;
	private final PaperRepository paperRepository;
	private final JwtTokenProvider jwt;
	private final AuthenticationManager authenticationManager;
//	private final EmailService service;
	
	private final PasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2A);

	@Override
	public Page<User> getAll(int skip, int limit, String key, int role) {
		// TODO Auto-generated method stub
		Page<User> page = null;
		if (key != null && key != "") {
			page = repository.findUsersByRoles_IdAndFullNameLikeIgnoreCase(role, "%" + key + "%",
					PageRequest.of(skip, limit, Sort.by(Sort.Order.desc("status"),Sort.Order.asc("fullName") )));
		} else {
			page = repository.findUsersByRoles_Id(role,
					PageRequest.of(skip, limit, Sort.by(Sort.Order.desc("status"),Sort.Order.asc("fullName") )));
		}
		return page;
	}

	@Override
	public boolean addUser(UserCreateRequest request) {
		User user = null;
		if (request.getRoleIds() != null) {
			List<Integer> listId = request.getRoleIds();
			List<Role> listRole = roleRepository.findAllById(listId);
			user = repository.save(UserDTO.fromCreateRequest(request, listRole));
			return (user != null) ? true : false;
		}
		Role role = roleRepository.findRoleByRoleNameLike("ROLE_USER");
		List<Role> list = new ArrayList<>();
		list.add(role);
		user = repository.save(UserDTO.fromCreateRequest(request, list));
		
//		String body = "Wellcome to our system! \n Now you can login with : \nUsername : "+ user.getUsername() + " \n Password : "+ request.getPassword();
//		
//		EmailDetails details = new EmailDetails();
//		
//		details.setMsgBody(body);
//		details.setSubject(Messages.TITLE_EMAIL);
//		details.setRecipient(user.getEmail());
//		
//		service.sendsimpleEmail(details);
		
		
		return (user != null) ? true : false;
	}

	@Override
	public LoginResponse login(LoginRequest request) {

		Optional<User> optional = repository.findUserByUsername(request.getUsername());
		if (optional.isEmpty()) {
			throw new CustomException(Messages.MSG_022);
		}
		User user = optional.get();
		boolean matches = encoder.matches(request.getPassword(), user.getPassword());
		if (!matches) {
			throw new CustomException(Messages.MSG_022);
		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String role = null;
		List<? extends GrantedAuthority> list = authentication.getAuthorities().stream().toList();
		for (GrantedAuthority grantedAuthority : list) {
			role = grantedAuthority.getAuthority();
		}
		String accessToken = jwt.generateToken(authentication);
		LoginResponse result = LoginResponse.builder().userId(user.getId()).userName(user.getUsername())
				.authorities(role).token(accessToken).build();

		return result;
	}

	@Override
	public UserResponse getById(int id) {
		// TODO Auto-generated method stub
		Optional<User> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + id);
		}
		UserResponse response = UserDTO.getResponse(findById.get());
		return response;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
	}

	@Override
	public RefreshTokenResponse refreshToken(TokenCheckRequest request) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public Integer checkExpireDate(@Valid TokenCheckRequest request) {
		// TODO Auto-generated method stub
		String token = request.getToken();
		long l = jwt.getExpireDateFromToken(token);
		long currentTimeMillis = System.currentTimeMillis();
		if (l - currentTimeMillis < 0) {
			return 401;
		}

		return 200;
	}

	@Override
	public boolean updateUser(int id, UserUpdateRequest request) {
		// TODO Auto-generated method stub
		Optional<User> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + id);
		}
		User user = findById.get();
		if (request.getPassword() != null && request.getPassword() != "") {
			user.setPassword(encoder.encode(request.getPassword()));
		}
		user.setFullName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		User save = repository.save(user);
		return (save != null) ? true : false;
	}

	@Override
	public boolean deleteUSer(int roleId, int userId) {
		// TODO Auto-generated method stub
		Optional<User> userOptional = repository.findById(userId);
		if (userOptional.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + userId);
		}
		Optional<Role> roleOptional = roleRepository.findById(roleId);
		if (roleOptional.isEmpty()) {
			throw new CustomException(Messages.MSG_031 + roleId);
		}
		User user = userOptional.get();
		if (roleId == 1) {
			user.setStatus(false);
			User save = repository.save(user);
			return (save != null) ? true : false;
		} else {
			repository.delete(user);
			return true;
		}
	}

	@Override
	public List<PaperResponse> getListPaper(int id) {
		Optional<User> findById = repository.findById(id);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_008 + id);
		}
		List<Paper> list = paperRepository.findPapersByUserId(id);
		List<PaperResponse> result = list.stream().map(t -> {
			return PaperResponse.builder()
					.id(t.getId())
					.className(t.getClassEntity().getClassName())
					.isPass(t.getStatus())
					.theory(t.getTheory())
					.practice(t.getPractice())
					.studentName(t.getUser().getFullName())
					.build();
		}).toList();
		return result;
	}

	@Override
	public void importFile(MultipartFile file,int num) throws IOException {
		// TODO Auto-generated method stub
		List<User> list = new ArrayList<User>();
		Optional<Role> findById = roleRepository.findById(num);
		if (findById.isEmpty()) {
			throw new CustomException(Messages.MSG_035 + num);
		}
		List<Role> roles = new ArrayList<>();
		roles.add(findById.get());
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			if (i > 0 ) {
				XSSFRow row = sheet.getRow(i);
				try {
					String value = row.getCell(1).getStringCellValue();
					if("".equals(value)){
						break;
					}
					String username = generateUsername(value);
					String phone =  row.getCell(3).getStringCellValue();
					User user = User.builder()
					.fullName(value)
					.email(row.getCell(2).getStringCellValue())
					.phone(phone)
					.username(username)
					.password(encoder.encode(username))
					.status(true)
					.roles(roles)
					.build();
					list.add(user);
				}catch(NullPointerException ex) {
					break;
				}
			}
		}
		try {
			repository.saveAll(list);
			System.out.println("insert completed");
		}catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			throw new CustomException(Messages.MSG_037);
		}
		
	}
	
	private String generateUsername(String fullName) {
		String username = null;
		StringBuilder backwardName = new StringBuilder();
		Faker faker = new Faker();
		
		String[] split = fullName.toLowerCase().split(" ");
		int n = split.length -1;
		for (int i = 0; i <= n; i++) {
			backwardName = backwardName.append(split[n - i]);
		}
		String number = faker.address().buildingNumber();
		username =  backwardName.toString() + number;
		return username;
	}

}
