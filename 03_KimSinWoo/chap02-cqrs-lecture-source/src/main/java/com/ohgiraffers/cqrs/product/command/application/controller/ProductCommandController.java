package com.ohgiraffers.cqrs.product.command.application.controller;

import com.ohgiraffers.cqrs.common.dto.ApiResponse;
import com.ohgiraffers.cqrs.product.command.application.dto.request.ProductCreateRequest;
import com.ohgiraffers.cqrs.product.command.application.dto.request.ProductUpdateRequest;
import com.ohgiraffers.cqrs.product.command.application.dto.response.ProductCommandResponse;
import com.ohgiraffers.cqrs.product.command.application.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/* 데이터 변경 명령 담당 (Command side) */
@RestController
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandService productCommandService;

    /* 상품 등록 */
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductCommandResponse>> createProduct(
            @RequestPart @Validated ProductCreateRequest productCreateRequest,
            @RequestPart MultipartFile productImg
    ) {
        /* 전달 받은 ProductCreateRequest의 데이터를 이용해 DB의 새 데이터 삽입 후 삽입된 행의 PK(ProductCode) 반환 받기 */
        Long productCode = this.productCommandService.createProduct(productCreateRequest, productImg);

        ProductCommandResponse productCommandResponse
                = ProductCommandResponse.builder().productCode(productCode).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(productCommandResponse));
    }

    /* 상품 수정 */
    @PutMapping("/products/{productCode}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @PathVariable Long productCode,
            @RequestPart @Validated ProductUpdateRequest productUpdateRequest,
            @RequestPart(required = false) MultipartFile productImg
    ) {
        this.productCommandService.updateProduct(productCode, productUpdateRequest, productImg);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 상품 삭제 */
    @DeleteMapping("/products/{productCode}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productCode) {
        this.productCommandService.deleteProduct(productCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(null));
    }

}
