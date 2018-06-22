package application.client.modules;

import application.util.answer.Answer;
import application.util.answer.AnswerType;
import application.util.answer.SignInAcceptedAnswer;
import application.util.request.ConstantConnectionRequest;
import application.util.request.SignUpRequest;

public class GraphicEventHandler {
    public static Answer requestSignIn(String username, String password) {
        Answer answer = Network.request(new ConstantConnectionRequest(username, password));
        if (answer.type == AnswerType.SIGN_IN_ACCEPTED) {
            Cache.currentUser = ((SignInAcceptedAnswer) answer).user;
        }
        return answer;
    }


    public static Answer requestSignUp(String name, String username, String password) {
        Answer answer = Network.request(new SignUpRequest(name, username, password));
        return answer;
    }
}
