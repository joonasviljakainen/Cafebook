/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

/**
 *
 * @author joonas
 */

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account extends AbstractPersistable<Long> {
    
    
    private String username;
    
    private String password;
    
    private String profileId;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;
    
    // Muuta
    @OneToMany
    private List<Image> images;
    
    @ManyToMany
    private List<Account> friends;
    
    @OneToMany
    private List<FriendRequest> friendRequests;
    
    @OneToMany
    private List<Comment> imageComments;
    
    @ManyToMany
    private List<Message> likedMessages;
    
    @ManyToMany
    private List<Image> likedImages;
    
    @OneToMany
    private List<Message> messagesAtMe;
    
    @OneToMany
    private List<Message> messagesByMe;
}
