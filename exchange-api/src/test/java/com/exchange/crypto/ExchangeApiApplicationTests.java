package com.exchange.crypto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ExchangeApiApplicationTests {

	@Test
	void contextLoads() {}

	@Test
    void main() {
        // Passa o profile de teste nos argumentos e garante que o método não lança exceções
        assertDoesNotThrow(() -> {
            ExchangeApiApplication.main(new String[]{"--spring.profiles.active=test"});
        });
    }
}