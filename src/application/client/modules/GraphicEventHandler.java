package application.client.modules;

import application.util.answer.Answer;
import application.util.answer.AnswerType;
import application.util.answer.SignInAcceptedAnswer;
import application.util.request.ConstantConnectionRequest;
import application.util.request.SearchConnectionRequest;
import application.util.request.SignUpRequest;
import application.util.user.SimpleUser;

import java.util.List;

public class GraphicEventHandler {
    public static Answer requestSignIn(String username, String password) {
        Answer answer = Network.request(new ConstantConnectionRequest(username, password));
        if (answer.type == AnswerType.SIGN_IN_ACCEPTED) {
            Cache.currentUser = ((SignInAcceptedAnswer) answer).user;
            initiateConnections(username, password);
        }
        return answer;
    }

    private static void initiateConnections(String username, String password) {
        Answer answer = Network.request(new SearchConnectionRequest(username, password));
        //TODO ???
    }


    public static Answer requestSignUp(String name, String username, String password) {
        Answer answer = Network.request(new SignUpRequest(name, username, password));
        return answer;
    }

    public static List<SimpleUser> searchFor(String s) {
        return Network.getSearchResult(s);
    }
}
