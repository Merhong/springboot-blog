package shop.mtcoding.blog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "user_tb") // 테이블 이름을 붙이기 위해 사용, DB는 대소문자를 구분함.
@Entity // ddl-auto: create일때 테이블 제작
public class User {

    // id : PK, auto_increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // username : not null, unique, length <= 20
    @Column(nullable = false, unique = true, length = 20)
    private String username;

    // password : not null, unique, length <= 20
    @Column(nullable = false, unique = true, length = 20)
    private String password;

    // email : not null, unique, length <= 20
    @Column(nullable = false, unique = true, length = 20)
    private String email;
}
