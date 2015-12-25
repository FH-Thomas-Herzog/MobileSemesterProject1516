package at.fh.ooe.moc5.amazingrace.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.adaptor.CheckpointArrayAdapter;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.view.CheckpointViewModel;
import at.fh.ooe.moc5.amazingrace.watcher.AnswerButtonTextWatcher;
import at.fh.ooe.moc5.amazingrace.watcher.CheckpointViewModelBindingTextWatcher;

public class CheckpointActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private CheckpointViewModel viewModel;
    private AmazingRaceApplication application;

    private static final String BUNDLE_ROUTE_DETAIL_VIEW = "EXTRA_ROUTE_DETAIL_VIEW";

    //region Activity Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (AmazingRaceApplication) getApplication();
        setContentView(R.layout.activity_checkpoint);
        final Intent intent = getIntent();
        if (intent.getExtras().containsKey(AmazingRaceApplication.EXTRA_ROUTE)) {
            RouteModel route = (RouteModel) intent.getExtras().get(AmazingRaceApplication.EXTRA_ROUTE);
            viewModel = new CheckpointViewModel(application.getLoggedUser(), route);
        } else {
            viewModel = (CheckpointViewModel) savedInstanceState.get(BUNDLE_ROUTE_DETAIL_VIEW);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_ROUTE_DETAIL_VIEW, viewModel);
    }
    //endregion

    //region Utilities
    public void prepareView() {
        // prepare list view of visited checkpoints
        final CheckpointArrayAdapter adapter = new CheckpointArrayAdapter(this);
        ListView listView = (ListView) findViewById(R.id.routeListContentList);
        listView.setVisibility(View.VISIBLE);
        adapter.addAll(viewModel.getVisitedCheckpoints());
        listView.setAdapter(adapter);

        // prepare next checkpoint
        ((Button) findViewById(R.id.answerBtn)).setEnabled(Boolean.FALSE);
        ((TextView) findViewById(R.id.nextCheckpointTxt)).setText(viewModel.getNextCheckpoint().getName());
        ((TextView) findViewById(R.id.nextCheckpointHintTxt)).setText(viewModel.getNextCheckpoint().getHint());
        EditText answer = (EditText) findViewById(R.id.hintAnswerEdTxt);
        answer.addTextChangedListener(new CheckpointViewModelBindingTextWatcher(viewModel));
        answer.addTextChangedListener(new AnswerButtonTextWatcher(this, viewModel));
    }
    //endregion

    //region Listeners
    public void toggleAccordion(View view) {
        ListView listView = (ListView) findViewById(R.id.routeListContentList);
        switch (listView.getVisibility()) {
            case View.GONE:
                listView.setVisibility(View.VISIBLE);
                break;
            case View.VISIBLE:
                listView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Show dialog with checkpount information
    }

    @Override
    public void onClick(View v) {

    }
    //endregion
}
