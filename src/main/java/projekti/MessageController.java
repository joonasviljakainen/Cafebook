/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
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
    @Autowired
    private MessageCommentRepository messageCommentRepository;

    @Secured("USER")
    @PostMapping("/profiles/{profileId}/messages")
    public String postMessage(@PathVariable String profileId,
            @RequestParam String messageContent) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account sender = accountRepository.findByUsername(username);
        if (sender == null) {
            return "403";
        }
        Account receiver = accountRepository.findByProfileId(profileId);
        if (receiver == null) {
            return "404";
        }

        if (!receiver.getFriends().contains(sender)
                && !receiver.equals(sender)) {
            return "403";
        }

        Message m = new Message();
        m.setMsg(messageContent);
        m.setOwner(sender);
        m.setTarget(receiver);
        m.setCreatedAt(LocalDateTime.now());

        messageRepository.save(m);

        return "redirect:/profiles/" + profileId;
    }
    
    @GetMapping("/profiles/{profileId}/messages/{messageId}")
    public String getMessage(@PathVariable String profileId,
            @PathVariable Long messageId, 
            Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account currentlyAuthenticatedUser = accountRepository.findByUsername(username);
        
        if (currentlyAuthenticatedUser != null) {
            model.addAttribute("currentlyAuthenticatedUser", currentlyAuthenticatedUser);
        }
        
        Message msg = messageRepository.getOne(messageId);
        model.addAttribute("message", msg);
        Account prof = accountRepository.findByProfileId(profileId);
        model.addAttribute("profile", prof);
        model.addAttribute("isFriend", prof.getFriends().contains(currentlyAuthenticatedUser));
        return "message";
    }

    @Secured("USER")
    @PostMapping("/profiles/{profileId}/messages/{messageId}/comments")
    public String postMessageComment(@PathVariable String profileId,
            @PathVariable Long messageId,
            @RequestParam String comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account sender = accountRepository.findByUsername(username);
        if (sender == null) {
            return "403";
        }
        Account receiver = accountRepository.findByProfileId(profileId);
        if (receiver == null) {
            return "404";
        }

        Message msg = messageRepository.getOne(messageId);
        if (msg == null) {
            return "404";
        }

        MessageComment mc = new MessageComment();
        mc.setContent(comment);
        mc.setOwner(sender);
        mc.setMessage(msg);
        mc.setCreatedAt(LocalDateTime.now());

        messageCommentRepository.save(mc);
        msg.getMessageComments().add(mc);
        messageRepository.save(msg);

        return "redirect:/profiles/" + profileId + "/messages/" + messageId;
    }

    @Secured("USER")
    @PostMapping("/profiles/{profileId}/messages/{messageId}/likes")
    public String likeMessage(@PathVariable String profileId,
            @PathVariable Long messageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account sender = accountRepository.findByUsername(username);
        if (sender == null) {
            return "403";
        }
        Account receiver = accountRepository.findByProfileId(profileId);
        if (receiver == null) {
            return "404";
        }

        Message msg = messageRepository.getOne(messageId);
        if (msg == null) {
            return "404";
        }

        if (sender.getLikedMessages().contains(msg)) {
            return "redirect:/profiles/" + profileId;
        }

        sender.getLikedMessages().add(msg);
        msg.getLikers().add(sender);
        accountRepository.save(sender);
        messageRepository.save(msg);

        return "redirect:/profiles/" + profileId + "/messages/" + messageId;
    }

    // TODO: messages by me
}
