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
package client;

import client.scenes.*;
import client.utils.Configuration;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.prefs.BackingStoreException;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static ResourceBundle bundle;

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initialize(new Pair<>(primaryStage, null));
    }

    public void initialize(Pair<Stage, Consumer<MainCtrl>> stageIntegerPair) {
        Stage primaryStage = stageIntegerPair.getKey();
        Configuration configuration = INJECTOR.getInstance(Configuration.class);
        bundle = ResourceBundle.getBundle("client.language", configuration.getLocale());
        var overview = FXML.load(QuoteOverviewCtrl.class, bundle, "client", "scenes", "QuoteOverview.fxml");
        var add = FXML.load(AddQuoteCtrl.class, bundle, "client", "scenes", "AddQuote.fxml");
        var startPage = FXML.load(StartPageCtrl.class, bundle, "client", "scenes", "StartPage.fxml");
        var overviewPage = FXML.load(OverviewPageCtrl.class, bundle, "client", "scenes", "OverviewPage.fxml");
        var invitationPage = FXML.load(InvitationPageCtrl.class, bundle, "client", "scenes", "InvitationPage.fxml");
        var addParticipant = FXML.load(AddParticipantCtrl.class, bundle, "client", "scenes", "AddParticipantPage.fxml");
        var addTagPage = FXML.load(AddTagCtrl.class, bundle, "client", "scenes", "AddTagPage.fxml");
        var addExpensePage = FXML.load(AddExpenseCtrl.class, bundle, "client", "scenes", "AddExpense.fxml");
        var statisticsPage = FXML.load(StatisticsPageCtrl.class, bundle, "client", "scenes", "StatisticsPage.fxml");
        var loginPage = FXML.load(LoginPageCtrl.class, bundle,"client", "scenes", "AdminLogin.fxml");
        var adminPage = FXML.load(AdminDashboardCtrl.class, bundle, "client", "scenes", "AdminDashboard.fxml");
        var editEventName = FXML.load(EditEventNameCtrl.class, bundle, "client", "scenes", "EditEventName.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        var tagsPage = FXML.load(TagsPageCtrl.class, bundle,"client", "scenes", "TagsPage.fxml");
        var settleDebts = FXML.load(SettleDebtsCtrl.class, bundle,"client", "scenes", "SettleDebts.fxml");
        mainCtrl.initialize(primaryStage, this::initialize, overview, add, startPage, overviewPage, invitationPage,
            addParticipant, addExpensePage, statisticsPage, loginPage, adminPage,editEventName,
            addTagPage, tagsPage, settleDebts);
        if (stageIntegerPair.getValue() != null) {
            stageIntegerPair.getValue().accept(mainCtrl);
        }
        else {
            mainCtrl.startPage();
        }
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            startPage.getKey().stop();
        });
    }

    public static String getLocalizedString(String key) {
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        } else {
            return "**Missing Key: " + key + "**";
        }
    }

    public static void switchLocale(String languageCode) throws BackingStoreException {
        if (languageCode != null) {
            Configuration uc = new Configuration();
            uc.setLangConfig(languageCode);
            Locale locale = new Locale(languageCode);
            bundle = ResourceBundle.getBundle("client.language", locale);
        } else {
            bundle = ResourceBundle.getBundle("client.language", Locale.getDefault());
        }
    }
}