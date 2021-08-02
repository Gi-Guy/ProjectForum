package com.projectForum;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.projectForum.Security.Roles;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Using the real DB
@Rollback(false) // Committing the changes TODO make sure tests don't commit changes
public class UserRepositoryTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository repo;
	
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
		User savedUser = repo.save(user);
		User existUser = entityManager.find(User.class,savedUser.getId());
		assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
		
	}
	@Test
	public void clearData() {
		repo.deleteAll();
	}
	
	@Test
	public void testFindUserByEmail() {
		String email = "itayk747@gmail.com";
		User user = repo.findByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testFindUserByUsername() {
		String username = "itaykirsh";
		User user = repo.findByUsername(username);
		
		assertThat(user).isNotNull();
	}

	@Test
	public void updateAllUserRoles() {
		List<User> users = repo.findAll();
		for (int i=0 ; i<users.size(); i++) {
			User user = users.get(i);
			if(user.getRole() == null)
				user.setRole(Roles.USER);
		}
	}
	@Test
	public void setUserAsAdmin() {
		User user = repo.findByEmail("Tom@tom.om");
		user.setRole(Roles.AMDIN);
	}
	
}
