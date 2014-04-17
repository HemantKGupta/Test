package com.test.domain;



public class Permission {
	private String id;
	private String name;
		
	public Permission(String id, String name) {
		this.id = id;
		this.name = name;
	}
	public Permission(String id) {
		this.id = id;
	}
	public Permission() {
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
