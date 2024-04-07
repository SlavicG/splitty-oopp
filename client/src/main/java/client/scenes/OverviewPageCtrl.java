package client.scenes;

import client.utils.Configuration;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.prefs.BackingStoreException;

import static client.Main.switchLocale;

public class OverviewPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final HashMap<Integer, String> userNamesCache;
    private final HashMap<Integer, String> tagCache;
    @FXML
    private Label eventName;
    @FXML
    private Label code;
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
    @FXML
    private TableColumn<Expense, String> tagColumn;
    @FXML
    private Button invitation;
    private Integer eventId;
    private ObservableList<User> data;
    private FilteredList<Expense> expenses;
    private ResourceBundle resources;
    Configuration configuration;

    @Inject
    public OverviewPageCtrl(ServerUtils server, MainCtrl mainCtrl, Configuration configuration) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.configuration = configuration;
        userNamesCache = new HashMap<>();
        tagCache = new HashMap<>();
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
        payerColumn.setCellValueFactory(
                expense -> new ReadOnlyObjectWrapper<>(userNamesCache.get(expense.getValue().getPayerId())));

        tagColumn.setCellValueFactory(
                expense -> new ReadOnlyObjectWrapper<>(tagCache.get(expense.getValue().getTagId())));
        tagColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String tagName, boolean empty) {
                super.updateItem(tagName, empty);
                if (empty || tagName == null) {
                    setText(null);
                    setStyle(null); // Clear any existing styles
                } else {
                    setText(tagName); // Display the tag name as text
                    Tag tagObj = server.getTags(eventId).stream()
                            .filter(t -> t.getName().equals(tagName)).findFirst().orElse(null);

                    if (tagObj != null) {
                        int r = Math.round(tagObj.getR());
                        int g = Math.round(tagObj.getG());
                        int b = Math.round(tagObj.getB());
                        String cssColor = String.format("rgba(%d, %d, %d, 0.2)", r, g, b);
                        setStyle("-fx-background-color: " + cssColor + ";");
                    }
                }
            }
        });
        expenseTable.setPlaceholder(new Label(resources.getString("add_expense_hint")));

        // Add column with a button to edit the expense.
        TableColumn<Expense, Expense> editExpenseColumn = new TableColumn<>("");
        editExpenseColumn.setPrefWidth(30);
        editExpenseColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));
        editExpenseColumn.setCellFactory(param -> {
            ImageView image = new ImageView(new Image("/client/images/pencil.png"));
            int size = 24;
            image.setFitHeight(size);
            image.setPreserveRatio(true);
            Button editButton = new Button();
            editButton.setGraphic(image);
            editButton.setPrefSize(size, size);
            editButton.getStyleClass().add("button-icon");
            editButton.setMaxSize(size, size);
            editButton.setMinSize(size, size);
            editButton.setAccessibleText("Edit Expense");
            return new TableCell<>() {
                @Override
                protected void updateItem(Expense s, boolean b) {
                    super.updateItem(s, b);
                    if (b) {
                        setGraphic(null);
                        return;
                    }

                    editButton.setOnAction(event -> {
                        mainCtrl.addExpensePage(eventId, getItem().getId());
                    });

                    setGraphic(editButton);
                }
            };
        });
        expenseTable.getColumns().add(editExpenseColumn);

        // Listen for new users received through WebSocket
        server.registerForMessages("/topic/users", User.class, this::handleNewUser);

        // Listen for changes in the user filter and search box
        userFilter.setOnAction(actionEvent -> refreshFilter());
        searchBox.textProperty().addListener(((observableValue, s, t1) -> refreshFilter()));

        mainCtrl.clearUndoStack();

        Configuration configuration = new Configuration();
        if (configuration.getMailConfig() == null) {
            invitation.setDisable(true);
            invitation.setStyle("-fx-opacity: 0.5;");
        }
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
        mainCtrl.statisticsPage(eventId);
    }

    public void editEventName() {
        mainCtrl.editEventName(eventId);
    }

    public void tagsPage() {
        mainCtrl.tagsPage(eventId);
    }

    public void refresh() {
        // Set up expenses table.
        expenseTable.getSelectionModel().clearSelection();
        Event event = server.getEventById(eventId);
        expenses = new FilteredList<>(FXCollections.observableList(server.getExpenses(eventId)));
        expenseTable.setItems(expenses);
        eventName.setText(event.getTitle());
        code.setText(event.getCode());

        List<Optional<Tag>> tags = server.getTags(eventId).stream().map(Optional::of).toList();
        tagCache.clear();
        tags.forEach(user -> {
            if (user.isEmpty()) {
                return;
            }
            tagCache.put(user.get().getId(), user.get().getName());
        });

        // Populate the userFilter ChoiceBox with all users that have paid for expense.
        List<Optional<User>> users = server.getUserByEvent(eventId).stream().map(Optional::of).toList();
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
        expenses.setPredicate(expense -> expense.getDescription().toLowerCase().contains(search.toLowerCase()) &&
                (user == null || user.isEmpty() || expense.getPayerId().equals(user.get().getId())));
    }

    public void changeLangEn() throws BackingStoreException {
        configuration.setLangConfig("en");
        switchLocale("en");

    }

    public void changeLangNl() throws BackingStoreException {
        configuration.setLangConfig("nl");
        switchLocale("nl");
    }

    public void changeLangRo() throws BackingStoreException {
        configuration.setLangConfig("ro");
        switchLocale("ro");

    }

    public void addNewLang() {
        try {
            String saveDir = createLanguageTemplate();
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Created new language template");
            alert.setHeaderText("Created new language template");
            alert.setContentText("Your new language template can be found at \"" + saveDir + "\".");
            alert.show();
        }
        catch (IOException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed creating new language template");
            alert.setHeaderText("Failed creating new language template");
            alert.setContentText("Something went wrong while creating a new language template:\n" + e);
            alert.show();
        }
    }

    public String createLanguageTemplate() throws IOException {
        String path = String.format("%s/Downloads/template.properties", System.getProperty("user.home"));
        createLanguageTemplate(new FileWriter(path));
        return path;
    }

    public void createLanguageTemplate(Writer writer) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("client.language", Locale.forLanguageTag("en"));
        writer.write("# Add the name of your new language to the first line of this file as a comment!\n"+
                "# Send the final version to splittyteam32@gmail.com\n");
        for (String key : bundle.keySet()) {
            writer.write(String.format("%s = %s\n", key, bundle.getString(key)));
        }
        writer.flush();
    }
}
