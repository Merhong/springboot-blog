package shop.mtcoding.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping({"/", "/board"})
    // page에 입력값이 없으면 0이 디폴트로 들어감 (문자열)
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {

        List<Board> boardList = boardRepository.findAll(page); // page = 1
        int totalCount = boardRepository.count();   // totalCount = 5
        int totalPage = totalCount / 3; // totalPage = 1
        if (totalPage % 3 > 0) {
            totalPage = totalPage + 1;  // totalPage = 2
        }
        boolean lastPage = totalPage - 1 == page;

        System.out.println("테스트 : " + boardList.size());
        System.out.println("테스트 : " + boardList.get(0).getTitle());

        // 페이징 데이터
        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", lastPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);


        // view 파일 호출, index
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
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);

        // view 파일 호출, board/detail
        return "board/detail";
    }
}
