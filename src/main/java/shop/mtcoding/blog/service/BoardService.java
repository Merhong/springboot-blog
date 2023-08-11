package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.repository.UserRepository;

@Service
public class BoardService {

    @Autowired
    private UserRepository userRepository;

    public void 글쓰기(WriteDTO writeDTO, Integer userId) {
        
    }
}
