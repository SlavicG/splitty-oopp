package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TagsPageCtrl{
    private ResourceBundle resourceBundle;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private Integer eventId;
    @FXML
    private FlowPane tags;

    @Inject
    public TagsPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setEvent(Integer id) {
        this.eventId = id;
        refresh();
    }
    public void addTagPage() {
        mainCtrl.addTagPage(eventId, null);
    }
    public void onCancel() {
        mainCtrl.eventPage(eventId);
    }
    public void refresh() {
        List<Optional<Tag>> allTags = server.getTags(eventId).stream().map(Optional::of).toList();
        tags.getChildren().clear();
        for (Optional<Tag> tag : allTags) {
            if (tag.isEmpty()) {
                continue;
            }
            Button button = new Button(tag.get().getName());
            button.setBackground(null);
            button.setOnAction(e -> editTag(tag.get()));
            int r = Math.round(tag.get().getR());
            int g = Math.round(tag.get().getG());
            int b = Math.round(tag.get().getB());
            String cssColor = String.format("rgba(%d, %d, %d, 0.2)", r, g, b);
            button.setStyle("-fx-background-color: " + cssColor + ";");
            tags.getChildren().add(button);
        }
    }
    public void editTag(Tag tag) {
        mainCtrl.addTagPage2(tag.getEventId(), tag.getId());
    }
}
