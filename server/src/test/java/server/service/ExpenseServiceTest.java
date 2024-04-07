package server.service;

import commons.dto.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import server.database.ExpenseRepository;
import server.model.Expense;

import server.model.User;


@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private commons.dto.User user1;
    private commons.dto.User user2;
    private LocalDateTime date1;
    private LocalDateTime date2;
    private Event event1;
    private Event event2;
    private Expense expense1;
    private Expense expense2;
    private commons.dto.Expense expense3;


    @BeforeEach
    void setup() {
        LocalDate date = LocalDate.of(2021, 10, 24);
        event1 = new Event(1, "party", null, null, null);
        event2 = new Event(2, "party2", null, null, null);
        expense1 = new Expense(1, 1.0, "description1",
                new User(), date, new server.model.Event(), new ArrayList<>(), null);
        expense2 = new Expense(1, 1.0, "description1",
                new User(), date,new server.model.Event(), new ArrayList<>(),null);
//        expense3 = new Expense(1, 1.0, "description1", 3, date, new ArrayList<>(), 1);


    }

    @Test
    public void getExpensesTest() {
        // Arrange

        List<Expense> mockExpenses = Arrays.asList(expense1);
        when(expenseRepository.findAll()).thenReturn(mockExpenses);

        // Act
        List<commons.dto.Expense> resultExpenses = expenseService.getExpenses();

        // Assert

        commons.dto.Expense expenseDto = resultExpenses.get(0);
        assertEquals(expense1.getId(), expenseDto.getId());
        assertEquals(expense1.getAmount(), expenseDto.getAmount());
        assertEquals(expense1.getDate(), expenseDto.getDate());
        assertEquals(expense1.getDescription(), expenseDto.getDescription());
        assertEquals(expense1.getPayer().getId(), expenseDto.getPayerId());
        assertEquals(expense1.getSplitBetween(), expenseDto.getSplitBetween());
        assertEquals(expense1.getTag().getId(), expenseDto.getTagId());
    }

    @Test
    public void createExpenseTest() {

        LocalDate date = LocalDate.of(2021, 10, 24);

        List<Expense> mockExpenses = Arrays.asList(expense1);
        commons.dto.Expense expenseDto = new commons.dto.Expense(1,15.0,"Food",
                1, date ,new ArrayList<>(),1);




        when(expenseRepository.findAll()).thenReturn(mockExpenses);


//        List<commons.dto.Expense> resultExpenses = expenseService.getExpenseById(e);


    }
}
