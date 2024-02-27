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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static ResourceBundle bundle;

    public static void main(String[] args) throws URISyntaxException, IOException {
        Configuration configuration = INJECTOR.getInstance(Configuration.class);
        bundle = ResourceBundle.getBundle("client.language", configuration.getLocale());
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var overview = FXML.load(QuoteOverviewCtrl.class, bundle, "client", "scenes", "QuoteOverview.fxml");
        var add = FXML.load(AddQuoteCtrl.class, bundle, "client", "scenes", "AddQuote.fxml");
        var startPage = FXML.load(StartPageCtrl.class, bundle, "client", "scenes", "StartPage.fxml");
        var overviewPage = FXML.load(OverviewPageCtrl.class, bundle, "client", "scenes", "OverviewPage.fxml");
        var invitationPage = FXML.load(InvitationPageCtrl.class, bundle, "client", "scenes","InvitationPage.fxml");
        var participantPage = FXML.load(ParticipantPageCtrl.class, bundle, "client", "scenes", "AddParticipantPage.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add, startPage, overviewPage, invitationPage, participantPage, addExpensePage);
    }
}