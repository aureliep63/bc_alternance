package com.example.BC_alternance;

import com.example.BC_alternance.config.FirebaseConfig;
import com.example.BC_alternance.config.TestFirebaseConfig;
import com.google.firebase.FirebaseApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@Import({TestMailConfig.class, TestFirebaseConfig.class})
@ActiveProfiles("test")
class BcAlternanceApplicationTests {

	@MockitoBean
	private FirebaseConfig firebaseConfig;

	@MockitoBean
	private FirebaseApp firebaseApp;

	@Test
	void contextLoads() {
	}

}
