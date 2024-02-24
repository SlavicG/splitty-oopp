package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class ParticipantPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public ParticipantPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void overview() {
        mainCtrl.overviewPage();
    }
}
