package com.nn.spring_note_rest_api;

import org.springframework.boot.SpringApplication;

public class TestSpringNoteRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringNoteRestApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
