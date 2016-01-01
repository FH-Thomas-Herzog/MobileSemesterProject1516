package at.fh.ooe.moc5.amazingrace;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import at.fh.ooe.moc5.amazingrace.model.view.UserContextModel;

/**
 * Created by Thomas on 12/24/2015.
 * This is the custom application implementation which handles uncaught exceptions.
 * The application will get killed on a an uncaught exception.
 */
public class AmazingRaceApplication extends Application {

    public UserContextModel loggedUser;

    public UserContextModel getLoggedUser() {
        return loggedUser;
    }

    public static final String EXTRA_ROUTE = "EXTRA_ROUTE";

    /**
     * Registers an exception handler for uncaught exceptions.
     */
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

    /**
     * Handles uncaught excpetions which are considered critical, therefore the application will exist with an error.
     *
     * @param thread the current thread the exception occurred
     * @param e      the occurred exception
     */
    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();
        Log.e("error", "Uncaught exception catched", e);
        System.exit(1);
    }

    /**
     * Sets the logged user which can be accessed by all implemented activities.
     *
     * @param loggedUser the logged user
     */
    public void setLoggedUser(UserContextModel loggedUser) {
        this.loggedUser = loggedUser;
    }
}
