package com.picbel.webflux.study.ex2.service;

import com.picbel.webflux.study.ex2.domain.Cart;
import com.picbel.webflux.study.ex2.domain.CartItem;
import com.picbel.webflux.study.ex2.domain.Item;
import com.picbel.webflux.study.ex2.repository.CartRepository;
import com.picbel.webflux.study.ex2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
//    private final ReactiveFluentMongoOperations fluentOperations;


    public Flux<Item> getItems() {
        // imagine calling a remote service!
        return Flux.empty();
    }


//    public Flux<Item> search(String partialName, String partialDescription, boolean useAnd) {
//        if (partialName != null) {
//            if (partialDescription != null) {
//                if (useAnd) {
//                    return itemRepository //
//                            .findByNameContainingAndDescriptionContainingAllIgnoreCase( //
//                                    partialName, partialDescription);
//                } else {
//                    return itemRepository.findByNameContainingOrDescriptionContainingAllIgnoreCase( //
//                            partialName, partialDescription);
//                }
//            } else {
//                return itemRepository.findByNameContaining(partialName);
//            }
//        } else {
//            if (partialDescription != null) {
//                return itemRepository.findByDescriptionContainingIgnoreCase(partialDescription);
//            } else {
//                return itemRepository.findAll();
//            }
//        }
//    }

//    Flux<Item> searchByFluentExample(String name, String description) {
//        return fluentOperations.query(Item.class) //
//                .matching(query(where("TV tray").is(name).and("Smurf").is(description))) //
//                .all();
//    }


//    Flux<Item> searchByFluentExample(String name, String description, boolean useAnd) {
//        Item item = new Item(name, description, 0.0);
//
//        ExampleMatcher matcher = (useAnd //
//                ? ExampleMatcher.matchingAll() //
//                : ExampleMatcher.matchingAny()) //
//                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //
//                .withIgnoreCase() //
//                .withIgnorePaths("price");
//
//        return fluentOperations.query(Item.class) //
//                .matching(query(byExample(Example.of(item, matcher)))) //
//                .all();
//    }

    public Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0); // <1>

        ExampleMatcher matcher = (useAnd // <2>
                ? ExampleMatcher.matchingAll() //
                : ExampleMatcher.matchingAny()) //
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // <3>
                .withIgnoreCase() // <4>
                .withIgnorePaths("price"); // <5>

        Example<Item> probe = Example.of(item, matcher); // <6>

        return itemRepository.findAll(probe); // <7>
    }

    public Mono<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    public Mono<Item> saveItem(Item newItem) {
        return this.itemRepository.save(newItem);
    }

    public Mono<Void> deleteItem(String id) {
        return this.itemRepository.deleteById(id);
    }

    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId)) //
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny() //
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        }) //
                        .orElseGet(() -> this.itemRepository.findById(itemId) //
                                    .map(CartItem::new) //
                                    .map(cartItem -> {
                                        cart.getCartItems().add(cartItem);
                                        return cart;
                                    })
                        ))
                .flatMap(this.cartRepository::save);
    }

    public Mono<Cart> removeOneFromCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.decrement();
                            return Mono.just(cart);
                        }) //
                        .orElse(Mono.empty()))
                .map(cart -> new Cart(cart.getId(), cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getQuantity() > 0)
                        .collect(Collectors.toList())))
                .flatMap(cart -> this.cartRepository.save(cart));
    }

    public Flux<Cart> getAllCarts() {
        return this.cartRepository.findAll();
    }

    public Mono<Cart> newCart() {
        return this.cartRepository.save(new Cart("cart"));
    }
}
