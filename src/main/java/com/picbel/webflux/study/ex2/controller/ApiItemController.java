package com.picbel.webflux.study.ex2.controller;

import com.picbel.webflux.study.ex2.domain.Item;
import com.picbel.webflux.study.ex2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ApiItemController {

    private final ItemRepository repository;


    @GetMapping("/api/items")
    Flux<Item> findAll() { // <2>
        return this.repository.findAll();
    }


    @GetMapping("/api/items/{id}")
    Mono<Item> findOne(@PathVariable String id) { // <2>
        return this.repository.findById(id);
    }

    @PostMapping("/api/items") // <1>
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.flatMap(this.repository::save)
                .map(savedItem -> ResponseEntity
                        .created(URI.create("/api/items/" +
                                savedItem.getId()))
                        .body(savedItem));
    }

    @PutMapping("/api/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(
                                               @RequestBody Mono<Item> item,
                                               @PathVariable String id) {

        return item
                .map(content -> new Item(id, content.getName(), content.getDescription(), //
                        content.getPrice()))
                .flatMap(this.repository::save)
                .map(ResponseEntity::ok);
    }
}
