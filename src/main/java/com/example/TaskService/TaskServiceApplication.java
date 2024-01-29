package com.example.TaskService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class TaskServiceApplication {

	public static void main(String[] args) {

		BlockHound.install();
		ReactorDebugAgent.init();

		SpringApplication.run(TaskServiceApplication.class, args);
	}

}
