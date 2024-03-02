package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class StatisticsPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public StatisticsPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void OverviewPage(){
        mainCtrl.overviewPage();
    }
}
