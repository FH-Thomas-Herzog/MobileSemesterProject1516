package at.fh.ooe.moc5.amazingrace.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.adaptor.CheckpointArrayAdapter;
import at.fh.ooe.moc5.amazingrace.model.json.CheckpointModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.task.AsyncTaskResult;
import at.fh.ooe.moc5.amazingrace.model.view.CheckpointViewModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.watcher.AnswerButtonTextWatcher;
import at.fh.ooe.moc5.amazingrace.watcher.CheckpointViewModelBindingTextWatcher;

public class CheckpointActivity extends AbstractActivity<CheckpointViewModel> implements AdapterView.OnItemClickListener, View.OnClickListener {

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
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (validViewModel) {
            prepareView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MenuGroup.OPTIONS.value, MenuId.RESET.value, 0, R.string.action_reset);
        menu.add(MenuGroup.OPTIONS.value, MenuId.CLOSE.value, 0, R.string.action_close);
        return Boolean.TRUE;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (MenuId.getMenuIdForValue(item.getItemId())) {
            case RESET:
                resetRoute();
                return Boolean.TRUE;
            case CLOSE:
                openCloseApplicationDialog();
                return Boolean.TRUE;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    //region Utilities
    public void prepareView() {
        // set default container visibility
        findViewById(R.id.routeCheckpointContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.routeCheckpointMapContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.routeCheckpointListContainer).setVisibility(View.GONE);

        // prepare list view of visited checkpoints
        LinearLayout listViewReplacement = (LinearLayout) findViewById(R.id.routeCheckpointListView);
        listViewReplacement.removeAllViews();
        listViewReplacement.setVisibility(View.VISIBLE);
        for (CheckpointModel model : viewModel.getRoute().getVisitedCheckpoints()) {
            View view = View.inflate(CheckpointActivity.this, R.layout.view_checkpoint_item, null);
            ((TextView) view.findViewById(R.id.visitedCheckpointNameLabel)).setText(model.getName());
            listViewReplacement.addView(view);
        }
        if (viewModel.getNextCheckpoint() != null) {
            final CheckpointModel model = viewModel.getNextCheckpoint();
            View view = View.inflate(CheckpointActivity.this, R.layout.view_checkpoint_item, null);
            ((TextView) view.findViewById(R.id.visitedCheckpointNameLabel)).setText(new StringBuilder(model.getName()).append(" (").append(getText(R.string.open)).append(")").toString());
            listViewReplacement.addView(view);
        }

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

        // Prepare maps
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.routeCheckpointMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.clear();
                map.getUiSettings().setZoomControlsEnabled(Boolean.TRUE);
                map.getUiSettings().setAllGesturesEnabled(Boolean.TRUE);
                map.getUiSettings().setCompassEnabled(Boolean.TRUE);
                LatLng zoomLocation = null;
                PolylineOptions lineOptions = null;
                // Get visited points
                if (!viewModel.getRoute().getVisitedCheckpoints().isEmpty()) {
                    lineOptions = new PolylineOptions();
                    for (int i = 0; i < viewModel.getRoute().getVisitedCheckpoints().size(); i++) {
                        CheckpointModel model = viewModel.getRoute().getVisitedCheckpoints().get(i);
                        LatLng location = new LatLng(model.getLatitude(), model.getLongitude());
                        map.addMarker(new MarkerOptions().position(location)
                                        .title(model.getName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        ).showInfoWindow();
                        lineOptions.add(location);
                        // Remember last for focusing on it
                        if (i == (viewModel.getVisitedCheckpoints().size() - 1)) {
                            zoomLocation = location;
                        }
                    }
                    map.addPolyline(lineOptions.color(Color.GREEN));
                }
                if (viewModel.getRoute().getNextCheckpoint() != null) {
                    zoomLocation = new LatLng(viewModel.getRoute().getNextCheckpoint().getLatitude(), viewModel.getRoute().getNextCheckpoint().getLongitude());
                    map.addMarker(new MarkerOptions().position(zoomLocation)
                                    .title(viewModel.getNextCheckpoint().getName())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    ).showInfoWindow();
                    // If visited checkpoints exist
                    if (!viewModel.getRoute().getVisitedCheckpoints().isEmpty()) {
                        map.addPolyline(new PolylineOptions().add(lineOptions.getPoints().get(lineOptions.getPoints().size() - 1), zoomLocation).color(Color.RED));
                    }
                }

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomLocation, 17));
            }
        });
    }
    //endregion

    //region Listeners

    public void toggleAccordion(View view) {
        View checkpointListContainer = findViewById(R.id.routeCheckpointListContainer);
        View checkpointMapContainer = findViewById(R.id.routeCheckpointMapContainer);

        switch (((View) view.getParent()).getId()) {
            case R.id.routeCheckpointListContainerLabel:
                checkpointListContainer.setVisibility((View.VISIBLE == checkpointListContainer.getVisibility()) ? View.GONE : View.VISIBLE);
                break;
            case R.id.routeCheckpointMapContainerLabel:
                checkpointMapContainer.setVisibility((View.VISIBLE == checkpointMapContainer.getVisibility()) ? View.GONE : View.VISIBLE);
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
                    Toast.makeText(CheckpointActivity.this, R.string.info_secret_ok, Toast.LENGTH_LONG).show();
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

    private void resetRoute() {
        new AsyncTask<Object, Object, AsyncTaskResult<Boolean>>() {
            @Override
            protected AsyncTaskResult<Boolean> doInBackground(Object... params) {
                Boolean result = null;
                Exception exception = null;
                try {
                    result = viewModel.resetRoute();
                } catch (Exception e) {
                    exception = e;
                }

                return new AsyncTaskResult<Boolean>(result, exception);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                openProgressDialog(R.string.progress_resetting_routes);
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<Boolean> result) {
                super.onPostExecute(result);
                closeProgressDialog();
                // Error occurred
                if (result.exception != null) {
                    // ServiceException occurred
                    if (result.exception instanceof ServiceException) {
                        handleServiceException(((ServiceException) result.exception));
                    } else {
                        Toast.makeText(CheckpointActivity.this, R.string.error_unknown, Toast.LENGTH_LONG).show();
                    }
                }
                // Reset failed on rest method
                else if (!result.result) {
                    Toast.makeText(CheckpointActivity.this, R.string.error_route_reset_failed, Toast.LENGTH_LONG).show();
                }
                // Reset ok
                else {
                    reloadRoute();
                }
            }
        }.execute();
    }
    //endregion
}
