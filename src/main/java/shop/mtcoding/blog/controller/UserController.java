package shop.mtcoding.blog.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
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

    // localhost:8080/check?username=ssar
    // AJAX 통신, 뷰가 아닌 데이터를 전송
    // 유저네임 중복체크 -> 회원가입시에 사용
    @GetMapping("/check")
    public ResponseEntity<String> check(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new ResponseEntity<String>("유저네임이 중복되었습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("유저네임을 사용할 수 있습니다.", HttpStatus.OK);
    }

    // 회원정보수정 update()
    @PostMapping("/user/{id}/update")
    public String update(@PathVariable Integer id, UserUpdateDTO userUpdateDTO) {
        // 1. 인증 검사
        // sessionUser라는 세션 제작 (로그인 했는지 확인용)
        User sessionUser = (User) session.getAttribute("sessionUser");
        // 유저가 null 이면 로그인 안했다는 말!
        if (sessionUser == null) {
            System.out.println("인증 실패");
            return "redirect:/loginForm"; // 로그인 화면으로 리디렉트
        }
        System.out.println("인증검사 통과");
        // 2. 권한 체크
        // id로 유저를 찾는다.
        User user = userRepository.findById(id);
        // 찾은 id와 세션유저의 id가 같지 않으면 권한 없음
        if (user.getId() != sessionUser.getId()) {
            System.out.println("권한 없음");
            return "redirect:/loginForm"; // 로그인 화면으로 리디렉트
        }
        System.out.println("권한체크 통과");
        // 3. 유효성 검사(부가 로직)
        // 유저이름이 DB에 null 이거나 비어있다면
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return "redirect:/40x"; // 40x 화면으로 리디렉트
        }
        // 패스워드가 DB에서 null 이거나 비어있다면
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        // 4. 핵심 기능
        // 비밀번호 해싱 후 업데이트
        String encPassword = BCrypt.hashpw(userUpdateDTO.getPassword(), BCrypt.gensalt()); // Bcrypt로 해싱
        userUpdateDTO.setPassword(encPassword);
        // 업데이트 진행
        userRepository.update(userUpdateDTO, id);
        return "redirect:/";
    }

    // 로그인 login()
    @PostMapping("/login")
    public String login(LoginDTO loginDTO) {
        // 유효성 검사(부가 로직)
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        /* 핵심 기능 */
        // 유저네임을 찾고 해싱된 패스워드를 비교
        try {
            // User user = userRepository.findByUsernameAndPassword(loginDTO);
            // System.out.println("loginDTO.getPassword() : " + loginDTO.getPassword());
            // System.out.println("user.getPassword() : " + user.getPassword());
            User user = userRepository.findByUsername(loginDTO.getUsername());
            // DB의 저장된 패스워드와 로그인창에서 입력한 패스워드를 해싱한 값이 동일하면 세션 생성
            boolean isValid = BCrypt.checkpw(loginDTO.getPassword(), user.getPassword());
            if (isValid) {
                System.out.println("isValid : " + isValid);
                session.setAttribute("sessionUser", user); // 세션 만들기
                return "redirect:/"; // 홈페이지로 리디렉트
            } else {
                return "redirect:/exLogin"; // 로그인창 리디렉트
            }
        } catch (Exception e) {
            return "redirect:/exLogin";
        }
    }

    // 회원가입 join()
    // 정상인(실무)
    // x-www-form-urlencoded 형식으로 들어오는 데이터 받아서 콘솔로 출력하기.
    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {
        /* 유효성 검사(Validation Check)
        null 입력 값을 제거한다.
        정상 접근은 절대 이 조건문을 못 탄다.
        공격자들이 이 조건문을 타게 됨.(PostMan 사용 같은것)
        따라서 친절하게 리턴 해줄 필요가 없음. 여기선 Error 페이지로 리턴
        */
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }
        // MVC 패턴의 M(Model)
        // DB에 해당 username이 있는지 체크해보기
        // username이 있다? username이 중복됐을때 걸러짐
        User user = userRepository.findByUsername(joinDTO.getUsername());
        if (user != null) {
            return "redirect:/50x";
        }
        /* 핵심기능 */
        // 패스워드 해싱
        String encPassword = BCrypt.hashpw(joinDTO.getPassword(), BCrypt.gensalt());
        joinDTO.setPassword(encPassword);
        // 저장
        userRepository.save(joinDTO);
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
        return "user/joinForm"; // view 파일 호출 user/joinForm 파일 호출
    }

    // Browser URL : IP주소:포트번호/login 입력시 호출
    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm"; // view 파일 호출 user/loginForm 파일 호출
    }

    // Browser URL : IP주소:포트번호/user/updateForm 입력시 호출
    @GetMapping("/user/updateForm")
    public String updateForm(HttpServletRequest request) {
        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // view 파일 호출 user/updateForm 파일 호출
        }
        // 2. 핵심 기능
        User user = userRepository.findById(sessionUser.getId());
        request.setAttribute("user", user);
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
