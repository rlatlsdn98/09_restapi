package com.ohgiraffers.cqrs.product.command.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA만 기본 생성자 사용 가능
@Getter
@EntityListeners(AuditingEntityListener.class) // Entity 삽입, 수정 감지 시 시간 기록
/* @SQLDelete */
@SQLDelete(sql = "UPDATE tbl_product SET status = 'DELETED' WHERE product_code = ?")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCode;

    private String productName;
    private Long productPrice;
    private String productDescription;
    private Long categoryCode;
    private String productImageUrl;
    private Long productStock;

    @CreatedDate // Entity 생성 시간을 자동 기록
    private LocalDateTime createdAt;

    @LastModifiedDate // Entity 값 변경 시간을 자동으로 기록하는 어노테이션
    private LocalDateTime modifiedAt;

    /* Product 상태를 저장할 Enum 타입을 DB에 저장할 때 문자열로 저장하게 함 */
    @Enumerated(value = EnumType.STRING)
    private ProductStatus status = ProductStatus.USABLE;


    // 이미지 변경 시 사용 될 메소드
    public void changeProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    /* 도메인 로직: 비즈니스 규칙에 따른 데이터 변경은 엔터티 내부 메소드로 캡슐화합니다. */
    public void updateProductDetails(
            @NotBlank String productName,
            @Min(value = 1) Long productPrice,
            @NotBlank String productDescription,
            @Min(value = 1) Long categoryCode,
            @Min(value = 1) Long productStock,
            ProductStatus productStatus
    ) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.categoryCode = categoryCode;
        this.productStock = productStock;
        this.status = productStatus;
    }

}
