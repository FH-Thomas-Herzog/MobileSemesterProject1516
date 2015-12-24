package at.fh.ooe.moc5.amazingrace;

import android.app.Application;

import at.fh.ooe.moc5.amazingrace.model.UserContextModel;

/**
 * Created by Thomas on 12/24/2015.
 */
public class AmazingRaceApplication extends Application {

    public UserContextModel loggedUser;

    public UserContextModel getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserContextModel loggedUser) {
        this.loggedUser = loggedUser;
    }
}
