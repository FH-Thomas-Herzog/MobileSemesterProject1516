package at.fh.ooe.moc5.amazingrace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.util.DialogUtil;

/**
 * Created by Thomas on 12/28/2015.
 * <p/>
 * This is the base class for all activities which forces the activity to define one view model it uses.
 * This class provides common functionality usable by all activities.
 */
public class AbstractActivity<T extends Serializable> extends AppCompatActivity {

    protected T viewModel;
    protected AmazingRaceApplication application;
    protected Boolean validViewModel = Boolean.TRUE;

    private ProgressDialog progress;

    private static final String VIEW_MODEL = "AbstractActivity#VIEW_MODEL";

    public static enum MenuGroup {
        ROUTE_ITEM(0),
        OPTIONS(1);

        public final int value;

        private MenuGroup(int value) {
            this.value = value;
        }

        public static MenuGroup getMenuGroupForValue(int value) {
            for (MenuGroup group : MenuGroup.values()) {
                if (group.value == value) {
                    return group;
                }
            }

            throw new IllegalArgumentException("MenuGroup with value '" + value + "' not found");
        }
    }

    public static enum MenuId {
        PLAY(0),
        RESET(1),
        RESET_ALL_ROUTES(2),
        CLOSE(3);

        public final int value;

        private MenuId(int value) {
            this.value = value;
        }

        public static MenuId getMenuIdForValue(int value) {
            for (MenuId id : MenuId.values()) {
                if (id.value == value) {
                    return id;
                }
            }

            throw new IllegalArgumentException("MenuGroup with value '" + value + "' not found");
        }
    }

    //region Activity Methods

    /**
     * Initializes the backed application so that it can be accessed by the concrete activity application.
     * It also tries to get the saved view model from the savedInstanceState which can be null here if haven't been saved before.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (AmazingRaceApplication) getApplication();
        if (savedInstanceState != null) {
            viewModel = (T) savedInstanceState.get(VIEW_MODEL);
        }
    }

    /**
     * Re-initializes the application on resume of the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        application = (AmazingRaceApplication) getApplication();
        if (viewModel == null) {
            validViewModel = Boolean.FALSE;
            openInvalidUserAccountDialog();
        }
    }

    /**
     * Saves the backed view model.
     *
     * @param outState the outcoming state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(VIEW_MODEL, viewModel);
    }
    //endregion

    /**
     * Handles a service exception by displaying a toast with a proper message
     *
     * @param exception the occurred ServiceException
     * @return true if handled here, false otherwise, which indicates that no error code or an unknown error occurred was set.
     */
    protected boolean handleServiceException(ServiceException exception) {
        Objects.requireNonNull(exception);
        boolean handled = Boolean.FALSE;
        ServiceException.ServiceErrorCode errorCode = exception.getErrorCode();
        if (errorCode != null) {
            switch (errorCode) {
                case INVALID_REQUEST:
                    Toast.makeText(AbstractActivity.this, R.string.error_request_invalid, Toast.LENGTH_LONG).show();
                    handled = Boolean.TRUE;
                    break;
                case TIMEOUT:
                    Toast.makeText(AbstractActivity.this, R.string.error_request_timeout, Toast.LENGTH_LONG).show();
                    handled = Boolean.TRUE;
                    break;
                case UNKNOWN:
                    Toast.makeText(AbstractActivity.this, R.string.error_unknown, Toast.LENGTH_LONG).show();
                    handled = Boolean.TRUE;
                    break;
                case INVALID_CREDENTIALS:
                    openInvalidUserAccountDialog();
                    handled = Boolean.TRUE;
            }
        } else {
            Toast.makeText(AbstractActivity.this, R.string.error_unknown, Toast.LENGTH_LONG).show();
        }
        Log.e(this.getClass().getSimpleName(), "ServiceException occurred", exception);

        return handled;
    }

    /**
     * Opens the invalid user dilaog which indicates that the user has become invalid during application invocation.
     * After the user clikced ok the user will be logged out.
     */
    protected void openInvalidUserAccountDialog() {
        DialogUtil.createErrorDialog(AbstractActivity.this, getString(R.string.error_user_became_invalid), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                application.setLoggedUser(null);
                finishAffinity();
                startActivity(new Intent(AbstractActivity.this, LoginActivity.class));
            }
        }).show();
    }

    /**
     * Opens an dialog to ask the user if he really wants to quit the application.
     */
    protected void openCloseApplicationDialog() {
        DialogUtil.createAlertDialog(AbstractActivity.this, getString(R.string.dialog_title_warning), getString(R.string.warning_want_quit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        application.setLoggedUser(null);
                        finishAffinity();
                        break;
                }
            }
        }).show();
    }

    /**
     * Opens a progress dialog with an custom message.
     *
     * @param messageId the resource id of the message
     */
    protected void openProgressDialog(int messageId) {
        progress = ProgressDialog.show(AbstractActivity.this, getString(R.string.progress_title), getString(messageId), Boolean.TRUE);
    }

    /**
     * Closes the progress dialog.
     */
    protected void closeProgressDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
}
