package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;

import java.util.List;

public class StatisticsPageLogic {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public StatisticsPageLogic(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public int TotalCostPerTag(Event event,int tagId) {
        List<Expense> expenses = server.getExpenses(event.getId()).stream()
                .filter(expense -> !expense.getDescription().equals("Settle Debts")).toList();
        var expensesPerTag = expenses.stream().filter(d -> d.getTagId() == tagId).toList();
        if (!expensesPerTag.isEmpty()) {
            int totalCostPerTag = (int) expensesPerTag.stream().mapToDouble(Expense::getAmount).sum();
            return totalCostPerTag;
        } else {
            return 0;
        }
    }
}
