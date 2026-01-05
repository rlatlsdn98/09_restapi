package com.ohgiraffers.restapi.section01.response;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/* HttpMessageConverter
* - Spring에서 HTTP 요청/응답을 알맞은 형태로 자동 변환해주는 객체
* - 숫자, 문자열 -> text/plain
* - Object 타입 -> application/json
* - file -> 지정된 produces MIME type
* */

@RestController // @ResponseBody + @Controller
                // 모든 핸들러 메서드가 데이터로만 응답을 하는 컨트롤러
@RequestMapping("/response")
public class ResponseController {

    /* 1. 문자열 응답 */
    @GetMapping("/hello")
//  @ResponseBody // View Resolver를 찾아가지 않고 응답 Body에 담겨 그대로 클라이언트에게 응답
    public String helloWorld() {
        return "Hello World!";
    }

    /* 2. Object 응답 */
    @GetMapping("/message")
    public Message getMessage() {
        return new Message(200,"메세지 응답합니다.");
    }

    @GetMapping("/list")
    public List<String> getList() {
        return List.of(new String[] {"사과","딸기","바나나"});
    }

    @GetMapping("/map")
    public Map<Integer, String> getMap() {

        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(200, "정상 응답"));
        messageList.add(new Message(404, "페이지를 찾을 수 없습니다"));
        messageList.add(new Message(500, "개발자의 잘못입니다"));

        return messageList.stream()
                .collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));

    }

    /* file 응답
    * produces: 해당 api의 응답 데이터 타입을 명시하는 속성
    *
    * MediaType.IMAGE_PNG_VALUE: image/png (각 이미지 MIME TYPE)
    *
    * 반환형 byte[]: file은 byte단위 전송을 해야 깨지지 않는다
    * */
    @GetMapping(value="/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage() throws IOException {
        return getClass().getResourceAsStream("/images/example.png").readAllBytes();
    }


    /* 6. ResponseEntity 응답 */
    @GetMapping("/entity")
    public ResponseEntity<Message> getEntity() {
        return ResponseEntity.ok(new Message(200,"정상 수행"));
    }
}
