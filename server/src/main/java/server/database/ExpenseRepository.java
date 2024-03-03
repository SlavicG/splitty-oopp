package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.model.Expense;

import java.util.List;

public interface ExpenseRepository  extends JpaRepository<Expense, Integer> {
    @Query("SELECT e FROM Expense e WHERE e.event.id = :eventId")
    List<Expense> findAllByEventId(Integer eventId);
}
