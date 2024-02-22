package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.stage.Stage;

public class StartPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public StartPageCtrl(ServerUtils server, MainCtrl mainCtrl, Stage primaryStage) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
}
