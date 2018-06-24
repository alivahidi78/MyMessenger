package application.client.modules;

import application.client.ClientMain;
import application.util.answer.Answer;
import application.util.answer.AnswerType;
import application.util.answer.SignInAcceptedAnswer;
import application.util.message.Message;
import application.util.message.TextMessage;
import application.util.request.ConstantConnectionRequest;
import application.util.request.SearchConnectionRequest;
import application.util.request.SignUpRequest;
import application.util.request.UserInfoRequest;
import application.util.user.SimpleUser;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphicEventHandler {
    public static ObservableList<SimpleUser> contactList;
    public static Answer requestSignIn(String username, String password) {
        Answer answer = Network.request(new ConstantConnectionRequest(username, password));
        if (answer.type == AnswerType.SIGN_IN_ACCEPTED) {
            Cache.currentUser = ((SignInAcceptedAnswer) answer).user;
            Cache.chats = ((SignInAcceptedAnswer) answer).user.getChats();
            initiateConnections();
        }
        return answer;
    }

    private static void initiateConnections() {
        Answer SearchConAnswer = Network.request(new SearchConnectionRequest(
                Cache.currentUser.getUsername(), Cache.currentUser.getPassword()));
        Answer userInfoAnswer = Network.request(new UserInfoRequest(Cache.currentUser.getUsername(),
                Cache.currentUser.getPassword()));
        //TODO ???
    }


    public static Answer requestSignUp(String name, String username, String password, Image userImg) {
        Answer answer = Network.request(new SignUpRequest(name, username, password));
        return answer;
    }

    public static List<SimpleUser> searchFor(String s) {
        try {
            return Network.getSearchResult(s);
        } catch (IOException e) {
            return new ArrayList<>();
            //TODO ERROR
        }
    }

    public static SimpleUser getUserInfo(long id){
        try {
            return Network.getUserInfo(id);
        }catch (IOException e){
            return  null;
        }
    }

    public static Image choosePicture(String defaultImgPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All Images", "*.*"));//TODO list extensions
        try {
            File file = fileChooser.showOpenDialog(ClientMain.getStage());
            return new Image("file:" + file.getAbsolutePath());
        } catch (NullPointerException e) {
            return new Image(defaultImgPath);
        }
    }

    public static void signOut() {
        Cache.clear();
        try {
            Network.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(TextMessage message) {
        try {
            Network.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadCache(Message message) {
        Platform.runLater(()->{
            contactList.add(getUserInfo(message.sender));
        });
    }
}
