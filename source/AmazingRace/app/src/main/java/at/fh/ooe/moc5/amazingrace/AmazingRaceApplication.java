package at.fh.ooe.moc5.amazingrace;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import at.fh.ooe.moc5.amazingrace.model.view.UserContextModel;

/**
 * Created by Thomas on 12/24/2015.
 */
public class AmazingRaceApplication extends Application {

    public UserContextModel loggedUser;

    public UserContextModel getLoggedUser() {
        return loggedUser;
    }

    public void onCreate() {
        super.onCreate();
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();
        Log.e("error", "Uncaught exception catched", e);
        Toast.makeText(this, R.string.error_unknown, Toast.LENGTH_LONG);
    }

    public void setLoggedUser(UserContextModel loggedUser) {
        this.loggedUser = loggedUser;
    }
}
