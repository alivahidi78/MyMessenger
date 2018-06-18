package application.util.message;

public class TextMessage extends Message {
    private String text;

    public TextMessage(String s) {
        this.text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}
