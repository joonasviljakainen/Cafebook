package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {
    
    @Autowired
    AccountRepository accountRepository;

    
    @GetMapping("/index")
    public String helloWorld(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            model.addAttribute("message", auth.getName());
        } else {
            model.addAttribute("message", "World!");
        }
        return "index";
    }
    
    // TODO: jos kirjauduttu sisään, uudelleenohjaa
    @GetMapping("/register")
    public String signUp () {
        return "signup";
    }
    
    @Transactional
    @PostMapping("/register")
    public String createUser(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String profileId) {

        System.out.println("REGISTERING");
        System.out.println("");
        System.out.println("");
        Account acc = new Account();
        acc.setUsername(username);
        acc.setProfileId(profileId);
        
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hash = passwordEncoder.encode(password);
        acc.setPassword(hash);
        
        acc.setLikedImages(new ArrayList<>());
        acc.setFriendRequests(new ArrayList<>());
        acc.setFriends(new ArrayList<>());
        acc.setLikedImages(new ArrayList<>());
        acc.setLikedMessages(new ArrayList<>());
        acc.setMessagesAtMe(new ArrayList<>());
        acc.setMessagesByMe(new ArrayList<>());
        acc.setImageComments(new ArrayList<>());
        
        accountRepository.save(acc);
        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String showLogin() {
        SecurityContextHolder.clearContext();
    return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password) {
        // TODO
        
        return "redirect:/";
    }
}
