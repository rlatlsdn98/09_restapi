package com.ohgiraffers.cqrs.product.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

  private Long productCode;
  private String productName;
  private Long productPrice;
  private String productDescription;
  private CategoryDTO category;
  private String productImageUrl;
  private Long productStock;
}
