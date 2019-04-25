/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author joonas
 */
@Controller
public class ProfileController {
    

    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/profiles")
    public String getProfiles(Model model) {
        List<Account> accs = accountRepository.findAll();
        model.addAttribute("profiles", accs);
        return "profiles";
    }
    
    @GetMapping("profiles/{profileId}")
    public String getProfile(@PathVariable String profileId, Model model) {
        Account acc = accountRepository.findByProfileId(profileId);
        if (acc == null) return "404";
        // KORJAA TÄMÄ; Poista salasanahashi (ehkä oma olio profiilille?)
        model.addAttribute("profile", acc);
        return "profile";
    }
}
