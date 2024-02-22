package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Expense;

public interface ExpenseRepository  extends JpaRepository<Expense, Integer> {

}
