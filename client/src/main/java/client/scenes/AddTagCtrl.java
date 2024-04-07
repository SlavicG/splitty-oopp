package client.scenes;

import client.utils.ServerUtils;
import commons.dto.Event;
import commons.dto.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.google.inject.Inject;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;

public class AddTagCtrl {
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
    @FXML
    private Button create;
    private Integer eventId;

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setEvent(Integer eventId) {
        this.event = server.getEventById(eventId);
        this.eventId = eventId;
    }

    public void setTag(Integer tagId, Integer eventId) {
        if (tagId == null) {
            return;
        }
        tag = server.getTagById(tagId, eventId);
        name.setText(server.getTagById(tagId, eventId).getName());
        color.setValue(Color.color(tag.getR()/255, tag.getG()/255, tag.getB()/255));
        create.setText("Edit");

    }

    public void onCreate() {
        invalid.setVisible(true);
        if (color.getValue() == null){
            invalid.setText(resourceBundle.getString("invalid_Tag_colour"));
            return;
        }
        if (name.getText() == null || name.getText().isEmpty()) {
            invalid.setText(resourceBundle.getString("invalid_tag_name"));
            return;
        }
        javafx.scene.paint.Color fx = color.getValue();
        float red = (float) fx.getRed() * 255;
        float green = (float) fx.getGreen() * 255;
        float blue = (float) fx.getBlue() * 255;

        if (tag == null) {
            Tag newTag = server.addTag(new Tag(null, name.getText(), red, green, blue, event.getId()), event.getId());
        } else {
            Tag changedTag = server.updateTag(
                    new Tag(tag.getId(), name.getText(), red, green, blue, event.getId()), event.getId());
            tag = null;
        }
        clear();
        mainCtrl.tagsPage(event.getId());
    }

    public void onDelete() {
        invalid.setVisible(true);
        if (color.getValue() == null) {
            invalid.setText(resourceBundle.getString("invalid_Tag_colour"));
            return;
        }
        if (name.getText() == null || name.getText().isEmpty()) {
            invalid.setText(resourceBundle.getString("invalid_tag_name"));
            return;
        }
    }
    public void clear() {
        name.setText(null);
        color.setValue(Color.WHITE);
        invalid.setVisible(false);
    }

    public void OverviewPage(){
        mainCtrl.tagsPage(eventId);
    }
}
