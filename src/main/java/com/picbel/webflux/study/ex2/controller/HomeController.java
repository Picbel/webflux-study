package com.picbel.webflux.study.ex2.controller;

import com.picbel.webflux.study.ex2.domain.Cart;
import com.picbel.webflux.study.ex2.domain.CartItem;
import com.picbel.webflux.study.ex2.repository.CartRepository;
import com.picbel.webflux.study.ex2.repository.ItemRepository;
import com.picbel.webflux.study.ex2.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    private final CartService cartService;

    @GetMapping
    Mono<Rendering> home() { // <1>
        return Mono.just(Rendering.view("home.html") // <2>
                .modelAttribute("items", //
                        this.itemRepository.findAll()) // <3>
                .modelAttribute("cart", //
                        this.cartRepository.findById("My Cart") // <4>
                                .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id){
        return cartService.addToCart("My Cart", id)
                .thenReturn("redirect:/");

    }
}
