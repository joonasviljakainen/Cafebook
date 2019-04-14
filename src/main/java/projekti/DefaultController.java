package projekti;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {

    @GetMapping("*")
    public String helloWorld(Model model) {
        model.addAttribute("message", "World!");
        return "index";
    }
    
    // TODO: jos kirjauduttu sisään, uudelleenohjaa
    @GetMapping("/register")
    public String signUp () {
        return "signup";
    }
    
    @PostMapping("/register")
    public String createUser(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String profileId) {
        return "redirect:/";
    }
}
