package shop.mtcoding.blog.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    // Model은 request라고 생각하자.
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("username", "ssar");
        return "test";
    }

    @GetMapping("/test2")
    public String test2(Model model) {
        List<String> list = new ArrayList<>();
        list.add("바나나");
        list.add("딸기");

        model.addAttribute("list", list);

        return "array";
    }
}