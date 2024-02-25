package client.scenes;

import com.google.inject.Inject;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddExpenseCtrl {
    private final MainCtrl mainCtrl;
    public ComboBox whoPaid;
    public TextField whatFor;
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
}
