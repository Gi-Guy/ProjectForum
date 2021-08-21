package com.projectForum.Security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projectForum.Services.RoleServices;
import com.projectForum.Services.UserServices;
import com.projectForum.user.User;

@Component
public class SetupDataLoader implements
				ApplicationListener<ContextRefreshedEvent> {

	
	public static final Logger LOG
    = LoggerFactory.getLogger(SetupDataLoader.class);
	
	@Autowired
	private UserServices userServices;
	@Autowired
	private RoleServices	roleServices;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/*This enum use to represent the data condition*/
	private enum dataCondition {
			COMPLETED,
			MISSING_ROLES,
			MISSING_USERS,
			MISSING_ADMIN,
			MISSING_UNKNOWN
	}
	
	/**
	 * defining final information
	 */
	private final List<String> roles = Path.getAllRoles();
	
	
	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		// Checking if roles are already set up, and if Admin user exists.
		List<dataCondition> conditions = this.isDataExists();
		
		if(conditions.get(0).equals(dataCondition.COMPLETED))
			return;
				
		if(!conditions.isEmpty() && conditions.get(0).equals(dataCondition.MISSING_ROLES)) {			
			/* Defining roles - USER & ADMIN */
			this.setRoles();
			conditions.remove(0);
			LOG.info("RESTORED/CREATED Roles.");
		}
		if(!conditions.isEmpty()) {
			// Getting roles id
			final Role adminRole = roleServices.findRoleByName("ADMIN");
			final Role userRole = roleServices.findRoleByName("USER");
			
			// Create initial user
			if(conditions.get(0).equals(dataCondition.MISSING_USERS)) {
				// Admin:
				createUser("Admin", "admin", "admin", "admin@admin.admin", "admin", adminRole);
				// Dummy user:
				createUser("Unknown", "unknown", "unknown", "unknown@unknown.unknown", "ThisisUnknownUser", userRole);
				LOG.info("RESTORED/CREATED 'Admin' & 'Unknown' users");
			}
			else if(conditions.get(0).equals(dataCondition.MISSING_ADMIN)) {				
				// Admin:
				createUser("Admin", "admin", "admin", "admin@admin.admin", "admin", adminRole);
				LOG.info("RESTORED/CREATED 'Admin' user");
			}
			else if(conditions.get(0).equals(dataCondition.MISSING_UNKNOWN)) {
				// Dummy user:
				createUser("Unknown", "unknown", "unknown", "unknown@unknown.unknown", "ThisisUnknownUser", userRole);	
				LOG.info("RESTORED/CREATED 'Unknown' user");
			}
		}
	}
	
	/**
	 * Set new roles in database
	 */
	@Transactional
	void setRoles() {
		roleServices.setNewRoles(roles);
	}
	
	@Transactional
	User createUser(String username, String firstName, String lastName, String email, String password, Role role) {
		User user = userServices.findUserByUsername(username);
		
		if(user == null) {
			user = new User();
			user.setUsername(username);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole(role);
		}
		return userServices.saveAndreturn(user);
	}
	
	/**
	 * 	This method will check if all data that needed is exists.
	 *  - Roles exist?
	 *  - Users exist?
	 *  - Any User is missing?
	 *  
	 *  @return dataCondition.COMPLETED - no data is missing.
	 *  dataCondition.MISSING_ROLES - Table Roles is empty.
	 *  dataCondition.MISSING_USERS - Admin & Unknown are missing.
	 *  dataCondition.MISSING_ADMIN - Admin is missing.
	 *  dataCondition.MISSING_UNKNOWN - Unknown is missing.
	 *  */
	@Transactional
	private List<dataCondition> isDataExists() {
		List<dataCondition> conditions = new ArrayList<dataCondition>();
		List<Role> rolesData = roleServices.findAll();
		User admin = userServices.findUserByUsername("Admin");
		User unknown = userServices.findUserByUsername("Unknown");
		
		// Are any roles missing?
		if(rolesData.isEmpty() || rolesData == null) {
			conditions.add(dataCondition.MISSING_ROLES);
			LOG.warn("Roles data are missing.");
		}
		
		// Are any users missing?
		if(admin == null && unknown == null) {
			conditions.add(dataCondition.MISSING_USERS);
			LOG.warn("Users 'Admin' & 'Unknown' are missing.");
		}
		else if(admin != null && unknown == null) {
			conditions.add(dataCondition.MISSING_UNKNOWN);
			LOG.warn("User 'Unknown' is missing.");
		}
		else if(admin == null && unknown != null) {
			conditions.add(dataCondition.MISSING_ADMIN);
			LOG.warn("User 'Admin' is missing.");
		}

		if(conditions.isEmpty() && !rolesData.isEmpty()
				&& admin != null && unknown != null) {
			conditions.add(dataCondition.COMPLETED);
			LOG.info("No data is missing.");
		}
		
		return conditions;
	}
}