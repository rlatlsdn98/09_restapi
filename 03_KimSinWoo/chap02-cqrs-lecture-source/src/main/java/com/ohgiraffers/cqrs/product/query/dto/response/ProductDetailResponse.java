package com.ohgiraffers.cqrs.product.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder // 빌더 패턴을 이용한 객체 생성 코드 추가 어노테이션
public class ProductDetailResponse {

  /* API 응답 구조의 일관성과 코드 확장성,
  * JSON 데이터 응답 형태를 위해
  * DTO를 감싼 Response 클래스를 별도 생성
  * */

  private final ProductDTO product;

}
