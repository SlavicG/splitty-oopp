package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import commons.dto.User;

import java.time.LocalDate;
import java.util.Optional;

public class AddExpenseLogic {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public AddExpenseLogic(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public Optional<String> validateNewExpense(Double howMuch, String whatFor, User whoPaid, Tag tag, LocalDate when) {
        if (howMuch == null || howMuch <= 0.0) {
            return Optional.of("invalid_expense_amount");
        }
        if (whatFor == null || whatFor.isEmpty()) {
            return Optional.of("invalid_expense_description");
        }
        if (whoPaid == null) {
            return Optional.of("invalid_expense_payer");
        }
        if (tag == null) {
            return Optional.of("invalid_tag");
        }
        if (when == null) {
            return Optional.of("invalid_expense_date");
        }
        return Optional.empty();
    }

    public void updateExpense(Expense expense, Expense newExpense, Event event) throws RuntimeException {
        if (expense == null) {
            Expense result = server.addExpense(newExpense, event.getId());
            mainCtrl.addUndoFunction(() -> server.deleteExpense(result, event.getId()));
        } else {
            server.updateExpense(newExpense, event.getId());
            Expense oldExpense = new Expense(expense);
            mainCtrl.addUndoFunction(() -> server.updateExpense(oldExpense, event.getId()));
        }
    }

    public void removeExpense(Expense expense, Event event) {
        assert(expense != null);
        server.deleteExpense(expense, event.getId());
        Expense oldExpense = new Expense(expense);
        mainCtrl.addUndoFunction(() -> server.addExpense(oldExpense, event.getId()));
        mainCtrl.eventPage(event.getId());
    }
}
