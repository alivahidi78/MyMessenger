package application.util.answer;

import java.io.Serializable;

public abstract class Answer implements Serializable {
    static final long serialVersionUID = 1L;
    public final boolean requestAccepted;
    public final AnswerType type;

    public Answer(boolean requestAccepted, AnswerType type) {
        this.requestAccepted = requestAccepted;
        this.type = type;
    }
}
