package server;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.model.Expense;
import server.service.ExpenseService;

import java.util.List;


@Controller
@RequestMapping("/rest/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    ExpenseController(ExpenseService expenseService)
    {
        this.expenseService=expenseService;
    }

    @PostMapping
    @ResponseBody
    public Expense createExpense(@RequestBody Expense expense)
    {
        return expenseService.createExpense(expense);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Expense updateExpense(@PathVariable Integer id, @RequestBody Expense expense) throws BadRequestException
    {
        if(expense.getId()!=id) throw new BadRequestException();
        return expenseService.updateExpense(expense);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Expense getExpense(@PathVariable Integer id )
    {
        return expenseService.getExpenseById(id);
    }
    @GetMapping
    @ResponseBody
    public List<Expense> getExpenses()
    {
        return expenseService.getExpenses();
    }


}
