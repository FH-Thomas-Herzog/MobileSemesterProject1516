package at.fh.ooe.moc5.amazingrace.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.activity.LoginActivity;
import at.fh.ooe.moc5.amazingrace.model.view.LoginViewModel;

/**
 * Created by Thomas on 12/24/2015.
 */
public class LoginButtonTextWatcher implements TextWatcher {

    private LoginViewModel viewModel;
    private final LoginActivity activity;

    public LoginButtonTextWatcher(LoginActivity activity, LoginViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        Button loginButton = (Button) activity.findViewById(R.id.loginBtn);
        loginButton.setEnabled(viewModel.isValid());
    }
}
