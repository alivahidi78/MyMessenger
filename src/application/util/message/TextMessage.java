package application.util.message;

import java.util.Date;
import java.util.Set;

public class TextMessage extends Message {
    private String text;

    public TextMessage(String s, long sender, long group, long target, Date date) {
        super(MessageType.TEXT, sender, group, target, date);
        this.text = s;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
