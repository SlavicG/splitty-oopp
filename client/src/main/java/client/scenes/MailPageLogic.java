package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailPageLogic {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public MailPageLogic(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
