package shop.mtcoding.blog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// User(1) : Reply(N)
// Board(1) : Reply(N)
@Getter
@Setter
@Table(name = "reply_tb")
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // not null, <= 100
    @Column(nullable = false, length = 100)
    private String comment; // 댓글 내용

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user; // FK(user_id)

    @ManyToOne
    private Board board; // FK(board_id)
}