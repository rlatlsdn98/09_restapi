package com.ohgiraffers.cqrs.product.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
  private Long categoryCode;
  private String categoryName;
}
