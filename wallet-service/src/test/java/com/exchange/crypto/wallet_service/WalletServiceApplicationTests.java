package com.exchange.crypto.wallet_service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WalletServiceApplicationTests {

	@Test
	void contextLoads() {}

	@Test
    void main() {
        // Passa o profile de teste nos argumentos e garante que o método não lança exceções
        assertDoesNotThrow(() -> {
            WalletServiceApplication.main(new String[]{"--spring.profiles.active=test"});
        });
    }
}
