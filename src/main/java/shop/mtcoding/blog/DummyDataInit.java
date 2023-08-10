package shop.mtcoding.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.repository.UserRepository;

@Component
@RequiredArgsConstructor

public class DummyDataInit {

    // private final UserRepository userRepository;
    //
    // @EventListener(ApplicationReadyEvent.class)
    // @Transactional
    // public void initDB() {
    //     initDummyUser();
    // }
    //
    // public void initDummyUser() {
    //     JoinDTO joinDTO1 = new JoinDTO("ssar", "1234", "ssar@nate.com");
    //     JoinDTO joinDTO2 = new JoinDTO("asdf", "1234", "zxcv@nate.com");
    //
    //     userRepository.save(joinDTO1);
    //     userRepository.save(joinDTO2);
    // }
}