package com.ohgiraffers.restapi.section02.reponseentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class ResponseMessage {

    /* 응답 메시지용 클래스 */

    private int httpStatus; // 응답 상태 코드(200, 201, 400, 500, ....... )
    private String message; // 응답으로 전달할 메시지
    private Map<String, Object> results; // 응답 시 전달할 데이터들

}