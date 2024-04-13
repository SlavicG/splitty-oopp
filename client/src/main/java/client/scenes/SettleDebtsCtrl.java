package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SettleDebtsCtrl {
    @FXML
    private VBox debtList;
    private Integer eventId;
    private final SettleDebtsLogic logic;
    private final MainCtrl mainCtrl;

    @Inject
    public SettleDebtsCtrl(SettleDebtsLogic logic, MainCtrl mainCtrl) {
        this.logic = logic;
        this.mainCtrl = mainCtrl;
    }

    public void refresh() {
        debtList.getChildren().clear();
        List<SettleDebtsLogic.Debt> debts = logic.getDebts(eventId);
        for (SettleDebtsLogic.Debt debt : debts) {
            addDebt(debt);
        }
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
        refresh();
    }

    public void addDebt(SettleDebtsLogic.Debt debt) {
        URL info = getClass().getResource("DebtInstruction.fxml");
        FXMLLoader infoLoader = new FXMLLoader(info);
        infoLoader.setRoot(debtList);
        try {
            infoLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DebtInstructionCtrl controller = infoLoader.getController();
        controller.initialize(debt, logic);
    }

    public void onBack() {
        mainCtrl.eventPage(eventId);
    }
}
