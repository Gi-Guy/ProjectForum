package com.projectForum;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useRepresentation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.projectForum.Security.Role;
import com.projectForum.Security.RoleRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Using the real DB
@Rollback(false) // Committing the changes TODO make sure tests don't commit changes
public class UserRepositoryTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setUsername("Guy950");
		user.setFirstName("Guy");
		user.setLastName("Gips");
		user.setEmail("Guy2@guy.com");
		user.setPassword("123456789");
		
		//repo.save(user) //<--Saving the new user to our DB
		
		//Doing some testing.
		User savedUser = userRepo.save(user);
		User existUser = entityManager.find(User.class,savedUser.getId());
		assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
		
	}
	@Test
	public void clearData() {
		userRepo.deleteAll();
	}
	
	@Test
	public void testFindUserByEmail() {
		String email = "itayk747@gmail.com";
		User user = userRepo.findByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testFindUserByUsername() {
		String username = "itaykirsh";
		User user = userRepo.findByUsername(username);
		
		assertThat(user).isNotNull();
	}

	@Test
	public void testFindUserRole() {
		User user = userRepo.findByUsername("Admin");
		
		Role role = roleRepo.findRoleByName("ADMIN");
		Role role2 = user.getRoles().iterator().next();
//		System.err.println(user.getRoles().iterator().next().getName());
		System.err.println(role.equals(role2));

	}
	
	@Test
	public void displayAllAdmins() {
		Role role = roleRepo.findRoleByName("ADMIN");
		List<User> admins = userRepo.findAll();
		// find it
		System.err.println("my size is: " + admins.size());
		
	}
	@Test
	public void SetUserRole() {
		User user = userRepo.findByUsername("Restful");
		Role role = roleRepo.findRoleByName("USER");
		
		user.setRole(role);
		userRepo.save(user);
	}
	
}
