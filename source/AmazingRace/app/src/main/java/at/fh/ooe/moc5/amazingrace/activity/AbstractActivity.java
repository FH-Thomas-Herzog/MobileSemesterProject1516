package at.fh.ooe.moc5.amazingrace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
public class AbstractActivity<T> extends Activity {

    protected T viewModel;
    protected AmazingRaceApplication application;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (AmazingRaceApplication) getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        application = (AmazingRaceApplication) getApplication();
    }

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

    protected void openCloseApplicationDialog() {
        DialogUtil.createAlertDialog(AbstractActivity.this, getString(R.string.dialog_title_warning), getString(R.string.warning_want_quit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finishAffinity();
                        break;
                }
            }
        }).show();
    }

    protected void openProgressDialog(int messageId) {
        progress = ProgressDialog.show(AbstractActivity.this, getString(R.string.progress_title), getString(messageId), Boolean.TRUE);
    }

    protected void closeProgressDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
}
