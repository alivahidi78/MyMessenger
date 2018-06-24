package application.util.message;

import java.util.Date;
import java.util.LinkedList;

public class TextMessage extends Message {
    private String text;

    public TextMessage(String s, long sender, LinkedList<Long> targets, Date date) {
        super(sender, targets, date);
        this.text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}
