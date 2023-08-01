package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * (아래의 내용은 DTO에 적는 내용이 아니라 따로 문서를 제작해야함.)
 * 게시글 목록 API 순서
 * 1. URL : http//localhost:8080/
 * 2. Method : GET
 * 3. Request Body : title=값(String)&content=값(String)
 * 4. MIME 타입 : X-WWW-FORM-URLENCODED
 * 5. 응답 : view(html)를 응답함. index 페이지
 */

@Getter
@Setter
public class DetailDTO {
    private Integer id;
    private String title;
    private String content;
    private String createdAt;
}