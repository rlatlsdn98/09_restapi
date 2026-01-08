package com.ohgiraffers.cqrs.product.query.controller;

import com.ohgiraffers.cqrs.common.dto.ApiResponse;
import com.ohgiraffers.cqrs.product.query.dto.request.ProductSearchRequest;
import com.ohgiraffers.cqrs.product.query.dto.response.ProductDetailResponse;
import com.ohgiraffers.cqrs.product.query.dto.response.ProductListResponse;
import com.ohgiraffers.cqrs.product.query.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/* 조회 담당 컨트롤러(Query Side) */

@RestController// @ResponseBody + @Contoller
@RequiredArgsConstructor
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    @GetMapping("/products/{productCode}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProduct(@PathVariable("productCode") Long productCode) {
        ProductDetailResponse response = productQueryService.getProduct(productCode);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(
            // @ModelAttribute 생략 가능
            ProductSearchRequest productSearchRequest // 커맨드 객체
    ) {
        ProductListResponse response = productQueryService.getProducts(productSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
