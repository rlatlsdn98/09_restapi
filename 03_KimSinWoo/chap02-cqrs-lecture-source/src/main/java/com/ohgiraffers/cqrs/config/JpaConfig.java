package com.ohgiraffers.cqrs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA Auditing 활성화: Entity의 생성, 수정 시각을 자동으로 기록하는 기능
public class JpaConfig {

}
