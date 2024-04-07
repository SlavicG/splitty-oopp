package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class MailPageLocig {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public MailPageLocig(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

}
