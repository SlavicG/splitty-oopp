package server.api;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import commons.dto.Expense;
import server.service.ExpenseService;

import java.util.List;


@Controller
@RequestMapping("/rest/events/{event_id}/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @ResponseBody
    public Expense createExpense(@PathVariable(name = "event_id") Integer eventId, @RequestBody Expense expense) {
        return expenseService.createExpense(eventId, expense);
    }

    @PutMapping("/{expense_id}")
    @ResponseBody
    public Expense updateExpense(@PathVariable(name = "event_id") Integer eventId, @PathVariable(name = "expense_id") Integer expenseId, @RequestBody Expense expense) throws BadRequestException {
        if (expense.getId() != expenseId) throw new BadRequestException();
        return expenseService.updateExpense(eventId, expense);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Expense getExpense(@PathVariable Integer id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping
    @ResponseBody
    public List<Expense> getExpenses(@PathVariable(name = "event_id") Integer eventId) {
        return expenseService.getExpenses(eventId);
    }

    @DeleteMapping("/{expense_id}")
    @ResponseBody
    public Expense deleteExpense(@PathVariable(name = "event_id") Integer eventId, @PathVariable(name = "expense_id") Integer expenseId) {
        return expenseService.deleteExpense(eventId, expenseId);
    }

}
