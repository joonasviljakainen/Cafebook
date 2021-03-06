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
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    @EntityGraph(attributePaths={"commenter"})
    public List<Comment> findByImage(Image image);
    @EntityGraph(attributePaths={"commenter"})
    public List<Comment> findByImage(Image image, Pageable pageable);
    
}
 