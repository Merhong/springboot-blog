package shop.mtcoding.blog.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import java.util.List;

// IoC 컨테이너에 뜬것들 : Controller 와 Repository, EntityManager, HttpSession
@Repository
public class ReplyRepository {

    @Autowired
    private EntityManager em;

    // Update, Create, Insert는 @Transacional을 붙여줘야한다.
    @Transactional
    public void deleteCommentById(Integer userId) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery("delete from reply_tb where id = :id");
        // 2. 변수 바인딩
        query.setParameter("id", userId);
        // 3. 전송
        query.executeUpdate();
    }

    // Reply id를 통해 튜플찾기
    public Reply findById(Integer id) {
        // 1. 쿼리문 작성 및 Board 클래스 매핑
        Query query = em.createNativeQuery("select * from reply_tb where id = :id", Reply.class);
        // 2. 변수 바인딩
        query.setParameter("id", id);
        // 3. 리턴
        return (Reply) query.getSingleResult();
    }

    // save() 글 저장 메서드
    @Transactional // Update, delete, insert시에 걸어서 사용
    public void save(ReplyWriteDTO replyWriteDTO, Integer userId) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery(
                "insert into reply_tb(comment, board_id, user_id) values(:comment, :boardId, :userId)");
        // 2. 변수 바인딩
        query.setParameter("comment", replyWriteDTO.getComment());
        query.setParameter("boardId", replyWriteDTO.getBoardId());
        query.setParameter("userId", userId);
        // 3. 전송
        query.executeUpdate();
    }
}
