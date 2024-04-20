package com.devicedetective.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@SpringBootApplication
@EnableMongoRepositories
public class ServerApplication {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

	public static void main(String[] args) { SpringApplication.run(ServerApplication.class, args);
		logger.info("Rtest");}
}
