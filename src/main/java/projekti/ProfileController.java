/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author joonas
 */
@Controller
public class ProfileController {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @GetMapping("/profiles")
    public String getProfiles(Model model) {
        List<Account> accs = accountRepository.findAll();
        model.addAttribute("profiles", accs);
        return "profiles";
    }

    @GetMapping("/profiles/{profileId}")
    public String getProfile(@PathVariable String profileId, Model model) {
        Account acc = accountRepository.findByProfileId(profileId);
        if (acc == null) {
            return "404";
        }
        
        model.addAttribute("profile", acc);
        model.addAttribute("messages", messageRepository.findByTarget(acc));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account currentlyLoggedInUser = accountRepository.findByUsername(username);
        model.addAttribute("username", auth.getName());
        if(currentlyLoggedInUser != null 
                && (currentlyLoggedInUser.getFriends().contains(acc) 
                || currentlyLoggedInUser.equals(acc))) {
            model.addAttribute("isFriend", true);
        }
        return "profile";
    }
    
    @GetMapping("/profiles/{profileId}/gallery")
    public String getGallery(@PathVariable String profileId, Model model) {
        Account acc = accountRepository.findByProfileId(profileId);
        if (acc == null) return "404";
        model.addAttribute("images", imageRepository.findByOwner(acc));
        model.addAttribute("profile", acc);
        
        return "gallery";
    }

    @GetMapping("/profiles/{profileId}/profilepicture")
    @ResponseBody
    public byte[] getProfilePicture(@PathVariable String profileId) {
        Account acc = accountRepository.findByProfileId(profileId);
        List<Image> list = imageRepository.findByOwner(acc);
        
        for (Image i : list) {
            if (i.getIsProfilePicture() != null && i.getIsProfilePicture() == true) {
                return i.getBytes();
            }
        }
        //TODO: default picture
        return null;
    }
    
    @Secured("USER")
    @PostMapping("/profiles/{profileId}/profilepicture")
    public String setProfilePicture(@PathVariable String profileId,
            @RequestParam Long imageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account acc = accountRepository.findByProfileId(profileId);
        
        if (acc.getUsername().equals(username)) {
            Image newProfilePic = imageRepository.getOne(imageId);
            if (newProfilePic.getOwner().equals(acc)) {
                Image cur;
                List<Image> imgs = imageRepository.findByOwner(acc);
                for (Image i: imgs) {
                    if (i.getIsProfilePicture() == true) {
                        cur = i;
                        cur.setIsProfilePicture(false);
                        imageRepository.save(cur);
                        break;
                    }
                }
                newProfilePic.setIsProfilePicture(true);
                imageRepository.save(newProfilePic);
            }
        }
        return "redirect:/profiles/" + profileId;
    }

}
