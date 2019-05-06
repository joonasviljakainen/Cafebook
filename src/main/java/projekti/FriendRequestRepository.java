/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author joonas
 */
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
    @EntityGraph(attributePaths={"target"})
    public List<FriendRequest> findByMaker(Account maker);
    @EntityGraph(attributePaths={"maker"})
    public List<FriendRequest> findByTarget(Account target);
}
