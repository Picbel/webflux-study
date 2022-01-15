package com.picbel.webflux.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebfluxStudyApplicationTests {

	@Test
	void contextLoads() {
	}

	@DisplayName("블록하운드 테스트에서 동작 확인")
	@Test
	void threadSleepIsABlockCall(){
		Mono.delay(Duration.ofSeconds(1))
				.flatMap(tick -> {
					try{
						Thread.sleep(10);
						return Mono.just(true);
					}catch (Exception e){
						return Mono.error(e);
					}
				})
				.as(StepVerifier::create)
				.verifyErrorMatches(throwable -> {
					assertThat(throwable.getMessage())
							.contains("Blocking call! java.lang.Thread.sleep");
					return true;
				});
	}

}
