package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * (아래의 내용은 DTO에 적는 내용이 아니라 따로 문서를 제작해야함.)
 * 회원가입 API 순서
 * 1. URL : http//localhost:8080/join
 * 2. Method : POST
 * 3. Request Body : username=값(String)&password=값(String)&email=값(String)
 * - 자료형이 어떻게 되는지? : 값 뒤에 String 붙임
 * 4. MIME 타입 : X-WWW-FORM-URLENCODED
 * 5. 응답 : view(html)를 응답함.
 */

//  Select를 위해서 뿐만 아니라 요청(POST)을 위해서도 DTO를 사용할 수 있음
//  요청에 String, Interger등 여러타입이 섞여 있는 경우 클래스 DTO로 받을 수 있음
//  게터, 세터를 만들어주면 자동으로 new해서 생성해줌.

// 회원가입을 위한 DTO
// joinForm.mustache를 보고 제작한다.
// Getter와 Setter가 필요하다.
@Getter
@Setter
public class JoinDTO {
    private String username;
    private String password;
    private String email;
}
