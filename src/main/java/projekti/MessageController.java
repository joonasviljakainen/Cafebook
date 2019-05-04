/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author joonas
 */
@Controller
public class MessageController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MessageRepository messageRepository;
    
    
    
    @PostMapping("/profiles/{profileId}/messages")
    public String postMessage(@PathVariable String profileId,
            @RequestParam String messageContent) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Account sender = accountRepository.findByUsername(username);
        if (sender == null) return "403";
        Account receiver = accountRepository.findByProfileId(profileId);
        if (receiver == null) return "404";
        
        if(!receiver.getFriends().contains(receiver) 
                && !receiver.equals(sender)) return "403";
        
        Message m = new Message();
        m.setMsg(messageContent);
        m.setOwner(sender);
        m.setTarget(receiver);
        m.setCreatedAt(new Date(System.currentTimeMillis()));
        
        messageRepository.save(m);
        
        return "redirect:/profiles/" + profileId;
    }
    
    // TODO: messages by me
}
