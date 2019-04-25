/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class FriendRequest extends AbstractPersistable<Long> {
    
    //public Account maker;
    //public Account target;
    //@NotEmpty
    //@NotNull
    public String makerName;
    
    //@NotEmpty
    //NotNull
    public String targetName;
}
