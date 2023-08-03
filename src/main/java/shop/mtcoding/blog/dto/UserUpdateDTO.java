package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * (아래의 내용은 DTO에 적는 내용이 아니라 따로 문서를 제작해야함.)
 * 유저 수정 API 순서
 * 1. URL : http//localhost:8080/user/update
 * 2. Method : POST
 * 3. Request Body : username=값(String)&password=값(String)&email=값(String)
 * 4. MIME 타입 : X-WWW-FORM-URLENCODED
 * 5. 응답 : view(html)를 응답함. updateForm 페이지
 */

@Getter
@Setter
public class UserUpdateDTO {
    private String username;
    private String password;
    private String email;

}
