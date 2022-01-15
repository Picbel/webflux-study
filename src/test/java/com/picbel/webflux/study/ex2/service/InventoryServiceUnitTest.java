package com.picbel.webflux.study.ex2.service;

import com.picbel.webflux.study.ex2.domain.Cart;
import com.picbel.webflux.study.ex2.domain.CartItem;
import com.picbel.webflux.study.ex2.domain.Item;
import com.picbel.webflux.study.ex2.repository.CartRepository;
import com.picbel.webflux.study.ex2.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class InventoryServiceUnitTest {
    @MockBean
    ItemRepository itemRepository;

    @MockBean
    CartRepository cartRepository;

    InventoryService inventoryService;

    @BeforeEach
    void setUp(){
        // 테스트 데이터
        Item sampleItem = new Item("item1","TV tray", "Alf TV tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        // 협력자와의 상호작용 정의
        when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new InventoryService(itemRepository, cartRepository);


    }

    @Test
    void addItemToEmptyCartShouldProductOneCartItem() throws Exception {
//        StepVerifier.create 사용해 서비스객체를 넘겨서 테스트 하는 방법도있지만 아래와 같이 TOP 레벨 방식을 더 권장한다고 한다.

        //given
        inventoryService.addItemToCart("My Cart", "item1")
                .as(StepVerifier::create) // 테스트 대상의 반환 타입을 리액터 테스트 모듈의 정적 메소드인 StepVerifier로 메소드 레퍼런스를 연결해서 테스트기능을 전담하는 리액터 타입 핸들러를 생성한다.
                .expectNextMatches(cart -> {
                    assertThat(cart.getCartItems()).extracting(CartItem::getQuantity)
                            .containsExactlyInAnyOrder(1);

                    assertThat(cart.getCartItems()).extracting(CartItem::getItem)
                            .containsExactly(new Item("item1","TV tray", "Alf TV tray", 19.99));

                    return true;

                })
                .verifyComplete();
        //when

        //then
    }
}