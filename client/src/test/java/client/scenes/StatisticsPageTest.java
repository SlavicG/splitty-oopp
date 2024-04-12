package client.scenes;

import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import org.junit.jupiter.api.Test;
import client.utils.ServerUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatisticsPageTest {

    @Test
    public void totalCost() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        StatisticsPageLogic logic = new StatisticsPageLogic(server,mainCtrl);
        Expense expense = new Expense(0,500.0,"fsdfsd",1,LocalDate.now(),null,1);
        Expense expense1 = new Expense(1,500.0,"fsdfsd",1,LocalDate.now(),null,1);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);
        expenses.add(expense1);
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1,"fssdf",3,3,3,0));
        var event = new Event(0, "target", new ArrayList<>(), new ArrayList<>(), tags);
        when(server.getExpenses(0)).thenReturn(expenses);
        double totalCost = logic.TotalCostPerTag(event,1);
        assertEquals(1000,totalCost);
    }

    @Test
    public void NoTagsTotalCost() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        StatisticsPageLogic logic = new StatisticsPageLogic(server,mainCtrl);
        List<Expense> expenses = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1,"fssdf",3,3,3,0));
        var event = new Event(0, "target", new ArrayList<>(), new ArrayList<>(), tags);
        when(server.getExpenses(0)).thenReturn(expenses);
        double totalCost = logic.TotalCostPerTag(event,1);
        assertEquals(0,totalCost);
    }

    @Test
    public void differentTagId() {
        ServerUtils server = mock(ServerUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        StatisticsPageLogic logic = new StatisticsPageLogic(server,mainCtrl);
        Expense expense = new Expense(0,500.0,"fsdfsd",1,LocalDate.now(),null,1);
        Expense expense1 = new Expense(1,500.0,"fsdfsd",1,LocalDate.now(),null,0);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);
        expenses.add(expense1);
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1,"fssdf",3,3,3,0));
        var event = new Event(0, "target", new ArrayList<>(), new ArrayList<>(), tags);
        when(server.getExpenses(0)).thenReturn(expenses);
        double totalCostTag1 = logic.TotalCostPerTag(event,1);
        double totalCostTag0 = logic.TotalCostPerTag(event,0);
        assertEquals(totalCostTag0,totalCostTag1);
    }

    @Test
    public void test() {

    }
}