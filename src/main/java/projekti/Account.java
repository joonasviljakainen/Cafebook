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
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account extends AbstractPersistable<Long> {
    
    //@NotEmpty
    private String username;
    //@NotEmpty
    private String password;
    //@NotEmpty
    private String profileId;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;
    
    // Muuta
    @OneToMany
    private List<Image> images;
    // Maybe this should not be looked at like this?
    //private List<FriendRequest> requests;
    
    @ManyToMany
    private List<Account> friends;
    // public List<Likes> likes;
    // public Image profileImage;
}
