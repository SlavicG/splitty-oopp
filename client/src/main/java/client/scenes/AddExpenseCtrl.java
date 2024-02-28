package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class AddExpenseCtrl {
    private final MainCtrl mainCtrl;
    @FXML
    public ChoiceBox<String> whoPaid;
    @FXML
    public TextField whatFor;
    @FXML
    public TextField howMuch;

    @Inject
    public AddExpenseCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void onCancel() {
        mainCtrl.overviewPage();
    }

    public void onCreate() {
    }

    public void setUsers(List<String> users) {
        whoPaid.getItems().clear();
        whoPaid.getItems().addAll(users);
    }
}
