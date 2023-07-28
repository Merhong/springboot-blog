package shop.mtcoding.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller 어노테이션
 * 1. 컴포넌트 스캔
 * 2. return 되는 값 : view 파일
 */

@Controller
public class UserController {

    // x-www-form-urlencoded 형식으로 들어오는 데이터 받아서 콘솔로 출력하기.
    @PostMapping("/join")
    public void join(String username, String password, String email) {
        System.out.println("이름 : " + username); // username
        System.out.println("패스워드 : " + password); // password
        System.out.println("E-mail : " + email); // email
    }

    // 브라우저 입력 : GET 요청!
    // Browser URL : IP주소:포트번호/join 입력시 호출
    @GetMapping("/joinForm")
    public String joinForm() {
        // view 파일 호출 user/joinForm 파일 호출
        return "user/joinForm";
    }

    // Browser URL : IP주소:포트번호/login 입력시 호출
    @GetMapping("/login")
    public String loginForm() {
        // view 파일 호출 user/loginForm 파일 호출
        return "user/loginForm";
    }

    // Browser URL : IP주소:포트번호/user/updateForm 입력시 호출
    @GetMapping("/user/updateForm")
    public String updateForm() {
        // view 파일 호출 user/updateForm 파일 호출
        return "user/updateForm";
    }

    // Browser URL : IP주소:포트번호/logout 입력시 호출
    @GetMapping("/logout")
    public String logout() {
        // view 파일 호출 -> Welcome File List에서 "/"가 index로 지정되어 있음!
        // redirect는 스프링에서 지원하는 문법
        return "redirect:/";
    }

}
