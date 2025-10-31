package com.nn.spring_note_rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringNoteRestApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
