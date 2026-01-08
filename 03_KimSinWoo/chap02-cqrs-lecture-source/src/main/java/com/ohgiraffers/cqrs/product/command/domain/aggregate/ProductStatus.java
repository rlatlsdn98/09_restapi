package com.ohgiraffers.cqrs.product.command.domain.aggregate;

public enum ProductStatus {
    USABLE,
    DISABLE,
    DELETED // 삭제(soft delete)

}
