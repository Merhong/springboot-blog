package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * (아래의 내용은 DTO에 적는 내용이 아니라 따로 문서를 제작해야함.)
 * 회원가입 API 순서
 * 1. URL : http//localhost:8080/join
 * 2. Method : POST
 * 3. Request Body : username=값(String)&password=값(String)&email=값(String)
 *  - 자료형이 어떻게 되는지? : 값 뒤에 String 붙임
 * 4. MIME 타입 : X-WWW-FORM-URLENCODED
 * 5. 응답 : view(html)를 응답함.
 */

// 회원가입을 위한 DTO
// joinForm.mustache를 보고 제작한다.
// Getter와 Setter가 필요하다.
@Getter @Setter
public class joinDTO {
    private String username;
    private String password;
    private String email;
}
