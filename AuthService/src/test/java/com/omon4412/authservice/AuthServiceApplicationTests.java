package com.omon4412.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureTestDatabase
@SpringBootTest
@ActiveProfiles(value = "test")
class AuthServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
