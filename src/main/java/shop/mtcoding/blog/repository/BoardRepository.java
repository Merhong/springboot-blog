package shop.mtcoding.blog.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.dto.WriteDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class BoardRepository {

    @Autowired
    private EntityManager em;

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