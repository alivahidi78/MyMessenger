package application.util.answer;

import application.util.user.User;

public class SignInAcceptedAnswer extends Answer {
    public final User user;
    public SignInAcceptedAnswer(User user) {
        super(true,AnswerType.SIGN_IN_ACCEPTED);
        this.user = user;
    }
}
