package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Messages;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.request.user.UserCreateRequest;
import com.example.demo.response.user.LoginResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import com.example.demo.util.UserDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository repository;
	private final RoleRepository roleRepository;
	private final JwtTokenProvider jwt;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2A);
	
	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public boolean addUser(UserCreateRequest request) {
		List<Integer> listId = request.getRoleIds();
		List<Role> listRole = roleRepository.findAllById(listId);

		User user = repository.save(UserDTO.fromCreateRequest(request, listRole));
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
			throw new CustomException(Messages.MSG_023);
		}
		
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwt.generateToken(authentication);
		LoginResponse result = LoginResponse.builder()
				.userId(user.getId())
				.userName(user.getUsername())
				.token(token)
				.build();
		
		return result;
	}

	@Override
	public User getById(int id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
	}

}
