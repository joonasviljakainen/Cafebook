/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author joonas
 */
public interface AccountRepository  extends JpaRepository<Account, Long>{
    @EntityGraph(attributePaths = {"messagesAtMe"})
    Account findByUsername(String username);
    @EntityGraph(attributePaths = {"messagesAtMe"})
    Account findByProfileId(String profileId);
}
