/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author joonas
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @EntityGraph(attributePaths={"messageComments","owner"})
    List<Message> findByTarget(Account target);
    @EntityGraph(attributePaths={"messageComments","owner"})
    List<Message> findByTarget(Account target, Pageable pageable);
    @EntityGraph(attributePaths={"messageComments","owner"})
    List<Message> findByOwner(Account owner);
    @EntityGraph(attributePaths={"messageComments","owner"})
    List<Message> findByOwner(Account owner, Pageable pageable);
}
