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
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;
    private Scene startPage;
    private Scene overviewPage;
    private Scene invitationPage;
    private Scene participantPage;
    private Scene addExpensePage;

    private AddQuoteCtrl addCtrl;

    private InvitationPageCtrl invitationPageCtrl;
    private Scene add;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add, Pair<StartPageCtrl, Parent> startPage, Pair<OverviewPageCtrl, Parent> overviewPage, Pair<InvitationPageCtrl, Parent> invitationPage,
                           Pair<ParticipantPageCtrl, Parent> participantPage, Pair<AddExpenseCtrl, Parent> addExpensePage) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.startPage = new Scene(startPage.getValue());
        this.overviewPage = new Scene(overviewPage.getValue());
        this.invitationPage = new Scene((invitationPage.getValue()));
        this.participantPage = new Scene(participantPage.getValue());
        this.invitationPageCtrl = invitationPage.getKey();
        this.addExpensePage = new Scene(addExpensePage.getValue());

        showOverview();
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
    }
    public void overviewPage(){
        primaryStage.setTitle("Overview Page");
        primaryStage.setScene(overviewPage);
    }
    public void invitationPage(Label name){
        primaryStage.setTitle("Invitation Page");
        invitationPageCtrl.setName(name);
        primaryStage.setScene(invitationPage);
    }
    public void addParticipantPage() {
        primaryStage.setTitle("Add Participant Page");
        primaryStage.setScene(participantPage);
    }
    public void eventPage() {
        primaryStage.setTitle("Overview Page");
        primaryStage.setScene(overviewPage);
    }

    public void addExpensePage() {
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpensePage);
    }
}