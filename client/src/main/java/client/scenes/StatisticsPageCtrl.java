package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import commons.dto.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StatisticsPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    private Label text;
    @FXML
    private PieChart pieChart;
    private double totalAmount;
    ObservableList<PieChart.Data> pieChartData;
    private HashMap<Tag, Integer> map;
    private ResourceBundle resourceBundle;
    private StatisticsPageLogic logic;
    @FXML
    private ListView<Label> debts;

    @Inject
    public StatisticsPageCtrl(ServerUtils server, MainCtrl mainCtrl, StatisticsPageLogic logic) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.logic = logic;
    }

    public void OverviewPage() {
        mainCtrl.overviewPage();
    }

    public void setEvent(Integer eventId) {
        this.event = server.getEventById(eventId);
    }

    public void clear() {
        text.setText(null);
        pieChart.setData(null);
    }

    public void refresh() {
        totalAmount = 0;
        if (!(this.pieChartData == null)) {
            pieChartData.clear();
        }
        var tags = server.getTags(event.getId());
        var expenses = server.getExpenses(event.getId());
        pieChartData = FXCollections.observableArrayList();
        createData(expenses, tags);
        pieChart.setData(pieChartData);
        text.setText("" + totalAmount);
    }

    private void createData(List<Expense> expenses, List<Tag> tags) {
        for (Tag tag : tags) {
            double amount = logic.TotalCostPerTag(event,tag.getId());
            if (Double.compare(amount, 0) != 0) {
                pieChartData.add(new PieChart.Data(tag.getName() + ": " + amount, amount));
            }
            totalAmount += amount;
        }
        long remainingPercentage = 100;
        String lastName = "";
        for (PieChart.Data data : pieChartData) {
            long percentage = Math.round((data.getPieValue() / totalAmount) * 100);
            remainingPercentage -= percentage;
            lastName = data.getName();
            data.setName(data.getName() + " - " + percentage + "%");
        }

        if (Double.compare(remainingPercentage, 0) != 0 && pieChartData != null && pieChartData.size() >= 1) {
            var changeData = pieChartData.get(pieChartData.size() - 1);
            var percentage = Math.round((changeData.getPieValue() / totalAmount) * 100);
            remainingPercentage += percentage;
            remainingPercentage = 0;
            changeData.setName(lastName + " - " + remainingPercentage + "%");
        }
        pieChart.setData(pieChartData);
        text.setText("" + totalAmount);

        List<Optional<User>> users = server.getUserByEvent(event.getId()).stream().map(Optional::of).toList();
        debts.getItems().clear();
        for (Optional<User> user : users) {
            if (user.isEmpty()) {
                continue;
            }
            Label label = new Label(user.get().getName() + " is owed " + getAmount(user.get().getId()));
            debts.getItems().add(label);
        }
    }

    public Integer getAmount(Integer userId) {
        //TO DO
        return 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}

