package at.fh.ooe.moc5.amazingrace.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Thomas on 12/24/2015.
 */
public class UserContextModel implements Serializable {

    private final String username;
    private final Calendar loginDate;

    public UserContextModel(String username) {
        this.username = username;
        this.loginDate = Calendar.getInstance();
    }

    public String getUsername() {
        return username;
    }

    public Calendar getLoginDate() {
        return loginDate;
    }
}
