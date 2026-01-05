package com.ohgiraffers.restapi.section01.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Message {

    private int httpStatusCode;
    private String message;

}
