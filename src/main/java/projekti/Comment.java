/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author joonas
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Comment extends AbstractPersistable<Long> {
    
    private String text;
    private Date commentTime;
    
    @ManyToOne
    private Account commenter;
    
    @ManyToOne
    private Image image;
    
}
