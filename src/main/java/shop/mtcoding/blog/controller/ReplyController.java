package shop.mtcoding.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

import javax.servlet.http.HttpSession;

@Controller
public class ReplyController {
    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyRepository replyRepository;

    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable Integer id) { // replyId
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
        Reply reply = replyRepository.findById(id);
        // 게시글을 쓴 유저 ID와 세션 ID가 같지 않으면, 다른 사람이란 뜻.
        if (reply.getUser().getId() != sessionUser.getId()) {
            return "redirect:/40x";
        }
        // 4. Model에 접근해서 삭제 "delete from reply_tb where id = :id"
        // BoardRepository.deleteById(id) 호출 -> 리턴 X (void)
        replyRepository.deleteCommentById(id);
        // 삭제후 홈페이지로 리다이렉트
        return "redirect:/";
    }

    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO replyWriteDTO) {
        // comment 유효성 검사
        if (replyWriteDTO.getBoardId() == null) {
            return "redirect:/40x";
        }
        // 댓글내용 빈칸이면 그 페이지 다시 리로드
        if (replyWriteDTO.getComment() == null || replyWriteDTO.getComment().isEmpty()) {
            return "redirect:/board/" + replyWriteDTO.getBoardId();
        }

        // 인증 검사 -> boardcontroller에서 참고
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 댓글 쓰기
        replyRepository.save(replyWriteDTO, sessionUser.getId());

        return "redirect:/board/" + replyWriteDTO.getBoardId();
    }

}