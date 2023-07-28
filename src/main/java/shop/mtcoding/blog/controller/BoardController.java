package shop.mtcoding.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller 어노테이션
 * 1. 컴포넌트 스캔
 * 2. 리턴되는 값 : view 파일
 */
@Controller
public class BoardController {

    // Browser URL : IP주소:포트번호/ or /board 입력시 호출
    @GetMapping({"/", "/board"})
    public String index() {
        // view 파일 호출, index
        return "index";
    }

    // Browser URL : IP주소:포트번호/board/saveForm 입력시 호출
    @GetMapping({"/board/saveForm"})
    public String saveForm() {
        // view 파일 호출, board/saveForm
        return "board/saveForm";
    }

    // Browser URL : IP주소:포트번호/board/board/1 입력시 호출
    @GetMapping({"/board/1"})
    public String detail() {
        // view 파일 호출, board/detail
        return "board/detail";
    }

}
