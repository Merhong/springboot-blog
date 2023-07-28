package shop.mtcoding.blog.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.dto.joinDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.print.attribute.standard.JobOriginatingUserName;

// 현재 메모리에 로딩되어 있는 것?
// BoardController, UserController, UserRepository
// EntitiyManager, HttpSession
@Repository // 컴포넌트 스캔, 메타 어노테이션
public class UserRepository {

    // IoC 컨테이너에서 들고옴
    @Autowired
    private EntityManager em;

    @Transactional // Update, delete, insert시에 걸어서 사용
    public void save(joinDTO joinDTO) {
        // 1. 쿼리문 작성
        Query query = em.createNativeQuery("insert into user_tb(username, password, email) values(:username, :password, :email)");
        // 2. 변수 바인딩
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        // 3. 전송
        query.executeUpdate();
    }
}