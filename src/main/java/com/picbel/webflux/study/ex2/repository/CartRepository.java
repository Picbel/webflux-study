package com.picbel.webflux.study.ex2.repository;

import com.picbel.webflux.study.ex2.domain.Cart;
import com.picbel.webflux.study.ex2.domain.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {

}
