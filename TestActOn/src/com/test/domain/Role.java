package com.test.domain;

import java.util.ArrayList;
import java.util.List;

public class Role {
	private String id;
	private List<Permission> permissions;
	
	public Role(String id) {
		this();
		this.id = id;
	}
	public Role() {
		this.permissions = new ArrayList<Permission>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void addPermission(Permission permission) {
		this.permissions.add(permission);
	}

}
