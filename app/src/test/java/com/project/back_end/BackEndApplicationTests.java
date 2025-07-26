package com.project.back_end;

import com.project.back_end.services.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackEndApplicationTests {

	@Autowired
	private PasswordService passwordService;

	@Test
	void contextLoads() {
		// Test that the Spring context loads successfully
	}

	@Test
	void passwordHashingWorks() {
		String plainPassword = "testPassword123";
		String hashedPassword = passwordService.hashPassword(plainPassword);

		// Verify password was hashed (should be different from plain text)
		assertNotEquals(plainPassword, hashedPassword);

		// Verify password verification works
		assertTrue(passwordService.verifyPassword(plainPassword, hashedPassword));

		// Verify wrong password fails
		assertFalse(passwordService.verifyPassword("wrongPassword", hashedPassword));
	}

	@Test
	void passwordServiceIsNotNull() {
		assertNotNull(passwordService);
	}
}
