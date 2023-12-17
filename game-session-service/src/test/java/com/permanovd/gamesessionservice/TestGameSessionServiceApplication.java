package com.permanovd.gamesessionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGameSessionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(GameSessionServiceApplication::main).with(TestGameSessionServiceApplication.class).run(args);
	}

}
