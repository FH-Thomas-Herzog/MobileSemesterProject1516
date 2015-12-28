package at.fh.ooe.moc5.amazingrace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.adaptor.CheckpointArrayAdapter;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.task.AsyncTaskResult;
import at.fh.ooe.moc5.amazingrace.model.view.CheckpointViewModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.service.ServiceException.ServiceErrorCode;
import at.fh.ooe.moc5.amazingrace.util.DialogUtil;
import at.fh.ooe.moc5.amazingrace.watcher.AnswerButtonTextWatcher;
import at.fh.ooe.moc5.amazingrace.watcher.CheckpointViewModelBindingTextWatcher;

public class CheckpointActivity extends AbstractActivity<CheckpointViewModel> implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String BUNDLE_ROUTE_DETAIL_VIEW = "EXTRA_ROUTE_DETAIL_VIEW";

    //region Activity Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);
        if (application.getLoggedUser() != null) {
            final Intent intent = getIntent();
            if (intent.getExtras().containsKey(AmazingRaceApplication.EXTRA_ROUTE)) {
                RouteModel route = (RouteModel) intent.getExtras().get(AmazingRaceApplication.EXTRA_ROUTE);
                viewModel = new CheckpointViewModel(application.getLoggedUser(), route);
            } else {
                viewModel = (CheckpointViewModel) savedInstanceState.get(BUNDLE_ROUTE_DETAIL_VIEW);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel == null) {
            openInvalidUserAccountDialog();
        } else {
            prepareView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_ROUTE_DETAIL_VIEW, viewModel);
    }
    //endregion

    //region Utilities
    public void prepareView() {
        // set default container visibility
        findViewById(R.id.routeCheckpointContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.routeCheckpointListContainer).setVisibility(View.GONE);

        // prepare list view of visited checkpoints
        final CheckpointArrayAdapter adapter = new CheckpointArrayAdapter(this);
        ListView listView = (ListView) findViewById(R.id.routeCheckpointListView);
        listView.setVisibility(View.VISIBLE);
        adapter.addAll(viewModel.getVisitedCheckpoints());
        listView.setAdapter(adapter);

        // route already finished
        if (viewModel.getNextCheckpoint() == null) {
            findViewById(R.id.routeCheckpointContainerLabel).setVisibility(View.GONE);
            findViewById(R.id.routeCheckpointContainer).setVisibility(View.GONE);
        }
        // checkpoints still open
        else {
            Button answerButton = (Button) findViewById(R.id.answerBtn);
            answerButton.setEnabled(Boolean.FALSE);
            answerButton.setOnClickListener(this);
            ((TextView) findViewById(R.id.nextCheckpointTxt)).setText(viewModel.getNextCheckpoint().getName());
            ((TextView) findViewById(R.id.nextCheckpointHintTxt)).setText(viewModel.getNextCheckpoint().getHint());
            EditText answer = (EditText) findViewById(R.id.hintAnswerEdTxt);
            answer.setText("");
            answer.addTextChangedListener(new CheckpointViewModelBindingTextWatcher(viewModel));
            answer.addTextChangedListener(new AnswerButtonTextWatcher(this, viewModel));
        }
    }
    //endregion

    //region Listeners
    public void toggleAccordion(View view) {
        View checkpointListContainer = findViewById(R.id.routeCheckpointListContainer);
        View checkpointContainer = findViewById(R.id.routeCheckpointContainer);

        switch (view.getId()) {
            case R.id.routeCheckpointContainerLabel:
                checkpointContainer.setVisibility((View.VISIBLE == checkpointContainer.getVisibility()) ? View.GONE : View.VISIBLE);
                checkpointListContainer.setVisibility(View.GONE);
                break;
            case R.id.routeCheckpointListContainerLabel:
                checkpointListContainer.setVisibility((View.VISIBLE == checkpointListContainer.getVisibility()) ? View.GONE : View.VISIBLE);
                checkpointContainer.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Show dialog with checkpount information
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answerBtn:
                onAnswerButtonClick((Button) v);
                break;
        }
    }

    private void onAnswerButtonClick(Button button) {
        new AsyncTask<Object, Object, AsyncTaskResult<Boolean>>() {
            @Override
            protected AsyncTaskResult<Boolean> doInBackground(Object... params) {
                Boolean result = null;
                Exception exception = null;
                try {
                    result = viewModel.validateSecret();
                } catch (Exception e) {
                    exception = e;
                }

                return new AsyncTaskResult<Boolean>(result, exception);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                openProgressDialog(R.string.progress_validating_secred);
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<Boolean> result) {
                super.onPostExecute(result);
                closeProgressDialog();
                if (result.exception != null) {
                    // ServiceException occurred
                    if (result.exception instanceof ServiceException) {
                        handleServiceException(((ServiceException) result.exception));
                    } else {
                        Toast.makeText(CheckpointActivity.this, R.string.error_unknown, Toast.LENGTH_LONG).show();
                    }
                } else if (result.result) {
                    reloadRoute();
                } else {
                    Toast.makeText(CheckpointActivity.this, R.string.info_secret_wrong, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void reloadRoute() {
        new AsyncTask<Object, Object, AsyncTaskResult<Boolean>>() {
            @Override
            protected AsyncTaskResult<Boolean> doInBackground(Object... params) {
                Boolean found = null;
                Exception exception = null;
                try {
                    found = viewModel.reloadRoute();
                } catch (Exception e) {
                    exception = e;
                }
                return new AsyncTaskResult<Boolean>(found, exception);
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<Boolean> result) {
                super.onPostExecute(result);
                closeProgressDialog();
                // Exception occurred
                if (result.exception != null) {
                    if (result.exception instanceof ServiceException) {
                        handleServiceException(((ServiceException) result.exception));
                    } else {
                        Toast.makeText(CheckpointActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                    }
                }
                // Route found
                else if (result.result) {
                    prepareView();
                }
                // Route not found, go back to routes activity
                else {
                    startActivity(new Intent(CheckpointActivity.this, RouteActivity.class));
                    finish();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                openProgressDialog(R.string.progress_updating_route);
            }
        }.execute();
    }
    //endregion
}
