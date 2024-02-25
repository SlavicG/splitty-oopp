/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.dto.Person;
import commons.dto.Quote;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuoteOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<Quote> data;
    private Stage primaryStage;

    @FXML
    private TableView<Quote> table;
    @FXML
    private TextField eventName;
    @FXML
    private TableColumn<Quote, String> colFirstName;
    @FXML
    private TableColumn<Quote, String> colLastName;
    @FXML
    private TableColumn<Quote, String> colQuote;

    @Inject
    public QuoteOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Stage primaryStage) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colFirstName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.firstName));
        colLastName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.lastName));
        colQuote.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().quote));
    }

    public void refresh() {
        var quotes = server.getQuotes();
        data = FXCollections.observableList(quotes);
        table.setItems(data);
    }

    public void addEvent() {
        try {
            server.addQuote(getQuote2());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showOverview();
    }
    private Quote getQuote2() {
        var q = eventName.getText();
        return new Quote(new Person(q, "no location added"), "No date added");
    }
    private void clearFields() {
        eventName.clear();
    }
    public void eventPage() {
        mainCtrl.eventPage();
    }
}