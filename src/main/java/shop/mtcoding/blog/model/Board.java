package shop.mtcoding.blog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "board_tb") // 테이블 이름을 붙이기 위해 사용, DB는 대소문자를 구분함.
@Entity // ddl-auto: create일때 테이블 제작
public class Board {

    // id : PK, auto_increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 제목 : not null, <= 100
    @Column(nullable = false, length = 100)
    private String title;

    // 내용 : not null, <= 100
    @Column(nullable = true, length = 10000)
    private String content;

    // 생성된 시간
    private Timestamp createdAt;

    // 이렇게 해도 되는데 ORM 하는게 편하다!!
    // private Integer userId;

    // ORM
    @ManyToOne
    private User user;
}
