package shop.mtcoding.blog.repository;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Repository
public class BoardRepository {

    @Autowired
    private EntityManager em;

    // Board id를 통한 댓글목록 보기
    public List<BoardDetailDTO> findByIdJoinReply(Integer boardId, Integer sessionUserId) {
        // 1. 쿼리문 작성 및 Reply 클래스 매핑
        String sql = "select ";
        sql += "b.id board_id, ";
        sql += "b.content board_content, ";
        sql += "b.title board_title, ";
        sql += "b.user_id board_user_id, ";
        sql += "r.id reply_id, ";
        sql += "r.comment reply_comment, ";
        sql += "r.user_id reply_user_id, ";
        sql += "ru.username reply_user_username, ";
        if (sessionUserId == null) {
            sql += "false reply_owner ";
        } else {
            sql += "case when r.user_id = :sessionUserId then true else false end reply_owner ";
        }
        sql += "from board_tb b left outer join reply_tb r ";
        sql += "on b.id = r.board_id ";
        sql += "left outer join user_tb ru ";
        sql += "on r.user_id = ru.id ";
        sql += "where b.id = :boardId ";
        sql += "order by r.id desc";
        // 2. 변수 바인딩
        Query query = em.createNativeQuery(sql);
        query.setParameter("boardId", boardId);
        if (sessionUserId != null) {
            query.setParameter("sessionUserId", sessionUserId);
        }
        // QLRM 라이브러리 사용
        JpaResultMapper mapper = new JpaResultMapper();
        List<BoardDetailDTO> dtos = mapper.list(query, BoardDetailDTO.class);
        // 3. 리턴(FK로 여러개를 조회하니 list로!!)
        return dtos;
    }


    // Update, Create, Insert는 @Transacional을 붙여줘야한다.
    @Transactional
    public void deleteById(Integer id) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery("delete from board_tb where id = :id");
        // 2. 변수 바인딩
        query.setParameter("id", id);
        // 3. 전송
        query.executeUpdate();
    }

    // select id, title from board_tb
    // resultClass 안붙이고 직접 파싱하려면?
    // object[] 로 리턴됨
    // object[0] = 1
    // object[1] = 제목1
    public int count() {
        // Entity(Board, User) 타입이 아니라도 기본 자료형도 안됨.
        Query query = em.createNativeQuery("select count(*) from board_tb");
        // 원래는 Object 배열로 리턴 받는다, Object 배열은 컬럼의 연속이다.
        // 그룹함수를 써서, 하나의 컬럼을 조회하려면, Object로 리턴한다.
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public int count(String keyword) {
        // Entity(Board, User) 타입이 아니라도 기본 자료형도 안됨.
        Query query = em.createNativeQuery("select count(*) from board_tb where title like :keyword");
        query.setParameter("keyword", "%" + keyword + "%");
        // 원래는 Object 배열로 리턴 받는다, Object 배열은 컬럼의 연속이다.
        // 그룹함수를 써서, 하나의 컬럼을 조회하려면, Object로 리턴한다.
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    // findAll() 메서드 게시글 목록 조회
    // localhost:8080?page=0
    public List<Board> findAll(int page) {
        final int SIZE = 3;
        // 1. 쿼리문 작성 (페이징 쿼리)
        // limit은 마지막에 들어간다.
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit :page, :size", Board.class);
        // 2. 변수 바인딩
        query.setParameter("page", page * SIZE);
        query.setParameter("size", SIZE);
        // 3. 출력
        return query.getResultList();
    }

    public List<Board> findAll(int page, String keyword) {
        final int SIZE = 3;
        // 1. 쿼리문 작성 (페이징 쿼리)
        // limit은 마지막에 들어간다.
        Query query = em.createNativeQuery("select * from board_tb where title like :keyword order by id desc limit :page, :size", Board.class);
        // 2. 변수 바인딩
        query.setParameter("page", page * SIZE);
        query.setParameter("size", SIZE);
        query.setParameter("keyword", "%" + keyword + "%");
        // 3. 출력
        return query.getResultList();
    }


    // save() 글 저장 메서드
    @Transactional // Update, delete, insert시에 걸어서 사용
    public void save(WriteDTO writeDTO, Integer userId) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery(
                "insert into board_tb(title, content, user_id, created_at) values(:title, :content, :userId, now())");
        // 2. 변수 바인딩
        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);
        // 3. 전송
        query.executeUpdate();
    }

    // Board id를 통한 상세보기
    public Board findById(Integer id) {
        // 1. 쿼리문 작성 및 Board 클래스 매핑
        Query query = em.createNativeQuery("select * from board_tb where id = :id", Board.class);
        // 2. 변수 바인딩
        query.setParameter("id", id);
        // 3. 리턴
        return (Board) query.getSingleResult();
    }

    // Update, Create, Insert는 @Transacional을 붙여줘야한다.
    // Update() 메서드
    @Transactional
    public void update(UpdateDTO updateDTO, Integer id) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery("update board_tb set title = :title, content = :content where id = :id");
        // 2. 변수 바인딩
        query.setParameter("title", updateDTO.getTitle());
        query.setParameter("content", updateDTO.getContent());
        query.setParameter("id", id);
        // 3. 전송
        query.executeUpdate();
    }
}