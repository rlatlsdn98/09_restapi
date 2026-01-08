package com.ohgiraffers.cqrs.product.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCommandResponse {

    private Long productCode;

}
