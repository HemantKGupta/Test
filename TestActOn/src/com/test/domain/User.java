package com.test.domain;



import java.util.ArrayList;
import java.util.List;


public class User {
	
	private String id;
	private List<Role> roles;
	
	
	public User() {
		roles = new ArrayList<Role>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void addRole(Role role) {
		roles.add(role);
	}

}
