package at.fh.ooe.moc5.amazingrace.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.model.task.AsyncTaskResult;
import at.fh.ooe.moc5.amazingrace.model.view.LoginViewModel;
import at.fh.ooe.moc5.amazingrace.model.view.UserContextModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.service.ServiceException.ServiceErrorCode;
import at.fh.ooe.moc5.amazingrace.watcher.LoginButtonTextWatcher;
import at.fh.ooe.moc5.amazingrace.watcher.LoginViewModelBindingTextWatcher;

/**
 * This class represents the login activity where an user need to login.
 */
public class LoginActivity extends AbstractActivity<LoginViewModel> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new LoginViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareViews();
    }

    //region utilities
    public void prepareViews() {
        viewModel.reset();
        // Register text change listeners
        EditText username = (EditText) findViewById(R.id.usernameEdTxt);
        username.addTextChangedListener(new LoginViewModelBindingTextWatcher(viewModel, username));
        username.addTextChangedListener(new LoginButtonTextWatcher(this, viewModel));
        EditText password = (EditText) findViewById(R.id.passwordEdTxt);
        password.addTextChangedListener(new LoginViewModelBindingTextWatcher(viewModel, password));
        password.addTextChangedListener(new LoginButtonTextWatcher(this, viewModel));

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
                            model = viewModel.loginAction();
                        } catch (Exception e) {
                            exception = e;
                        }

                        return new AsyncTaskResult<UserContextModel>(model, exception);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        openProgressDialog(R.string.progress_login);
                    }

                    @Override
                    protected void onPostExecute(AsyncTaskResult<UserContextModel> result) {
                        super.onPostExecute(result);
                        closeProgressDialog();
                        // Exception occurred
                        if (result.exception != null) {
                            resetView();
                            if (result.exception instanceof ServiceException) {
                                handleServiceException(((ServiceException) result.exception));
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
                openCloseApplicationDialog();
                break;
        }
    }
    //endregion
}

