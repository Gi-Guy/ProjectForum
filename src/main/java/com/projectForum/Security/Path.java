package com.projectForum.Security;

import java.util.Arrays;
import java.util.List;

public class Path {
    /** In this area we defines all of Access Rules for all Roles*/
    private static final String[] ALL_ACCESS = {
    		"/",
    		"/index",
    		"/forum/**",
    		"/topic/**",
    		"/css/**",
    		"/api/**"
    };

    private static final String[] AUTHORIZED_ACCESS = {
    		"/topic/newTopic",
    		"/topic/newTopic/**",
    		"/topic/edit/**",
    		"/topic/editTopic",
    		"/topic/delete/**",
    		"/messages/**",
    		"/user/edit/**",
    		"/user/editUser",
    		"/post/edit/**",
    		"/post/editPost",
    		"/post/delete/**",
    };
    
    private static final String[] ADMIN_ACCESS = {
    		"/list_users",
    		"/forum/newForum",
    		"/a/**"
    };
    final static String adminRole		=	"ADMIN";
    final static String userRole		=	"USER";
    final static String undrfinedRole	=	"UNDEFINED_USER";
    final static String blocked			=	"BLOCKED";

	public static String[] getAllAccess() {
		return ALL_ACCESS;
	}

	public static String[] getAuthorizedAccess() {
		return AUTHORIZED_ACCESS;
	}

	public static String[] getAdminAccess() {
		return ADMIN_ACCESS;
	}

	public static String getAdminRole() {
		return adminRole;
	}

	public static String getUserRole() {
		return userRole;
	}
	
	public static String getBlockedRole() {
		return blocked;
	}

	public static String getUndrfinedRole() {
		return undrfinedRole;
	}
	
	public static String getAuthorityRoles() {
		return adminRole + "," + userRole;
		
	}
    public static List<String> getAllRoles() {
    	final List<String> roles = Arrays.asList(getAdminRole(), getUserRole(), getBlockedRole());
    	return roles;
    }
}
