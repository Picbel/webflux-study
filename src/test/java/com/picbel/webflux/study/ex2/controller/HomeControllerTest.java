package com.picbel.webflux.study.ex2.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;


@Disabled("pom.xml에서 blockhound-junit-platform 의존 관계를 제거한 후에 실행해야 성공한다.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //<1>
@AutoConfigureWebTestClient // <2>
class HomeControllerTest {
    @Autowired
    WebTestClient client; // <3>

    @Test
    void test() {
        client.get().uri("/").exchange() //
                .expectStatus().isOk() //
                .expectHeader().contentType(TEXT_HTML) //
                .expectBody(String.class) //
                .consumeWith(exchangeResult -> {
                    assertThat(exchangeResult.getResponseBody()).contains("<a href=\"/add");
                });
    }
}