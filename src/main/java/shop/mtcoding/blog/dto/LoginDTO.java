package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * (아래의 내용은 DTO에 적는 내용이 아니라 따로 문서를 제작해야함.)
 * 로그인 API 순서
 * 1. URL : http//localhost:8080/login
 * 2. Method : POST (로그인은 Select지만, POST 요청을 쓴다! GET쓰면 password 노출됨.)
 * 3. Request Body : username=값(String)&password=값(String)
 * - 자료형이 어떻게 되는지? : 값 뒤에 String 붙임
 * 4. MIME 타입 : X-WWW-FORM-URLENCODED
 * 5. 응답 : view(html)를 응답함. index 페이지
 */

@Getter
@Setter
public class LoginDTO {
    private String username;
    private String password;
}