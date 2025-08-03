package com.example.BC_alternance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestMailConfig.class)
class BcAlternanceApplicationTests {

	@Test
	void contextLoads() {
	}

}
