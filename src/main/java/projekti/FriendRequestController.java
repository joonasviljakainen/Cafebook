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

/**
 *
 * @author joonas
 */
@Controller
public class FriendRequestController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Secured("USER")
    @GetMapping("/friends")
    public String getOwnFriends(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = accountRepository.findByUsername(auth.getName());
        if (acc != null) {
            List<Account> friends = acc.getFriends();
            model.addAttribute("friends", friends);
            return "friends";
        }
        return "redirect:/login";
    }

    @GetMapping("/friendRequests")
    public String getFriendRequests(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = accountRepository.findByUsername(auth.getName());
        if (acc != null) {
            List<FriendRequest> list = friendRequestRepository.findByMaker(acc);
            model.addAttribute("requestsByMe", list);
            model.addAttribute("requestsAtMe", friendRequestRepository.findByTarget(acc));
            return "requests";
        }
        return "redirect:/login";

    }

    @Secured("USER")
    @PostMapping("/friendRequests")
    public String createFriendRequest(@RequestParam String target) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return "redirect:/login";
        }

        Account makerAcc = accountRepository.findByUsername(auth.getName());
        Account targetAcc = accountRepository.findByProfileId(target);

        if (makerAcc != null && targetAcc != null && !makerAcc.equals(targetAcc)) {
            FriendRequest fr = new FriendRequest();
            fr.setMaker(makerAcc);
            fr.setTarget(targetAcc);

            friendRequestRepository.save(fr);
        }

        return "redirect:/profiles";
    }

    // Weird URL due to PUT not being supported in HTML forms
    @Secured("USER")
    @PostMapping("/friends/requests/{friendRequestId}")
    public String confirmFriendRequest(@PathVariable Long friendRequestId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = accountRepository.findByUsername(auth.getName());
        if (acc != null) {
            FriendRequest f = friendRequestRepository.getOne(friendRequestId);
            Account newFriend = f.getMaker();
            acc.getFriends().add(newFriend);

            accountRepository.save(acc);
            newFriend.getFriends().add(acc);

            accountRepository.save(newFriend);
            friendRequestRepository.delete(f);
        }
        return "redirect:/friendRequests";
    }

    // Deletes a request. Either user can delete, resulting in a cancelled or declined request.
    @Secured("USER")
    @PostMapping("/friends/refusals/{friendRequestId}")
    public String deleteFriendRequest(@PathVariable Long friendRequestId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = accountRepository.findByUsername(auth.getName());
        if (acc != null) {
            FriendRequest req = friendRequestRepository.getOne(friendRequestId);
            if (req.getTarget().equals(acc) || req.getMaker().equals(acc)) {
                friendRequestRepository.delete(req);
            }
        }
        return "redirect:/friendRequests";
    }

    // Removes a friend
    @Secured("USER")
    @PostMapping("/friendships/removals/{profileId}")
    public String removeFriendship(@PathVariable String profileId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = accountRepository.findByUsername(auth.getName());
        if (acc != null) {
            Account toBeRemoved = accountRepository.findByProfileId(profileId);
            if (toBeRemoved != null && acc.getFriends().contains(toBeRemoved)) {
                acc.getFriends().remove(toBeRemoved);
                toBeRemoved.getFriends().remove(acc);
                accountRepository.save(acc);
                accountRepository.save(toBeRemoved);
            }
        }
        return "redirect:/friends";
    }
}
