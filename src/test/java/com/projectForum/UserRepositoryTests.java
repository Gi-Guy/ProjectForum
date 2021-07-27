package com.projectForum;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.projectForum.user.User;
import com.projectForum.user.UserRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)//Defining to use the real DB
@Rollback(false)//We are commit to changes.
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
		String email = "test@test.com";
		User user = repo.findByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testFindUserByUsername() {
		String username = "Guy950";
		User user = repo.findByUsername(username);
		
		assertThat(user).isNotNull();
	}

	
}
