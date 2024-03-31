package client.scenes;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import client.utils.ServerUtils;
import com.google.inject.Inject;


public class LoginPageCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Inject
    public LoginPageCtrl(ServerUtils server, MainCtrl mainCtrl)
    {
        this.server=server;
        this.mainCtrl=mainCtrl;
    }
    // Method to handle login action
    @FXML
    protected void onLoginButtonClick() {
        String password = passwordField.getText();

        // Now, authenticate with the backend. This is a simplified example.
        // You'd replace this with actual logic to send a request to your backend.
        boolean isAuthenticated = server.login(password);
        if (isAuthenticated) {
            mainCtrl.adminPage();
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void backToStart()
    {
        mainCtrl.startPage();
    }
}
