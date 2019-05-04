/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.tools.FileObject;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author joonas
 */
@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired CommentRepository commentRepository;

    @GetMapping("/images")
    public String getImageAdder(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = accountRepository.findByUsername(auth.getName());
        List<Image> l = imageRepository.findByOwner(acc);

        model.addAttribute("images", l);
        model.addAttribute("length", l.size());
        model.addAttribute("username", auth.getName());
        return "addimage";
    }

    @GetMapping(path = "/images/{id}", produces = "image/jpeg")
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        return imageRepository.getOne(id).getBytes();
    }
    
    @GetMapping(path = "/views/images/{id}")
    public String showImage(@PathVariable Long id, Model model) {
        
        Image img = imageRepository.getOne(id);
        if (img == null) return "404";
        model.addAttribute("image", img);
        model.addAttribute("comments", commentRepository.findByImage(img));
        return "image";
    }

    @PostMapping(path = "/images/{id}/comments")
    public String createImageComment(@PathVariable Long id, 
            @RequestParam String comment) {
        // TODO
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = accountRepository.findByUsername(auth.getName());
                System.out.println("");

        System.out.println(auth.getName());
        System.out.println("");
        Image cur = imageRepository.getOne(id);
        System.out.println("IMAGE + " + cur.getDescription());
        if (cur != null && acc != null) {
            Comment c = new Comment();
            c.setText(comment);
            c.setCommentTime(new Date(System.currentTimeMillis()));
            c.setImage(cur);
            c.setCommenter(acc);
            cur.getComments().add(c);
            
            commentRepository.save(c);
            imageRepository.save(cur);
            
            return "redirect:/views/images/" + id;
        }

        return "redirect:/login";
    }

    @Secured("USER")
    @PostMapping("/images")
    public String saveImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("description") String description) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account acc = accountRepository.findByUsername(username);

        if (acc.getImages().size() > 9) {
            return "Too many images!";
        }

        Image i = new Image();
        i.setBytes(image.getBytes());
        i.setOwner(acc);
        i.setIsProfilePicture(false);
        Date d = new Date(System.currentTimeMillis());
        i.setCreationDate(d);
        if (description != null && !description.isEmpty()) {
            i.setDescription(description);
        }
        imageRepository.save(i);
        return "redirect:/";
    }
}
