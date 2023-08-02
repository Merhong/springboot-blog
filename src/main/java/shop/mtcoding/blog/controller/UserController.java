package shop.mtcoding.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Controller 어노테이션
 * 1. 컴포넌트 스캔
 * 2. return 되는 값 : view 파일
 */

@Controller
public class UserController {

    @Autowired // IoC 컨테이너
    private UserRepository userRepository;

    @Autowired // IoC 컨테이너
    private HttpSession session; // request는 가방, session은 서랍

    // 회원정보수정
    @PostMapping("/update")
    public String update(User user) {
        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 2. 권한 체크
        if (user.getId() != sessionUser.getId()) {
            return "redirect:/loginForm";
        }
        // 3. 유효성 검사(부가 로직)
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        // 4. 핵심 기능
        userRepository.update(user);

        return "redirect:/";
    }

    // 로그인
    @PostMapping("/login")
    public String login(LoginDTO loginDTO) {
        // 유효성 검사(부가 로직)
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }

        // 핵심 기능
        try {
            User user = userRepository.findByUsernameAndPassword(loginDTO);
            session.setAttribute("sessionUser", user);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/exLogin";
        }

    }

    // 정상인(실무)
    // x-www-form-urlencoded 형식으로 들어오는 데이터 받아서 콘솔로 출력하기.
    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {

        // 유효성 검사(Validation Check)
        // null 입력 값을 제거한다.
        // 정상 접근은 절대 이 조건문을 못 탄다.
        // 공격자들이 이 조건문을 타게 됨.(PostMan 사용 같은것)
        // 따라서 친절하게 리턴 해줄 필요가 없음. 여기선 Error 페이지로 리턴
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";

        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }
        // MVC패턴의 M(Model)
        try {
            userRepository.save(joinDTO);
        } catch (Exception e) {
            return "redirect:/50x";
        }

        System.out.println("username : " + joinDTO.getUsername());
        System.out.println("password : " + joinDTO.getPassword());
        System.out.println("email : " + joinDTO.getEmail());

        return "redirect:/loginForm";
    }

    // 정상인(학생)
    // x-www-form-urlencoded 형식으로 들어오는 데이터 받아서 콘솔로 출력하기.
    // DS(컨트롤러 메서드 찾기, 바디데이터 파싱)
    // DS가 바디데이터를 파싱안하고 컨트롤러 메서드만 찾은 상황
    // @PostMapping("/join")
    // public String join(String username, String password, String email) {
    //     System.out.println("username : " + username);
    //     System.out.println("password : " + password);
    //     System.out.println("email : " + email);
    //     return "redirect:/loginForm";
    // }

    // // 비정상
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) throws IOException {
    //     // http 바디데이터가 다 들어옴, 헤더는 안들어온다.
    //     // username=ssar&password=1234&email=ssar@nate.com
    //     BufferedReader br = request.getReader();
    //     // 버퍼 소비
    //     String body = br.readLine();
    //
    //     // 아래는 버퍼가 (readline에서) 소비됐기에 값이 없어서 사용불가!!!
    //     String username = request.getParameter("username");
    //     System.out.println("username : " + username);
    //     System.out.println("body : " + body);
    //
    //     return "redirect:/loginForm";
    // }

    // // 약간 정상
    // // x-www-form-urlencoded 형식으로 들어오는 데이터 받아서 콘솔로 출력하기.
    // // DS(컨트롤러 메서드 찾기, 바디데이터 파싱)
    // // DS가 바디데이터를 파싱안하고 컨트롤러 메서드만 찾은 상황
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) {
    //     String username = request.getParameter("username");
    //     String password = request.getParameter("password");
    //     String email = request.getParameter("email");
    //     System.out.println("username : " + username);
    //     System.out.println("password : " + password);
    //     System.out.println("email : " + email);
    //     return "redirect:/loginForm";
    // }

    // 브라우저 입력 : GET 요청!
    // Browser URL : IP주소:포트번호/join 입력시 호출
    @GetMapping("/joinForm")
    public String joinForm() {
        // view 파일 호출 user/joinForm 파일 호출
        return "user/joinForm";
    }

    // Browser URL : IP주소:포트번호/login 입력시 호출
    @GetMapping("/loginForm")
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
        // 로그아웃시 세션 무효화 (내 서랍을 비우는 것)
        session.invalidate();

        // view 파일 호출 -> Welcome File List에서 "/"가 index로 지정되어 있음!
        // redirect는 스프링에서 지원하는 문법
        return "redirect:/";
    }

}
