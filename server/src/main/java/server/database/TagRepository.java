package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.model.Tag;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("SELECT t FROM Tag t WHERE t.id = :tagId")
    server.model.Tag findByTagId(Integer tagId);
    @Query("SELECT e FROM Tag e WHERE e.event.id = :eventId")
    List<Tag> findAllByEventId(Integer eventId);
}
