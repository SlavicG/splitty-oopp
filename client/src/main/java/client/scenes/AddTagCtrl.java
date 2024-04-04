package client.scenes;

import client.utils.ServerUtils;
import commons.dto.Event;
import commons.dto.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.google.inject.Inject;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setEvent(Integer eventId) {
        this.event = server.getEventById(eventId);
    }

    public void setTag(Integer tagId) {
        if (tagId == null) {
            return;
        }
        tag = server.getTagById(tagId, event.getId());
        name.setText(tag.getName());
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
        if(tag == null) {
            Tag newTag = server.addTag(new Tag(null, name.getText(), (float)fx.getRed(), (float)fx.getGreen(), (float) fx.getBlue(), event.getId()), event.getId());
        }
        else {
            Tag changedTag = server.updateTag(new Tag(tag.getId(), name.getText(),
                    (float)fx.getRed(), (float)fx.getGreen(), (float) fx.getBlue(), event.getId()), event.getId());
            tag = null;
        }
        mainCtrl.eventPage(event.getId());
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
        //Tag tag = new Tag();
        //server.deleteTag();
    }
    public void clear() {
        name.setText(null);
        color.setValue(null);
        invalid.setVisible(false);
    }

    public void OverviewPage(){
        mainCtrl.overviewPage();
    }
}
