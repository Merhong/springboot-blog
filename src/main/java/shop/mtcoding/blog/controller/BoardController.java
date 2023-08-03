package shop.mtcoding.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller 어노테이션
 * 1. 컴포넌트 스캔
 * 2. 리턴되는 값 : view 파일
 */
@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    // Update 기능 구현
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, UpdateDTO updateDTO) {
        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 2. 권한 체크
        // Board를 조회해서 가방(request)에 담는다
        Board board = boardRepository.findById(id);
        // 게시글을 쓴 유저 ID와 세션 ID가 같지 않으면, 다른 사람이란 뜻.
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }
        // 3. 핵심 기능
        // update board_tb set title = :title, content = :content where id = :id
        boardRepository.update(updateDTO, id);
        // update() 메서드 호출후에 갱신된 글로 리다이렉트
        return "redirect:/board/" + id;
    }

    // UpdateForm
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/40x";
        }
        // 2. 권한 체크
        // Board를 조회해서 가방(request)에 담는다
        Board board = boardRepository.findById(id);
        // 게시글을 쓴 유저 ID와 세션 ID가 같지 않으면, 다른 사람이란 뜻.
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }
        request.setAttribute("board", board);
        // 3. 핵심 기능
        // localhost:8080/board/{id}/updateForm

        return "/board/updateForm";
    }

    // Delete(삭제) 메서드
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) {
        // 1. PathVariable 값 받기
        // 2. 인증 검사
        // session에 접근해서 sessionUser 키 값을 가져 와서
        // null이면 로그인 페이지로 보내고
        // null 아니면, 3번을 실행!
        User sessionUser = (User) session.getAttribute("sessionUser");
        // 로그인 안했으면 로그인 화면으로 리다이렉트
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 3. 권한 검사
        Board board = boardRepository.findById(id);
        // 게시글을 쓴 유저 ID와 세션 ID가 같지 않으면, 다른 사람이란 뜻.
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }
        // 4. Model에 접근해서 삭제 "delete from board_tb where id = :id"
        // BoardRepository.deleteById(id) 호출 -> 리턴 X (void)
        boardRepository.deleteById(id);
        // 삭제후 홈페이지로 리다이렉트
        return "redirect:/";
    }

    // Save(저장) 메서드
    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {
        // 유효성 검사(부가 로직)
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {
            return "redirect:/40x";
        }
        if (writeDTO.getContent() == null || writeDTO.getContent().isEmpty()) {
            return "redirect:/40x";
        }
        // 핵심 기능 : 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        boardRepository.save(writeDTO, sessionUser.getId());

        return "redirect:/";
    }

    // 조회
    // 1. 유효성 검사 X
    // 2. 인증검사 X
    // Browser URL : localhost:8080/ or /board 입력시 호출
    // http://localhost:8080?num=4
    @GetMapping({"/", "/board"})
    public String index(
            @RequestParam(defaultValue = "0") Integer page,
            HttpServletRequest request) {
        // 1. 유효성 검사 X
        // 2. 인증검사 X

        List<Board> boardList = boardRepository.findAll(page); // page = 1
        int totalCount = boardRepository.count(); // totalCount = 5

        System.out.println("테스트 : totalCount :" + totalCount);
        int totalPage = totalCount / 3; // totalPage = 1
        if (totalCount % 3 > 0) {
            totalPage = totalPage + 1; // totalPage = 2
        }
        boolean last = totalPage - 1 == page;

        System.out.println("테스트 :" + boardList.size());
        System.out.println("테스트 :" + boardList.get(0).getTitle());

        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);

        return "index";
    }

    // Browser URL : IP주소:포트번호/board/saveForm 입력시 호출
    @GetMapping({"/board/saveForm"})
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser"); // 값이 있으면 로그인됨
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // view 파일 호출, board/saveForm
        return "board/saveForm";
    }

    // Browser URL : IP주소:포트번호/board/board/id 입력시 호출
    // localhost:8080/board/1
    // localhost:8080/board/2
    // MVC 패턴
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {    // Controller
        User sessionUser = (User) session.getAttribute("sessionUser"); // 세션접근
        Board board = boardRepository.findById(id); // M

        boolean pageOwner = false;
        if (sessionUser != null) {
            System.out.println("테스트 세션 ID : " + sessionUser.getId());
            System.out.println("테스트 세션 board.getUser().getId() : " + board.getUser().getId());
            pageOwner = sessionUser.getId() == board.getUser().getId();
            System.out.println("테스트 : pageOwner : " + pageOwner);
        }

        request.setAttribute("board", board);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail"; // V
    }
}
