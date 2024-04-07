package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;

public class TagsPageCtrl {
    private ResourceBundle resourceBundle;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    @FXML
    private ColorPicker color;
    @FXML
    private TextField name;
    @FXML
    public Label invalid;
    private Event event;
    private Tag tag;

    @Inject
    public TagsPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

}
