package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import commons.dto.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final AddExpenseLogic logic;
    private final HashMap<Integer, String> userNamesCache;
    @FXML
    public ChoiceBox<User> whoPaid;
    @FXML
    public ChoiceBox<Tag> tag;
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
    @FXML
    public Button remove;
    @FXML
    public CheckBox everybody;
    @FXML
    public CheckBox not_everybody;
    @FXML
    public FlowPane participants;


    private Event event;
    private Expense expense;

    private List<Integer> splitBetweenId;
    @FXML
    private ListView<CheckBox> menu;
    private ResourceBundle resourceBundle;

    @Inject
    public AddExpenseCtrl(MainCtrl mainCtrl, ServerUtils server, AddExpenseLogic logic) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.logic = logic;
        userNamesCache = new HashMap<>();
    }

    public void onCancel() {
        mainCtrl.eventPage(event.getId());
    }

    public void onCreate() {
        // Validate data entries
        Optional<String> validation = logic.validateNewExpense(howMuch.getValue(), whatFor.getText(),
                whoPaid.getValue(), tag.getValue(), when.getValue());
        if (validation.isEmpty()) {
            invalid.setVisible(false);
        }
        else {
            invalid.setVisible(true);
            invalid.setText(resourceBundle.getString(validation.get()));
            return;
        }

        splitBetweenId = new ArrayList<>();
        if (everybody.isSelected()) {
            splitBetweenId = event.getUserIds();
        }
        else {
            List<Optional<User>> users = server.getUserByEvent(event.getId()).stream().map(Optional::of).toList();
            for (CheckBox c: menu.getItems()) {
                if (c.isSelected()) {
                    String name = c.getText();
                    for (Optional<User> u: users) {
                        if (u.get().getName().equals(name)) splitBetweenId.add(u.get().getId());
                    }
                }
            }
        }

        Expense newExpense = new Expense(
            expense == null ? null : expense.getId(), howMuch.getValue(), whatFor.getText(), whoPaid.getValue().getId(),
                when.getValue(), splitBetweenId, tag.getValue().getId());

        try {
            logic.updateExpense(expense, newExpense, event);
        }
        catch (RuntimeException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could not undo edit operation.");
            alert.setHeaderText("Undo of expense edit not possible anymore.");
            alert.setContentText(
                "Undoing an expense edit is no longer possible after previously deleting that expense.");
            alert.show();
        }

        mainCtrl.eventPage(event.getId());
    }

    public void onRemove() {
        logic.removeExpense(expense, event);
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
        whoPaid.getItems().addAll(server.getUserByEvent(eventId));
        tag.getItems().clear();
        tag.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                if (tag == null) {
                    return "";
                }
                return tag.getName();
            }

            @Override
            public Tag fromString(String s) {
                return null;
            }
        });
        tag.getItems().addAll(server.getTags(eventId));
        List<Optional<User>> users = server.getUserByEvent(event.getId()).stream().map(Optional::of).toList();
        menu.getItems().clear();
        for (Optional<User> user : users) {
            if (user.isEmpty()) {
                continue;
            }
            CheckBox checkBox = new CheckBox(user.get().getName());
            menu.getItems().add(checkBox);
        }
    }

    public void setExpenseId(Integer id) {
        title.setText(resourceBundle.getString(id == null ? "new_expense" : "edit_expense"));
        create.setText(resourceBundle.getString(id == null ? "create" : "edit"));
        remove.setVisible(id != null);
        if (id == null) {
            expense = null;
            return;
        }
        expense = server.getExpenseById(event.getId(), id);
        whoPaid.setValue(server.getUserById(event.getId(), expense.getPayerId()));
        whatFor.setText(expense.getDescription());
        howMuch.getValueFactory().setValue(expense.getAmount());
        when.setValue(expense.getDate());
        tag.setValue(server.getTagById(event.getId(), expense.getTagId()));
        if (expense.getSplitBetween().size() == event.getUserIds().size()) {
            everybody.setSelected(true);
            not_everybody.setSelected(false);
            //splitOptions.forEach(x -> x.setSelected(true));

        }
        else {
            not_everybody.setSelected(true);
            everybody.setSelected(false);
//            for (CheckBox c: splitOptions) {
//                for (Integer u: expense.getSplitBetween()) {
//                    if (server.getUserById(event.getId(), u).getName().equals(c.getText()))
//                        c.setSelected(true);
//                    else c.setSelected(false);
//                }
//            }
        }
        List<Optional<User>> users = server.getUserByEvent(event.getId()).stream().map(Optional::of).toList();
        menu.getItems().clear();
        for (Optional<User> user : users) {
            if (user.isEmpty()) {
                continue;
            }
            CheckBox checkBox = new CheckBox(user.get().getName());
            checkBox.setBackground(null);
            if (expense.getSplitBetween().contains(user.get().getId()))
                checkBox.setSelected(true);
            else checkBox.setSelected(false);
            Platform.runLater(() -> menu.getItems().add(checkBox));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        howMuch.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000.0, 0.0, 1.0));
        howMuch.getValueFactory().setConverter(new StringConverter<>() {
            private final NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.FRANCE);

            @Override
            public String toString(Double value) {
                // If the specified value is null, return a zero-length String
                if (value == null) {
                    return "";
                }

                return nf.format(value);
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
                    return nf.parse(value).doubleValue();
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
        tag.setValue(null);
        invalid.setVisible(false);
    }

    public void handleEverybody() {
        not_everybody.setSelected(false);
        menu.getItems().forEach(x -> x.setSelected(true));
    }

    public void handleNotEverybody() {
        everybody.setSelected(false);
    }
}
