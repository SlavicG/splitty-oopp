package client.scenes;

import client.utils.ServerUtils;
import commons.dto.Event;
import commons.dto.Tag;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.google.inject.Inject;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTagCtrl implements Initializable {
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
    @FXML
    private Label title;
    @FXML
    private Button remove;
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
            remove.setVisible(false);
            create.setText(resourceBundle.getString("ok"));
            title.setText(resourceBundle.getString("add_tag"));
            name.setText(null);
            color.setValue(Color.WHITE);
            return;
        }
        tag = server.getTagById(tagId, eventId);
        name.setText(server.getTagById(tagId, eventId).getName());
        color.setValue(Color.color(tag.getR()/255, tag.getG()/255, tag.getB()/255));
        create.setText(resourceBundle.getString("ok"));
        title.setText(resourceBundle.getString("edit_tag"));
        remove.setVisible(true);
    }

    public void onCreate() {
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
            invalid.setVisible(true);
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
        assert(tag != null);
        Response response = server.deleteTag(event.getId(), tag.getId());
        if (response.getStatus() == 200) {
            Tag oldTag = new Tag(tag);
            mainCtrl.addUndoFunction(() -> server.createTag(event.getId(), oldTag));
            mainCtrl.tagsPage(event.getId());
            clear();
        }
        else {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to remove tag");
            alert.setHeaderText("Could not remove tag");
            alert.setContentText("This tag is involved in (several) expenses(s). " +
                    "Please remove these expenses before removing the tag.");
            alert.show();
        }
    }
    public void clear() {
        name.setText(null);
        color.setValue(Color.WHITE);
        invalid.setVisible(false);
        tag = null;
    }

    public void OverviewPage(){
        mainCtrl.tagsPage(eventId);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
