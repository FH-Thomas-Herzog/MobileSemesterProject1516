package at.fh.ooe.moc5.amazingrace.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.model.LoginViewModel;
import at.fh.ooe.moc5.amazingrace.model.UserContextModel;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
import at.fh.ooe.moc5.amazingrace.watcher.*;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy.ServiceErrorCode;

public class LoginActivity extends Activity implements View.OnClickListener {

    public LoginViewModel loginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginView = new LoginViewModel();
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
                new AsyncTask<Object, Object, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Object... params) {
                        boolean result = Boolean.FALSE;
                        try {
                            result = loginView.loginAction();
                        } catch (RestServiceProxy.ServiceException e) {
                            ServiceErrorCode errorCode = e.getErrorCode();
                            if (errorCode != null) {
                                switch (errorCode) {
                                    case INVALID_REQUEST:
                                        break;
                                    case TIMEOUT:
                                        break;
                                    case UNKNOWN:
                                        break;
                                }
                            }
                        }

                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                        if (result) {
                            ((AmazingRaceApplication) getApplication()).setLoggedUser(new UserContextModel(loginView.getUsername()));
                        }
                    }
                }.execute();
                break;
            case R.id.cancelBtn:

                moveTaskToBack(Boolean.TRUE);

                finish();

                break;

        }

    }
}

