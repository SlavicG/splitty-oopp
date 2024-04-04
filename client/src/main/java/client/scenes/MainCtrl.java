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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Stack;

public class MainCtrl {
    private Stage primaryStage;
    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;
    private Scene startPage;
    private StartPageCtrl startPageCtrl;
    private Scene overviewPage;
    private OverviewPageCtrl overviewPageCtrl;
    private Scene invitationPage;
    private Scene addParticipantPage;
    private AddParticipantCtrl addParticipantPageCtrl;
    private Scene addExpensePage;
    private Scene statisticsPage;
    private StatisticsPageCtrl statisticsPageCtrl;
    private Scene editEventNamePage;

    private AddQuoteCtrl addCtrl;

    private InvitationPageCtrl invitationPageCtrl;
    private AddExpenseCtrl addExpenseCtrl;
    private LoginPageCtrl loginPageCtrl;
    private AdminDashboardCtrl adminDashboardCtrl;
    private EditEventNameCtrl editEventNameCtrl;
    private Scene adminPage;
    private Scene loginPage;
    private Scene add;

    private final Stack<Runnable> undoFunctionHistory = new Stack<>();

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<StartPageCtrl, Parent> startPage,
                           Pair<OverviewPageCtrl, Parent> overviewPage,
                           Pair<InvitationPageCtrl, Parent> invitationPage,
                           Pair<AddParticipantCtrl, Parent> addParticipantPage,
                           Pair<AddExpenseCtrl, Parent> addExpensePage,
                           Pair<StatisticsPageCtrl, Parent> statisticsPage,
                           Pair<LoginPageCtrl, Parent> loginPage,
                           Pair<AdminDashboardCtrl, Parent> adminPage,
                           Pair<EditEventNameCtrl, Parent> editEventNamePage) {

        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.startPage = new Scene(startPage.getValue());
        this.startPageCtrl = startPage.getKey();
        this.overviewPage = new Scene(overviewPage.getValue());
        this.overviewPageCtrl = overviewPage.getKey();
        this.overviewPage.getAccelerators().put(
                new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN), this::undo);
        this.invitationPage = new Scene((invitationPage.getValue()));
        this.addParticipantPage = new Scene(addParticipantPage.getValue());
        this.addParticipantPageCtrl = addParticipantPage.getKey();
        this.invitationPageCtrl = invitationPage.getKey();
        this.addExpenseCtrl = addExpensePage.getKey();
        this.addExpensePage = new Scene(addExpensePage.getValue());
        this.statisticsPage = new Scene(statisticsPage.getValue());
        this.statisticsPageCtrl = statisticsPage.getKey();
        this.loginPage = new Scene(loginPage.getValue());
        this.loginPageCtrl = loginPage.getKey();
        this.adminPage = new Scene(adminPage.getValue());
        this.adminDashboardCtrl = adminPage.getKey();
        this.editEventNamePage = new Scene(editEventNamePage.getValue());
        this.editEventNameCtrl = editEventNamePage.getKey();

        startPage();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void startPage() {
        primaryStage.setTitle("Start Page");
        primaryStage.setScene(startPage);
        startPageCtrl.refresh();
    }

    public void overviewPage() {
        primaryStage.setTitle("Overview Page");
        primaryStage.setScene(overviewPage);
        overviewPageCtrl.refresh();
    }

    public void invitationPage(Label name, Integer eventId) {
        primaryStage.setTitle("Invitation Page");
        invitationPageCtrl.setEvent(eventId);
        invitationPageCtrl.clear();
        invitationPageCtrl.setName(name);
        invitationPageCtrl.refresh();
        primaryStage.setScene(invitationPage);
    }

    public void addParticipantPage(Integer eventId, Integer userId) {
        primaryStage.setTitle("Add Participant Page");
        addParticipantPageCtrl.setEvent(eventId);
        addParticipantPageCtrl.clear();
        addParticipantPageCtrl.setUser(userId);
        primaryStage.setScene(addParticipantPage);
    }

    public void eventPage(Integer eventId) {
        primaryStage.setTitle("Overview Page");
        primaryStage.setScene(overviewPage);
        overviewPageCtrl.setEventId(eventId);
    }

    public void addExpensePage(Integer eventId, Integer expenseId) {
        primaryStage.setTitle("Add Expense");
        addExpenseCtrl.clear();
        addExpenseCtrl.setEvent(eventId);
        addExpenseCtrl.setExpenseId(expenseId);
        primaryStage.setScene(addExpensePage);
    }

    public void statisticsPage(Integer eventId) {
        primaryStage.setTitle("Statistics of " + eventId);
        statisticsPageCtrl.setEvent(eventId);
        primaryStage.setScene(statisticsPage);
        statisticsPageCtrl.refresh();
    }

    public void loginPage(){
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(loginPage);
    }

    public void adminPage()
    {
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(adminPage);
        if(adminDashboardCtrl != null)
            adminDashboardCtrl.refresh();
    }
    public void editEventName(Integer eventId) {
        primaryStage.setTitle("Edit Event Name");
        editEventNameCtrl.setEvent(eventId);
        primaryStage.setScene(editEventNamePage);
    }

    public void addUndoFunction(Runnable undoFunction) {
        undoFunctionHistory.push(undoFunction);
    }

    public void undo() {
        if (undoFunctionHistory.isEmpty()) {
            return;
        }
        undoFunctionHistory.pop().run();
        overviewPageCtrl.refresh();
    }

    public void clearUndoStack() {
        undoFunctionHistory.clear();
    }
}