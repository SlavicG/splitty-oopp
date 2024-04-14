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
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;
import server.database.UserRepository;
import commons.dto.Expense;

import server.model.Tag;
import server.model.User;


@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TagRepository tagRepository;

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
//

//    private java.util.function.Function<Expense, commons.dto.Expense> mapper = expense -> new commons.dto.Expense(
//            expense.getId(),
//            expense.getAmount(),
//            expense.getDescription(),
//            expense.getPayer().getId(),
//            expense.getDate(),
//            expense.getSplitBetween(),
//            expense.getTag().getId());



    @BeforeEach
    void setup() {
        LocalDate date = LocalDate.of(2021, 10, 24);
        event1 = new Event(1, "party", null, null, null);
        event2 = new Event(2, "party2", null, null, null);
//        expense1 = new Expense(1, 1.0, "description1",
//                new User(), date, new server.model.Event(), new ArrayList<>(), new Tag());
//        expense2 = new Expense(1, 1.0, "description1",
//                new User(), date,new server.model.Event(), new ArrayList<>(),new Tag());
//        expense3 = new Expense(1, 1.0, "description1", 3, date, new ArrayList<>(), 1);

        User payer = new User(1,"Slavic","S@gmail.com","123","777");
        server.model.Event event = new server.model.Event(1,"birthday",new ArrayList<>(),new ArrayList<>(),
                null);

        Tag tag = new Tag(1, "Food",1,2,8,new server.model.Event());

        when(userRepository.findById(1)).thenReturn(Optional.of(payer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));

    }





    @Test
    public void testCreateExpense() {
        // Mock dependencies setup
        User payer = new User(1, "Slavic", "S@gmail.com", "123", "777");
        server.model.Event event = new server.model.Event(1, "birthday", new ArrayList<>(), new ArrayList<>(), null);
        Tag tag = new Tag(1, "Food", 1, 2, 8, event);
        LocalDate date = LocalDate.of(2021, 10, 24);

        Expense inputExpense = new Expense(null, 5.0, "Birthday", 1, date, Arrays.asList(1), 1);
        server.model.Expense mappedExpense = new server.model.Expense(1, 5.0, "Birthday",
                payer, date, event, Arrays.asList(1), tag);

        when(expenseRepository.save(any(server.model.Expense.class))).thenReturn(mappedExpense);


        when(userRepository.findById(1)).thenReturn(Optional.of(payer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));

        Expense createdExpense = expenseService.createExpense(1, inputExpense);

        assertEquals(1, createdExpense.getId());
        assertEquals(5.0, createdExpense.getAmount());
        assertEquals("Birthday", createdExpense.getDescription());
        assertEquals(1, createdExpense.getPayerId());

        verify(expenseRepository).save(any(server.model.Expense.class));
        verify(eventRepository).findById(1);
        verify(userRepository).findById(1);
        verify(tagRepository).findById(1);
    }


}
