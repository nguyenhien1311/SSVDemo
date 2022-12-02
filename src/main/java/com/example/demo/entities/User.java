package com.example.demo.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "Users", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "username" }),
		@UniqueConstraint(columnNames = { "email"}),
		@UniqueConstraint(columnNames = { "phone" }) })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email",unique = true)
	private String email;

	@Column(name = "phone",unique = true)
	private String phone;

	@Column(name = "username",unique = true)
	private String username;

	@Column(name = "password")
	@Length(min = 5, message = "Password need at least 5 characters")
	private String password;

	@Column(name = "status")
	private boolean status;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Paper> papers;

	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Fetch(FetchMode.JOIN)
	private List<Role> roles;
}
