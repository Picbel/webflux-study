package com.picbel.webflux.study.ex2.controller;

import com.picbel.webflux.study.ex2.domain.Cart;
import com.picbel.webflux.study.ex2.domain.CartItem;
import com.picbel.webflux.study.ex2.domain.Item;
import com.picbel.webflux.study.ex2.repository.CartRepository;
import com.picbel.webflux.study.ex2.repository.ItemRepository;
import com.picbel.webflux.study.ex2.service.CartService;
import com.picbel.webflux.study.ex2.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
//    private final CartRepository cartRepository;

//    private final CartService cartService;
    private final InventoryService inventoryService;

    @GetMapping
    Mono<Rendering> home() { // <1>
        return Mono.just(Rendering.view("home.html") // <2>
                .modelAttribute("items", this.inventoryService.getInventory()) // <3>
                .modelAttribute("cart", this.inventoryService.getCart("My Cart") // <4>
                        .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }
    // end::2[]

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.inventoryService.addItemToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/remove/{id}")
    Mono<String> removeFromCart(@PathVariable String id) {
        return this.inventoryService.removeOneFromCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @PostMapping
    Mono<String> createItem(@ModelAttribute Item newItem) {
        return this.inventoryService.saveItem(newItem) //
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/delete/{id}")
    Mono<String> deleteItem(@PathVariable String id) {
        return this.inventoryService.deleteItem(id) //
                .thenReturn("redirect:/");
    }



//    @GetMapping
//    Mono<Rendering> home() {
//        return Mono.just(Rendering.view("home.html")
//                .modelAttribute("items", //
//                        this.itemRepository.findAll().doOnNext(System.out::println))
//                .modelAttribute("cart", //
//                        this.cartRepository.findById("My Cart")
//                                .defaultIfEmpty(new Cart("My Cart")))
//                .build());
//    }
//
//    @PostMapping("/add/{id}")
//    Mono<String> addToCart(@PathVariable String id){
//        return cartService.addToCart("My Cart", id)
//                .thenReturn("redirect:/");
//
//    }
//
//    @PostMapping
//    Mono<String> createItem(@ModelAttribute Item newItem) {
//        return this.itemRepository.save(newItem) //
//                .thenReturn("redirect:/");
//    }
//
//    @DeleteMapping("/delete/{id}")
//    Mono<String> deleteItem(@PathVariable String id) {
//        return this.itemRepository.deleteById(id) //
//                .thenReturn("redirect:/");
//    }
//
//    // tag::search[]
//    @GetMapping("/search") // <1>
//    Mono<Rendering> search( //
//                            @RequestParam(required = false) String name, // <2>
//                            @RequestParam(required = false) String description, //
//                            @RequestParam boolean useAnd) {
//        return Mono.just(Rendering.view("home.html") // <3>
//                .modelAttribute("items", //
//                        inventoryService.searchByExample(name, description, useAnd)) // <4>
//                .modelAttribute("cart", //
//                        this.cartRepository.findById("My Cart")
//                                .defaultIfEmpty(new Cart("My Cart")))
//                .build());
//    }
}
