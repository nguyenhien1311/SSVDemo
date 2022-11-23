package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Messages;
import com.example.demo.entities.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.UserDetail;
@Service
public class UserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> findUserByUsername = repository.findUserByUsername(username);
		if (findUserByUsername.isEmpty()) {
			throw new CustomException(Messages.MSG_009 + username);
		}
		User user = findUserByUsername.get();
		List<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
		if (user.getRoles() != null) {
			user.getRoles().stream().forEach(t -> authorities.add(new SimpleGrantedAuthority(t.getRoleName())));
		}
		return new UserDetail(username, user.getPassword(), authorities);
	}

}
