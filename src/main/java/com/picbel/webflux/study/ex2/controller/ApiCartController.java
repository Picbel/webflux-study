package com.picbel.webflux.study.ex2.controller;

import com.picbel.webflux.study.ex2.domain.Cart;
import com.picbel.webflux.study.ex2.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ApiCartController {

    private final InventoryService service;

    @GetMapping("/api/carts")
    Flux<Cart> findAll() {
        return this.service.getAllCarts()
                .switchIfEmpty(this.service.newCart());
    }

    @GetMapping("/api/carts/{id}")
    Mono<Cart> findOne(@PathVariable String id) {
        return this.service.getCart(id);
    }

    @PostMapping("/api/carts/{cartId}/add/{itemId}")
    Mono<Cart> addToCart(@PathVariable String cartId, @PathVariable String itemId) {
        return this.service.addItemToCart(cartId, itemId);
    }

    @DeleteMapping("/api/carts/{cartId}/remove/{itemId}")
    Mono<Cart> removeFromCart(@PathVariable String cartId, @PathVariable String itemId) {
        return this.service.removeOneFromCart(cartId, itemId);
    }
}
