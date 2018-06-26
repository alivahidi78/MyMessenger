package application.client.controllers;

import application.client.modules.LogicalEventHandler;
import application.util.answer.Answer;
import application.util.user.Info;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class CreateGroupDialog extends GraphicController implements Initializable {
    public VBox contactListBox;
    public TextField nameField;
    public ImageView imageView;
    public ListView<Info> groupMemberList;
    private ObservableList<Info> list;
    private Set<Long> groupMembers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactListBox.getChildren().add(getContactList());
        list = FXCollections.observableArrayList();
        groupMembers = new LinkedHashSet<>();
        groupMemberList.setItems(list);

        groupMemberList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Info> call(ListView<Info> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Info user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null && !empty) {
                            ContactCell cell = new ContactCell();
                            cell.setInfo(user);
                            setGraphic(cell.getBox());
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    public void create() {
        if (nameField.getText().isEmpty())
            getNewAlert(Alert.AlertType.ERROR, "Please choose a name!").show();
        else {
            Answer answer = LogicalEventHandler.requestGroupCreation(nameField.getText(), groupMembers);
            if (answer.requestAccepted) {
                getNewAlert(Alert.AlertType.INFORMATION, "Group created!").show();
                goToChatScene();
            } else
                getNewAlert(Alert.AlertType.ERROR, "Failed to create group!").show();
        }
    }

    public void goToChatScene() {
        goTo("ChatScene");
    }

    public void addToGroup() {
        Info user = selectedUser.get();
        if (user != null) {
            if (!list.contains(user))
                list.add(user);
            groupMembers.add(user.getID());
        }
    }

    public void removeFromGroup() {
        Info user = groupMemberList.getSelectionModel().selectedItemProperty().get();
        if(user != null) {
            list.remove(user);
            groupMembers.remove(user.getID());
        }
    }
}
