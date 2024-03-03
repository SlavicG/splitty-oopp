package server.database;
import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
