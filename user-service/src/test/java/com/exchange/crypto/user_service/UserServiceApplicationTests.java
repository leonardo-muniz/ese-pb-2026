package com.exchange.crypto.user_service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceApplicationTests {

	@Test
	void contextLoads() {}

	@Test
	void main() {
		// Passa o profile de teste nos argumentos e garante que o método não lança exceções
		assertDoesNotThrow(() -> {
			UserServiceApplication.main(new String[]{"--spring.profiles.active=test"});
		});
	}

}
