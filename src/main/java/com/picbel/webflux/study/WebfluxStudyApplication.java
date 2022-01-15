package com.picbel.webflux.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class WebfluxStudyApplication {

	public static void main(String[] args) {
		BlockHound.builder() // 블록하운드는 SpringApplication보다 먼저 실행되어야 한다.
		.allowBlockingCallsInside(
				TemplateEngine.class.getCanonicalName(),"process" // 특정 block동작을 허용한다
		).install();

		SpringApplication.run(WebfluxStudyApplication.class, args);
	}

}
