package com.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.test.domain.Permission;
import com.test.domain.Role;
import com.test.domain.User;

public class Datasource {

	public void readData() throws JsonParseException, IOException {
		JsonFactory jsonfactory = new JsonFactory();

		String path = this.realpath + "/permissions.json";
		JsonParser jsonParser = jsonfactory.createParser(new File(path));

		while (jsonParser.nextToken() != null) {

			if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {

				Permission permission = new Permission();
				jsonParser.nextToken();
				String fieldname = jsonParser.getCurrentName();
				if ("id".equals(fieldname)) {
					jsonParser.nextToken();
					permission.setId(jsonParser.getText());
				}
				jsonParser.nextToken();
				fieldname = jsonParser.getCurrentName();
				if ("name".equals(fieldname)) {
					jsonParser.nextToken();
					permission.setName(jsonParser.getText());
				}
				jsonParser.nextToken();
				this.permissions.put(permission.getId(), permission);
			}

		}
		jsonParser.close();

		path = this.realpath + "/roles.json";
		jsonParser = jsonfactory.createParser(new File(path));

		while (jsonParser.nextToken() != null) {

			if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {

				Role role = new Role();
				jsonParser.nextToken();
				String fieldname = jsonParser.getCurrentName();
				String idValue = null;
				if ("id".equals(fieldname)) {
					jsonParser.nextToken();
					idValue = jsonParser.getText();
					role.setId(idValue);
				}
				jsonParser.nextToken();
				fieldname = jsonParser.getCurrentName();
				if ("permissions".equals(fieldname)) {
					jsonParser.nextToken();
					while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
						String permissionId = jsonParser.getText();
						Permission permission = this.permissions
								.get(permissionId);
						if (permission == null) {
							System.out.println("No permission found.");
						} else {
							role.getPermissions().add(permission);
						}
					}
				}
				jsonParser.nextToken();
				this.roles.put(role.getId(), role);
			}

		}
		jsonParser.close();

		path = this.realpath + "/users.json";
		jsonParser = jsonfactory.createParser(new File(path));
		while (jsonParser.nextToken() != null) {

			if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
				User user = new User();
				jsonParser.nextToken();
				String fieldname = jsonParser.getCurrentName();
				String idValue = null;
				if ("id".equals(fieldname)) {
					jsonParser.nextToken();
					idValue = jsonParser.getText();
					user.setId(idValue);
				}
				jsonParser.nextToken();
				fieldname = jsonParser.getCurrentName();
				if ("roles".equals(fieldname)) {
					jsonParser.nextToken();
					while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
						String roleId = jsonParser.getText();
						Role role = this.roles.get(roleId);
						if (role == null) {
							System.out.println("while building role null");
						} else {
							user.getRoles().add(role);
						}
					}
				}
				jsonParser.nextToken();
				this.users.put(user.getId(), user);
			}
		}
		jsonParser.close();
	}

	private Map<String, User> users;
	private Map<String, Role> roles;
	private Map<String, Permission> permissions;

	private String realpath;

	public String getRealpath() {
		return realpath;
	}

	public void setRealpath(String realpath) {
		this.realpath = realpath;
	}

	public Datasource(String path) {
		this.realpath = path;
		this.users = new HashMap<String, User>();
		this.roles = new HashMap<String, Role>();
		this.permissions = new HashMap<String, Permission>();
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public Map<String, Role> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, Role> roles) {
		this.roles = roles;
	}

	public Map<String, Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Map<String, Permission> permissions) {
		this.permissions = permissions;
	}

}
