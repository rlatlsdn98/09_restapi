package com.ohgiraffers.cqrs.product.command.domain.repository;

import com.ohgiraffers.cqrs.product.command.domain.aggregate.Product;

import java.util.Optional;

/*
* ProductRepository를 상속받은 subclass가 proxy에 의존성 주입이 되어 자동으로 bean등록이 된다
* */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(long productCode);

    void deleteById(long productCode);
}
