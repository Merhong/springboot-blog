package shop.mtcoding.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import shop.mtcoding.blog.repository.ReplyRepository;

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
        // Board 테이블에서 id가 같은 튜플을 조회
        Board board = boardRepository.findById(id);
        // 게시글을 쓴 유저 ID와 세션 ID가 같지 않으면, 다른 사람이란 뜻.
        if (board.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }
        // 3. 핵심 기능
        // update board_tb set title = :title, content = :content where id = :id
        boardRepository.update(updateDTO, id);
        return "redirect:/board/" + id; // update() 메서드 호출후에 갱신된 글로 리다이렉트
    }

    // UpdateForm 회원정보수정 페이지
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
        // 3. 핵심 기능
        // localhost:8080/board/{id}/updateForm
        request.setAttribute("board", board);

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
        // 4. 핵심 기능
        // Model에 접근해서 삭제 "delete from board_tb where id = :id"
        boardRepository.deleteById(id);
        return "redirect:/"; // 삭제후 홈페이지로 리다이렉트
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
        // 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 핵심 기능
        boardRepository.save(writeDTO, sessionUser.getId()); // 저장

        return "redirect:/";
    }

    // 조회
    // 1. 유효성 검사 X
    // 2. 인증검사 X
    // Browser URL : localhost:8080/ or /board 입력시 호출
    // http://localhost:8080?num=4
    @GetMapping({"/", "/board"})
    public String index(
            @RequestParam(defaultValue = "") String keyword, // defaultvalue 없으면 공백시에 null이 들어온다.
            @RequestParam(defaultValue = "0") Integer page,
            HttpServletRequest request) {
        // 1. 유효성 검사 X
        // 2. 인증검사 X

        List<Board> boardList = null;
        int totalCount = 0;
        // 검색어가 없을때 조건
        if (keyword.isBlank()) { // 공백 or 값 없음!
            request.setAttribute("keyword", keyword);
            boardList = boardRepository.findAll(page); // page = 1
            totalCount = boardRepository.count(); // total = 5
        }
        // 검색어가 있을때 조건
        else {
            request.setAttribute("keyword", keyword);
            boardList = boardRepository.findAll(page, keyword); // page = 1
            totalCount = boardRepository.count(keyword); // total = 5
        }

        int totalPage = totalCount / 3; // totalPage = 1
        if (totalCount % 3 > 0) {
            totalPage = totalPage + 1; // totalPage = 2
        }
        boolean last = totalPage - 1 == page;

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
        List<BoardDetailDTO> dtos = null;

        // 동적 쿼리
        // 로그인하지 않은 상태일때 sessionId 제어 안해주면 NullPointException 터짐
        if (sessionUser == null) {
            dtos = boardRepository.findByIdJoinReply(id, null);
        } // 로그인하면 sessionId를 다시 재설정 해줘야 함.
        else {
            dtos = boardRepository.findByIdJoinReply(id, sessionUser.getId());
        }

        boolean pageOwner = false;
        if (sessionUser != null) {
            // System.out.println("테스트 세션 ID : " + sessionUser.getId());
            // System.out.println("테스트 세션 board.getUser().getId() : " + board.getUser().getId());
            pageOwner = sessionUser.getId() == dtos.get(0).getBoardUserId();
        }

        // Request에 담기 -> mustache에서 사용
        request.setAttribute("dtos", dtos);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail"; // V
    }
}
