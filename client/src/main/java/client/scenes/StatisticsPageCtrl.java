package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Tag;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StatisticsPageCtrl {
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
    }
}
