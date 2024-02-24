package server.service;
import org.springframework.stereotype.Service;
import server.database.ExpenseRepository;
import server.model.Expense;

import java.util.List;
import java.util.function.Function;

@Service
public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private Function<server.model.Expense, Expense> mapper = expense -> new Expense(expense.getId(), expense.getAmount(),expense.getDescription(), expense.getPayer(), expense.getDate(), expense.getEvent());

    protected ExpenseService(ExpenseRepository expenseRepository)
    {
        this.expenseRepository=expenseRepository;
    }
    public List<Expense> getExpenses()
    {
        return expenseRepository.findAll().stream().map(mapper).toList();
    }
    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id).map(mapper).orElse(null);
    }
    public Expense createExpense(Expense expense) {
        server.model.Expense expenseEntity = new server.model.Expense(null, expense.getAmount(),expense.getDescription(), expense.getPayer(), expense.getDate(), expense.getEvent());
        server.model.Expense createdEntity = expenseRepository.save(expenseEntity);
        return new Expense(createdEntity.getId(),expense.getAmount(),expense.getDescription(), expense.getPayer(), expense.getDate(), expense.getEvent());
    }

    public Expense updateExpense(Expense expense) {
        server.model.Expense existingExpense = expenseRepository.findById(expense.getId()).orElse(null);
        if (existingExpense != null) {
            server.model.Expense updatedExpense = new server.model.Expense(existingExpense.getId(),expense.getAmount(),expense.getDescription(), expense.getPayer(), expense.getDate(), expense.getEvent());
            server.model.Expense savedExpense = expenseRepository.save(updatedExpense);
            return mapper.apply(savedExpense);
        }
        return null;
    }





}
