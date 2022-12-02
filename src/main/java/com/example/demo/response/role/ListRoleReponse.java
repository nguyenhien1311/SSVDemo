package com.example.demo.response.role;

import java.util.List;

import com.example.demo.entities.Role;
import com.example.demo.response.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class ListRoleReponse extends ResponseBody{
	List<Role> list;
}
