package com.projectForum.Security;

public class Path {
    /** In this area we defines all of Access Rules for all Roles*/
    private static final String[] ALL_ACCESS = {
    		"/",
    		"/index",
    		"/forum/**"
    };

    private static final String[] AUTHORIZED_ACCESS = {
    		"/post/**",
    		"/topic/newTopic",
    		"/topic/*",
    		"/a/**"
    };
    
    private static final String[] ADMIN_ACCESS = {
    		"/list_users",
    		"/forum/newForum"
    };
    final static String adminRole		=	"ADMIN";
    final static String userRole		=	"USER";
    final static String undrfinedRole	=	"UNDEFINED_USER";

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

	public static String getUndrfinedRole() {
		return undrfinedRole;
	}
	
	public static String getAuthorityRoles() {
		return adminRole + "," + userRole;
		
	}
    public static String getAllRoles() {
    	return adminRole + "," +  userRole + "," + undrfinedRole;
    }
}
