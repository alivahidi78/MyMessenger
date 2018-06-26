package application.client.controllers;

import application.client.modules.Cache;
import application.client.modules.LogicalEventHandler;
import application.util.user.Info;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ContactList extends GraphicController implements Initializable {
    public TextField searchField;
    public ListView<Info> contactListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedUser = new SimpleObjectProperty<>();
        contactList = FXCollections.observableArrayList();
        ObservableList<Info> searchList = FXCollections.observableArrayList();
        contactListView.setItems(contactList);
        searchList.clear();
        for (Long id : Cache.getChats().keySet())
            contactList.add(LogicalEventHandler.getUserInfo(id));
        contactListView.setCellFactory(new Callback<>() {
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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Info> list = searchFor(newValue);
            searchList.clear();
            searchList.addAll(list);
            if (newValue.isEmpty())
                contactListView.setItems(contactList);
            else
                contactListView.setItems(searchList);
        });

        contactListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectedUser.setValue(newValue));
    }
}
