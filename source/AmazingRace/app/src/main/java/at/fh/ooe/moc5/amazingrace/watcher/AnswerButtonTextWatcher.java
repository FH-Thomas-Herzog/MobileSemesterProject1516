package at.fh.ooe.moc5.amazingrace.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.activity.CheckpointActivity;
import at.fh.ooe.moc5.amazingrace.model.view.CheckpointViewModel;

/**
 * Created by Thomas on 12/24/2015.
 * A watcher which enables the answer button if the backed view model is valid, disables it otherwise
 */
public class AnswerButtonTextWatcher implements TextWatcher {

    private final CheckpointViewModel viewModel;
    private final CheckpointActivity activity;

    public AnswerButtonTextWatcher(CheckpointActivity activity, CheckpointViewModel viewModel) {
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
        ((Button) activity.findViewById(R.id.answerBtn)).setEnabled(viewModel.isValid());
    }
}
