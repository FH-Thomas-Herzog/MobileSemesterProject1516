package at.fh.ooe.moc5.amazingrace.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.model.view.LoginViewModel;
import at.fh.ooe.moc5.amazingrace.model.view.UserContextModel;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy.ServiceErrorCode;
import at.fh.ooe.moc5.amazingrace.util.DialogUtil;
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

    public void prepareViews() {
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                new AsyncTask<Object, Object, UserContextModel>() {
                    @Override
                    protected UserContextModel doInBackground(Object... params) {
                        UserContextModel model = null;
                        try {
                            model = loginView.loginAction();
                        } catch (RestServiceProxy.ServiceException e) {
                            ServiceErrorCode errorCode = e.getErrorCode();
                            loginView.reset();
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
                            }
                        }

                        return model;
                    }

                    @Override
                    protected void onPreExecute() {
                        progress = ProgressDialog.show(LoginActivity.this, getString(R.string.progress_title), getString(R.string.progress_login), Boolean.TRUE);
                    }

                    @Override
                    protected void onPostExecute(UserContextModel model) {
                        progress.dismiss();
                        if (model != null) {
                            application.setLoggedUser(model);
                            final Intent intent = new Intent(LoginActivity.this, RouteActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.error_login_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.id.cancelBtn:
                finishAffinity();
                break;
        }
    }
}

