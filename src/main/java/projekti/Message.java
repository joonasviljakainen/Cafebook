/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author joonas
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message extends AbstractPersistable<Long>{
    
    @ManyToOne
    private Account owner;
    @ManyToOne
    private Account target;
    
    private LocalDateTime createdAt;
    private String msg;
    
    @OneToMany
    private List<MessageComment> messageComments;
    
    @ManyToMany
    private List<Account> likers;
    
}
