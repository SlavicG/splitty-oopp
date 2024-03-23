package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OverviewPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final HashMap<Integer, String> userNamesCache;
    @FXML
    private Label eventName;
    @FXML
    private HBox participants;
    @FXML
    private ChoiceBox<Optional<User>> userFilter;
    @FXML
    private TextField searchBox;
    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String> descriptionColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;
    @FXML
    private TableColumn<Expense, String> payerColumn;
    private Integer eventId;
    private ObservableList<User> data;
    private FilteredList<Expense> expenses;
    private ResourceBundle resources;

    @Inject
    public OverviewPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        userNamesCache = new HashMap<>();
        data = FXCollections.observableArrayList();
    }

    public void startPage() {
        mainCtrl.startPage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        payerColumn.setCellValueFactory(expense -> new ReadOnlyObjectWrapper<>(
                userNamesCache.get(expense.getValue().getPayerId())));

        expenseTable.setPlaceholder(new Label(resources.getString("add_expense_hint")));

        // Listen for new users received through WebSocket
        server.registerForMessages("/topic/users", User.class, this::handleNewUser);

        // Listen for changes in the user filter and search box
        userFilter.setOnAction(actionEvent -> refreshFilter());
        searchBox.textProperty().addListener(((observableValue, s, t1) -> refreshFilter()));
    }

    private void handleNewUser(User newUser) {
        Platform.runLater(() -> {
            refresh();
        });
    }

    public void setEventId(Integer id) {
        eventId = id;
        refresh();
    }

    public void invitation() {
        mainCtrl.invitationPage(eventName, eventId);
    }

    public void addParticipant() {
        mainCtrl.addParticipantPage(eventId, null);
    }

    public void editParticipant(User user) {
        mainCtrl.addParticipantPage(eventId, user.getId());
    }

    public void addExpense() {
        mainCtrl.addExpensePage(eventId, null);
    }

    public void statisticsPage() {
        mainCtrl.statisticsPage();
    }

    public void refresh() {
        // Set up expenses table.
        expenseTable.getSelectionModel().clearSelection();
        Event event = server.getEventById(eventId);
        expenses = new FilteredList<>(FXCollections.observableList(server.getExpenses(eventId)));
        expenseTable.setItems(expenses);
        eventName.setText(event.getTitle());

        // Populate the userFilter ChoiceBox with all users that have paid for expense.
        List<Optional<User>> users = event.getUserIds().stream()
                .distinct().map(server::getUserById).map(Optional::of).toList();
        // This cache is here, so we don't have to fetch usernames for every expense.
        userNamesCache.clear();
        users.forEach(user -> {
            if (user.isEmpty()) {
                return;
            }
            userNamesCache.put(user.get().getId(), user.get().getName());
        });
        userFilter.getItems().clear();
        userFilter.setConverter(new StringConverter<>() {
            @Override
            public String toString(Optional<User> user) {
                if (user == null || user.isEmpty()) {
                    return resources.getString("everyone");
                }
                return user.get().getName();
            }

            @Override
            public Optional<User> fromString(String s) {
                return Optional.empty();
            }
        });
        userFilter.getItems().addAll(users);

        // This empty is the 'All Users' option
        userFilter.getItems().addFirst(Optional.empty());
        userFilter.setValue(userFilter.getItems().getFirst());

        // Add 'edit' buttons for each user
        participants.getChildren().clear();
        for (Optional<User> user : users) {
            if (user.isEmpty()) {
                continue;
            }
            Button button = new Button(user.get().getName());
            button.setBackground(null);
            button.setOnAction(e -> editParticipant(user.get()));
            participants.getChildren().add(button);
        }
    }

    public void refreshFilter() {
        Optional<User> user = userFilter.getValue();
        String search = searchBox.getText();
        expenses.setPredicate(expense -> expense.getDescription().toLowerCase().contains(search.toLowerCase())
                && (user == null || user.isEmpty() || expense.getPayerId().equals(user.get().getId())));
    }
}
