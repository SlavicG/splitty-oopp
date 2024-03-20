package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
public class AdminDashboardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public AdminDashboardCtrl(ServerUtils server, MainCtrl mainCtrl)
    {
        this.server=server;
        this.mainCtrl=mainCtrl;
    }
    public void backToStart()
    {

        mainCtrl.startPage();
    }
}
