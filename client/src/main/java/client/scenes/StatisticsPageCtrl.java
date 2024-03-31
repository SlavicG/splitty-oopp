package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class StatisticsPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    private Label text;
    @FXML
    private PieChart pieChart;
    private int totalCost;
    private HashMap<Tag,Integer> map;
    private ResourceBundle resourceBundle;
    @Inject
    public StatisticsPageCtrl(ServerUtils server, MainCtrl mainCtrl, HashMap<Tag,Integer> map) {
        this.map = new HashMap<>();
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void OverviewPage(){
        mainCtrl.overviewPage();
    }
    public void setEvent(Integer eventId) {
        this.event = server.getEventById(eventId);
    }

    public void clear() {
        text.setText(null);
        pieChart.setData(null);
    }

    public int totalCost() {
        List<Expense> expenses = server.getExpenses(event.getId());
        if(expenses != null) {
            totalCost = (int) expenses.stream().mapToDouble(Expense::getAmount).sum();
            return totalCost;
        }
        else {
            return 0;
        }
    }

    public void setTotalCost(double totalCost) {
        text.setText("$" + totalCost);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public int totalExpensePerTag(int tagId) {
        List<Expense> expenses = server.getExpenses(event.getId());
        var expensesPerTag = expenses.stream().filter(d -> d.getTagId()==tagId).toList();
        if(!expensesPerTag.isEmpty()) {
            int totalCostPerTag =(int) expensesPerTag.stream().mapToDouble(Expense::getAmount).sum();
            return totalCostPerTag;
        }
        else {
            return 0;
        }
    }

    public void mapTagToTotalCostPerTag() {
        List<Tag> tags = server.getTags(event.getId());
        for(Tag tag: tags) {
            map.put(tag, totalExpensePerTag(tag.getId()));
        }
    }

    public void CreatePieChart(){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(Map.Entry<Tag,Integer> entry: map.entrySet()){
            PieChart.Data data = new PieChart.Data(entry.getKey().getName(),entry.getValue());
            pieChartData.add(data);
        }

        pieChart.setData(pieChartData);
        pieChart.setTitle("TotalCost per Tag");
        pieChart.getData().forEach(data -> {
            String percentage = String.format("%.2f%%",((data.getPieValue()/totalCost())*100));
            Tooltip tooltip = new Tooltip(percentage);
            Tooltip.install(data.getNode(), tooltip);
        });
    }

    public void pieChartColors() {
        ArrayList<Color> colors = new ArrayList<>();
        for(Map.Entry<Tag,Integer> entry: map.entrySet()) {
            colors.add(entry.getKey().getColor());
        }
        int i = 0;
        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().setStyle("-fx-pie-color: " + colors.get(i % colors.size()) + ";");
            i++;
        }
    }
}

