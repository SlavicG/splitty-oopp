package client.scenes;

import client.utils.ServerUtils;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import commons.dto.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AddExpenseTest {

    @Test
    void validateNewExpense() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        AddExpenseLogic logic = new AddExpenseLogic(server, mainCtrl);
        // Valid expense
        assertEquals(Optional.empty(), logic.validateNewExpense(
            10.0,
            "test",
            new User(),
            new Tag(),
            LocalDate.now()
        ));
        // Invalid amount
        assertEquals(Optional.of("invalid_expense_amount"), logic.validateNewExpense(
            0.0,
            "test",
            new User(),
            new Tag(),
            LocalDate.now()
        ));
        // No description
        assertEquals(Optional.of("invalid_expense_description"), logic.validateNewExpense(
            10.0,
            "",
            new User(),
            new Tag(),
            LocalDate.now()
        ));
        // Invalid description
        assertEquals(Optional.of("invalid_expense_description"), logic.validateNewExpense(
            10.0,
            null,
            new User(),
            new Tag(),
            LocalDate.now()
        ));
        // Invalid user
        assertEquals(Optional.of("invalid_expense_payer"), logic.validateNewExpense(
            10.0,
            "test",
            null,
            new Tag(),
            LocalDate.now()
        ));
        // Invalid tag
        assertEquals(Optional.of("invalid_tag"), logic.validateNewExpense(
            10.0,
            "test",
            new User(),
            null,
            LocalDate.now()
        ));
        // Invalid time
        assertEquals(Optional.of("invalid_expense_date"), logic.validateNewExpense(
            10.0,
            "test",
            new User(),
            new Tag(),
            null
        ));
    }

    @Test
    void updateExpense() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        AddExpenseLogic logic = new AddExpenseLogic(server, mainCtrl);
        Event event = new Event();
        event.setId(5);
        Expense oldExpense = new Expense();
        Expense newExpense = new Expense();
        // Test expense update
        logic.updateExpense(oldExpense, newExpense, event);
        verify(server).updateExpense(newExpense, 5);
        // Test adding undo function
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(mainCtrl).addUndoFunction(captor.capture());
        // Test undo function
        captor.getValue().run();
        verify(server, times(2)).updateExpense(oldExpense, 5);
    }

    @Test
    void removeExpense() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        AddExpenseLogic logic = new AddExpenseLogic(server, mainCtrl);
        Expense expense = new Expense();
        Event event = new Event();
        event.setId(5);
        // Test remove expense
        logic.removeExpense(expense, event);
        verify(server).deleteExpense(expense, event.getId());
        // Test adding undo function
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(mainCtrl).addUndoFunction(captor.capture());
        // Test undo function
        captor.getValue().run();
        verify(server).addExpense(expense, event.getId());
    }
}