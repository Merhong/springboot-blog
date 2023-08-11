package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
}
