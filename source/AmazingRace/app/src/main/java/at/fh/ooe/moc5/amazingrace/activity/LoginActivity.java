package at.fh.ooe.moc5.amazingrace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.model.task.AsyncTaskResult;
import at.fh.ooe.moc5.amazingrace.model.view.LoginViewModel;
import at.fh.ooe.moc5.amazingrace.model.view.UserContextModel;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.service.ServiceException.ServiceErrorCode;
import at.fh.ooe.moc5.amazingrace.watcher.LoginButtonTextWatcher;
import at.fh.ooe.moc5.amazingrace.watcher.LoginViewModelBindingTextWatcher;

/**
 * This class represents the login activity where an user need to login.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private ProgressDialog progress;
    private AmazingRaceApplication application;
    private LoginViewModel loginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginView = new LoginViewModel();
        application = ((AmazingRaceApplication) getApplication());
        prepareViews();
        // Nothing to restore on login activity
    }

    //region utilities
    public void prepareViews() {
        loginView.reset();
        // Register text change listeners
        EditText username = (EditText) findViewById(R.id.usernameEdTxt);
        username.addTextChangedListener(new LoginViewModelBindingTextWatcher(loginView, username));
        username.addTextChangedListener(new LoginButtonTextWatcher(this, loginView));
        EditText password = (EditText) findViewById(R.id.passwordEdTxt);
        password.addTextChangedListener(new LoginViewModelBindingTextWatcher(loginView, password));
        password.addTextChangedListener(new LoginButtonTextWatcher(this, loginView));

        // Register Login action
        Button loginButton = (Button) findViewById(R.id.loginBtn);
        loginButton.setEnabled(Boolean.FALSE);
        loginButton.setOnClickListener(this);
        Button cancelButton = (Button) findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(this);
    }

    private void resetView() {
        EditText username = (EditText) findViewById(R.id.usernameEdTxt);
        EditText password = (EditText) findViewById(R.id.passwordEdTxt);

        // Could be called on non visible activity
        if ((username != null) && (password != null)) {
            username.setText("");
            password.setText("");
        }
    }
    //endregion

    //region Listeners
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                new AsyncTask<Object, Object, AsyncTaskResult<UserContextModel>>() {
                    @Override
                    protected AsyncTaskResult<UserContextModel> doInBackground(Object... params) {
                        UserContextModel model = null;
                        Exception exception = null;
                        try {
                            model = loginView.loginAction();
                        } catch (Exception e) {
                            exception = e;
                        }

                        return new AsyncTaskResult<UserContextModel>(model, exception);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progress = ProgressDialog.show(LoginActivity.this, getString(R.string.progress_title), getString(R.string.progress_login), Boolean.TRUE);
                    }

                    @Override
                    protected void onPostExecute(AsyncTaskResult<UserContextModel> result) {
                        super.onPostExecute(result);
                        progress.dismiss();
                        // Exception occurred
                        if (result.exception != null) {
                            resetView();
                            if (result.exception instanceof ServiceException) {
                                ServiceErrorCode errorCode = ((ServiceException) result.exception).getErrorCode();
                                if (errorCode != null) {
                                    switch (errorCode) {
                                        case INVALID_REQUEST:
                                            Toast.makeText(LoginActivity.this, R.string.error_request_invalid, Toast.LENGTH_LONG).show();
                                            break;
                                        case TIMEOUT:
                                            Toast.makeText(LoginActivity.this, R.string.error_request_timeout, Toast.LENGTH_SHORT).show();
                                            break;
                                        case UNKNOWN:
                                            Toast.makeText(LoginActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                            }
                        }
                        // Login failed due to invalid credentials
                        else if (result.result == null) {
                            resetView();
                            Toast.makeText(LoginActivity.this, R.string.error_login_failed, Toast.LENGTH_SHORT).show();
                        }
                        // Login ok
                        else {
                            application.setLoggedUser(result.result);
                            final Intent intent = new Intent(LoginActivity.this, RouteActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }.execute();
                break;
            case R.id.cancelBtn:
                finishAffinity();
                break;
        }
    }
    //endregion
}

