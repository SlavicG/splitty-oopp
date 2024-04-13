package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.util.Locale;

public class DebtInstructionCtrl {
    @FXML
    private VBox infoBox;
    @FXML
    private Button expand;
    @FXML
    private Button collapse;
    @FXML
    private Button sendReminder;
    @FXML
    private Label emailUnknown;
    @FXML
    private Label bankInfo;
    @FXML
    private Label indebted;
    @FXML
    private Label owed;
    @FXML
    private Label amount;
    private SettleDebtsLogic.Debt debt;
    private SettleDebtsLogic logic;

    public void onCollapse() {
        infoBox.setManaged(false);
        infoBox.setVisible(false);
        collapse.setManaged(false);
        collapse.setVisible(false);
        expand.setManaged(true);
        expand.setVisible(true);
    }

    public void onExpand() {
        infoBox.setManaged(true);
        infoBox.setVisible(true);
        collapse.setManaged(true);
        collapse.setVisible(true);
        expand.setManaged(false);
        expand.setVisible(false);
    }

    public void onMarkReceived() {
        logic.markReceived(debt);
    }

    public void onSendReminder() {
        logic.sendReminder(debt);
    }

    public void initialize(SettleDebtsLogic.Debt debt, SettleDebtsLogic logic) {
        this.debt = debt;
        this.logic = logic;
        owed.setText(debt.getOwed().getName());
        amount.setText(NumberFormat.getCurrencyInstance(Locale.FRANCE).format(debt.getAmount()));
        indebted.setText(debt.getIndebted().getName());
        bankInfo.setText(String.format("""
            Bank information:
            Account holder: %s
            IBAN: %s
            BIC: %s""",
            debt.getOwed().getName().isBlank() ? "Unknown" : debt.getOwed().getName(),
            debt.getOwed().getIban().isBlank() ? "Unknown" : debt.getOwed().getIban(),
            debt.getOwed().getBic().isBlank() ? "Unknown" : debt.getOwed().getBic()));

        boolean validEmail = logic.isValidEmail(debt.getOwed().getEmail());
        sendReminder.setDisable(true);
        if (!validEmail) {
            emailUnknown.setText("Email address unknown.");
        } else if (!logic.isEmailConfigured()) {
            emailUnknown.setText("Email is not configured.");
        }
        else {
            emailUnknown.setVisible(false);
            emailUnknown.setManaged(false);
            sendReminder.setDisable(false);
        }

        onCollapse();
    }
}
