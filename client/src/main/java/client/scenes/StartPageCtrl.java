package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class StartPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public StartPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void showOverview() {
        mainCtrl.showOverview();
    }
    public void OverviewPage(){
        mainCtrl.overviewPage();
    }
}
