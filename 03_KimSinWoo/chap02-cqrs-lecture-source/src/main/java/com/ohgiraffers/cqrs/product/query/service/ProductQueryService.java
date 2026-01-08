package com.ohgiraffers.cqrs.product.query.service;

import com.ohgiraffers.cqrs.common.dto.Pagination;
import com.ohgiraffers.cqrs.exception.BusinessException;
import com.ohgiraffers.cqrs.exception.ErrorCode;
import com.ohgiraffers.cqrs.product.query.dto.request.ProductSearchRequest;
import com.ohgiraffers.cqrs.product.query.dto.response.ProductDTO;
import com.ohgiraffers.cqrs.product.query.dto.response.ProductDetailResponse;
import com.ohgiraffers.cqrs.product.query.dto.response.ProductListResponse;
import com.ohgiraffers.cqrs.product.query.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // final이 붙은 필드를 초기화하는 생성자 추가 어노테이션
public class ProductQueryService {

    private final ProductMapper productMapper;

    /**
     * 상품 코드가 일치하는 상품 상세 조회
     * @param productCode
     * @return
     */
    /* 읽기 전용 트랜잭션을 이용해서 성능 최적화 + 실수로 데이터 변경 방지 */
    @Transactional(readOnly = true)
    public ProductDetailResponse getProduct(Long productCode) {

        /* Mybatis는 매퍼를 통해서 조회 결과를 바로 DTO에 매핑할 수 있음
        * -> JPA 보다 편함(Query Side)장점
        * */

        // Optional: Null일 경우와 아닐 경우에 대한 동작을 선택하는 null 처리 객체

        // 조회 결과가 없을 경우 예외 발생, 있을 경우 ProductDTO 반환
        ProductDTO product = Optional.ofNullable(productMapper.selectProductByCode(productCode))
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // 빌더 패턴을 이용해서 ProductDetailResponse 객체 생성
        return ProductDetailResponse.builder().product(product).build();
    }

    /* 상품 목록 조회 */
    @Transactional(readOnly = true)
    public ProductListResponse getProducts(ProductSearchRequest productSearchRequest) {
        /* 상품 목록 조회*/
        List<ProductDTO> products = productMapper.selectProducts(productSearchRequest);

        /* 해당 조건의 상품이 몇개 있는지 조회 */
        long totalItems = productMapper.countProducts(productSearchRequest);

        /* 페이징 처리를 위한 속성 값 계산 */
        int page = productSearchRequest.getPage();
        int size = productSearchRequest.getSize();

        return ProductListResponse.builder()
                .products(products) // 조회된 상품 목록
                .pagination(
                        Pagination.builder()
                                .currentPage(page) // 현재 페이지
                                .totalPage((int)Math.ceil((double)totalItems/size)) // 전체 페이지
                                .totalItems(totalItems).build()
                )
                .build();
    }

}
