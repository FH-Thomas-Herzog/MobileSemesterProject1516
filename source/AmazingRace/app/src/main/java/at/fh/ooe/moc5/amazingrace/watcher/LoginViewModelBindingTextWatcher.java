package at.fh.ooe.moc5.amazingrace.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.model.view.LoginViewModel;

/**
 * Created by Thomas on 12/24/2015.
 * Performs a binding to the given view model
 */
public class LoginViewModelBindingTextWatcher implements TextWatcher {

    private final LoginViewModel view;
    private final EditText text;

    public LoginViewModelBindingTextWatcher(LoginViewModel view, EditText text) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(text);
        this.view = view;
        this.text = text;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (text.getId()) {
            case R.id.usernameEdTxt:
                view.setUsername(s.toString());
                break;
            case R.id.passwordEdTxt:
                view.setPassword(s.toString());
                break;
        }
    }
}
