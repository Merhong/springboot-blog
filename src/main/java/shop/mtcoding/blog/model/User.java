package shop.mtcoding.blog.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "user_tb") // 테이블 이름을 붙이기 위해 사용, DB는 대소문자를 구분함.
@Entity // ddl-auto: create일때 테이블 제작
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    private Integer id;

    @Column(nullable = false, length = 20, unique = true) // not null 속성 부여
    private String username;

    @Column(nullable = false, length = 20) // not null 속성 부여
    private String password;

    @Column(nullable = false, length = 20) // not null 속성 부여
    private String email;
}
