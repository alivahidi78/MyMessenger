package application.util.message;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Date;

public class FileMessage extends Message {
    public final String name;
    public transient DoubleProperty progress = new SimpleDoubleProperty();
    public transient BooleanProperty download;

    public FileMessage(long sender, long group, long target, Date date, String name) {
        super(MessageType.FILE, sender, group, target, date);
        this.name = name;
    }

    @Override
    public String toString() {
        return "" + sender + " " + target + " " + date;
    }
}
