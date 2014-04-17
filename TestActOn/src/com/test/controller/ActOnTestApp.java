package com.test.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.JsonParseException;
import com.test.Datasource;
import com.test.domain.InputData;
import com.test.domain.Permission;
import com.test.domain.Role;
import com.test.domain.User;

@Path("/")
public class ActOnTestApp {

	@Context
	private UriInfo context;

	private Datasource datasource;

	@Context
	private ServletContext servletContext;

	public ActOnTestApp() {

	}

	@POST
	@Path("roles/{roleid}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String modifyPermission(InputData data,
			@PathParam("roleid") String roleid) {
		if (this.datasource == null) {
			setup();
		}
		List<String> permissionsToSet = data.permissions;
		Role role = this.datasource.getRoles().get(roleid);
		if (role != null) {
			for (String s : permissionsToSet) {
				Permission permission = datasource.getPermissions().get(s);
				if (permission == null) {
					return "Permission with id : " + s
							+ " not present in the system.";
				} else {
					role.addPermission(new Permission(s));
				}
			}
		} else {
			role = new Role(roleid);
			for (String s : permissionsToSet) {
				Permission permission = datasource.getPermissions().get(s);
				if (permission == null) {
					return "Permission with id : " + s
							+ " not present in the system.";
				} else {
					role.addPermission(new Permission(s));
				}
			}
			this.datasource.getRoles().put(roleid, role);
		}
		return "Modified sucessfully.";
	}

	@GET
	@Path("checkpermission")
	@Produces(MediaType.TEXT_PLAIN)
	public boolean checkPermissions(@QueryParam("userid") String userid,
			@QueryParam("permissionid") String permissionid) {

		if (this.datasource == null) {
			setup();
		}
		List<String> permissions = getPermissionsByUserid(userid);
		Permission permission = datasource.getPermissions().get(permissionid);
		if (permission != null && permissions.contains(permission.getName())) {
			return true;
		}
		return false;
	}

	@GET
	@Path("user/{userid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserPermissions(@PathParam("userid") String userid) {

		if (this.datasource == null) {
			setup();
		}
		List<String> permissions = getPermissionsByUserid(userid);
		if (permissions == null || permissions.size() == 0) {
			return "";
		}
		return permissions.toString();

	}

	@DELETE
	@Path("permissions/{permissionid}")
	@Produces(MediaType.TEXT_PLAIN)
	public void deletePermission(@PathParam("permissionid") String permissionid) {
		if (this.datasource == null) {
			setup();
		}
		// remove permission from roles first
		for (Role role : this.datasource.getRoles().values()) {
			for (Permission permission : role.getPermissions()) {
				if (permission.getId() == permissionid) {
					role.getPermissions().remove(permission);
				}
			}
		}
		// remove permission
		for (Iterator<Map.Entry<String, Permission>> iter = this.datasource
				.getPermissions().entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, Permission> entry = iter.next();
			if (entry.getKey() == permissionid) {
				iter.remove();
			}
		}
	}

	private List<String> getPermissionsByUserid(String userid) {
		List<String> results = new ArrayList<String>();
		User user = datasource.getUsers().get(userid);
		for (Role role : user.getRoles()) {
			/*
			 * if(role==null){ System.out.println("role is null"); }else {
			 * System
			 * .out.println("role is : "+role.getId()+" and permission size is : "
			 * +role.getPermissions().size()); }
			 */
			for (Permission permission : role.getPermissions()) {
				/*
				 * if(permission==null){
				 * System.out.println("permission is null"); }else{
				 * System.out.println
				 * ("permission is : "+permission.getId()+" and name is : "
				 * +permission.getName()); }
				 */
				String name = permission.getName();
				results.add(name);
			}
		}
		return results;
	}

	public void setup() {
		String path = servletContext.getRealPath("/");
		this.datasource = new Datasource(path);
		if (datasource == null) {
			System.out.println("Failed to get datasource.");
		} else {
			try {
				datasource.readData();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
