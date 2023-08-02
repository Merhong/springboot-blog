package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * (아래의 내용은 DTO에 적는 내용이 아니라 따로 문서를 제작해야함.)
 * 글 수정 API 순서
 * 1. URL : http//localhost:8080/borad/{id}/update
 * 2. Method : POST
 * 3. Request Body : title=값(String)&content=값(String)
 * 4. MIME 타입 : X-WWW-FORM-URLENCODED
 * 5. 응답 : view(html)를 응답함. detail 페이지
 * // index로 돌아오면 UX가 박살난다. 수정된 화면으로 돌아오게 해야함.
 */

@Getter
@Setter
public class UpdateDTO {
    private String title;
    private String content;

}
