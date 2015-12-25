package at.fh.ooe.moc5.amazingrace.watcher;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.model.view.CheckpointViewModel;

/**
 * Created by Thomas on 12/24/2015.
 */
public class CheckpointViewModelBindingTextWatcher implements TextWatcher {

    private final CheckpointViewModel view;

    public CheckpointViewModelBindingTextWatcher(CheckpointViewModel view) {
        Objects.requireNonNull(view);
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        view.setAnswer(s.toString());
    }
}
