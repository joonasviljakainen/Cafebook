/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author joonas
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Account owner;
    private String description;
    private LocalDateTime creationDateTime;
    
    private Boolean isProfilePicture;
    
    @OneToMany
    private List<Comment> comments;
    
    @ManyToMany
    private List<Account> likers;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    //@Type(type="org.hibernate.type.BinaryType")
    private byte[] bytes;
}
