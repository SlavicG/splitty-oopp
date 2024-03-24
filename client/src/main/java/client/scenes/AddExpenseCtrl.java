package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public ChoiceBox<User> whoPaid;
    @FXML
    public TextField whatFor;
    @FXML
    public Spinner<Double> howMuch;
    @FXML
    public DatePicker when;
    @FXML
    public Label invalid;
    @FXML
    public Text title;
    @FXML
    public Button create;


    private Event event;
    private Integer expenseId;

    private List<Integer> splitBetweenId;
    private ResourceBundle resourceBundle;

    @Inject
    public AddExpenseCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void onCancel() {
        mainCtrl.eventPage(event.getId());
    }

    public void onCreate() {
        invalid.setVisible(true);
        if (howMuch.getValue() == null || howMuch.getValue() <= 0.0) {
            invalid.setText(resourceBundle.getString("invalid_expense_amount"));
            return;
        }
        if (whatFor.getText() == null || whatFor.getText().isEmpty()) {
            invalid.setText(resourceBundle.getString("invalid_expense_description"));
            return;
        }
        if (whoPaid.getValue() == null) {
            invalid.setText(resourceBundle.getString("invalid_expense_payer"));
            return;
        }
        if (when.getValue() == null) {
            invalid.setText(resourceBundle.getString("invalid_expense_date"));
            return;
        }
        invalid.setVisible(false);

        Expense newExpense = new Expense(
                expenseId, howMuch.getValue(), whatFor.getText(), whoPaid.getValue().getId(), when.getValue(),
                splitBetweenId);

        if (expenseId == null) {
            Expense result = server.addExpense(newExpense, event.getId());
            mainCtrl.addUndoFunction(() -> server.deleteExpense(event.getId(), result.getId()));

        } else {
            int oldExpenseId = expenseId;
            Expense oldExpense = server.getExpenseById(event.getId(), oldExpenseId);
            server.updateExpense(newExpense, event.getId(), oldExpenseId);
            mainCtrl.addUndoFunction(() -> server.updateExpense(oldExpense, event.getId(), oldExpenseId));
        }

        mainCtrl.eventPage(event.getId());
    }

    public void setEvent(Integer eventId) {
        event = server.getEventById(eventId);
        // Eventually, get users for this event specifically
        whoPaid.getItems().clear();
        whoPaid.setConverter(new StringConverter<>() {
            @Override
            public String toString(User user) {
                if (user == null) {
                    return "";
                }
                return user.getName();
            }

            @Override
            public User fromString(String s) {
                return null;
            }
        });
        whoPaid.getItems().addAll(server.getUsers());
    }

    public void setExpenseId(Integer id) {
        expenseId = id;
        title.setText(resourceBundle.getString(id == null ? "new_expense" : "edit_expense"));
        create.setText(resourceBundle.getString(id == null ? "create" : "edit"));
        if (id == null) {
            return;
        }
        Expense expense = server.getExpenseById(event.getId(), expenseId);
        whoPaid.setValue(server.getUserById(expense.getPayerId()));
        whatFor.setText(expense.getDescription());
        howMuch.getValueFactory().setValue(expense.getAmount());
        when.setValue(expense.getDate());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        howMuch.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000.0, 0.0, 1.0));
        howMuch.getValueFactory().setConverter(new StringConverter<>() {
            private final DecimalFormat df = new DecimalFormat("#.##");

            @Override
            public String toString(Double value) {
                // If the specified value is null, return a zero-length String
                if (value == null) {
                    return "";
                }

                return df.format(value);
            }

            @Override
            public Double fromString(String value) {
                try {
                    // If the specified value is null or zero-length, return null
                    if (value == null) {
                        return null;
                    }

                    value = value.trim();

                    if (value.isEmpty()) {
                        return null;
                    }

                    // Perform the requested parsing
                    return df.parse(value).doubleValue();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void clear() {
        whoPaid.setValue(null);
        whatFor.setText(null);
        howMuch.getValueFactory().setValue(0.0);
        when.setValue(null);
        invalid.setVisible(false);
    }
}
