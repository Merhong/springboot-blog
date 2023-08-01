package shop.mtcoding.blog.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class BoardRepository {

    @Autowired
    private EntityManager em;

    // Board id를 통한 상세보기
    public Board findById(int id) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery("select * from board_tb where id = :id", Board.class);
        // 2. 변수 바인딩
        query.setParameter("id", id);
        // 3. 리턴
        return (Board) query.getSingleResult();
    }


    // 게시글 목록 조회
    // localhost:8080?page=0
    public List<Board> findAll(int page) {
        final int SIZE = 3;
        // 1. 쿼리문 작성 (페이징 쿼리)
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit :page, :size", Board.class);
        // 2. 변수 바인딩
        query.setParameter("page", page * SIZE);
        query.setParameter("size", SIZE);
        // 3. 출력
        return query.getResultList();
    }

    @Transactional // Update, delete, insert시에 걸어서 사용
    public void save(WriteDTO writeDTO, Integer userId) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery("insert into board_tb(title, content, user_id, created_at) values(:title, :content, :userId, now())");
        // 2. 변수 바인딩
        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);
        // 3. 전송
        query.executeUpdate();
    }
}