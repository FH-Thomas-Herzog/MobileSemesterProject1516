package at.fh.ooe.moc5.amazingrace.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.adaptor.RouteArrayAdapter;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.task.AsyncTaskResult;
import at.fh.ooe.moc5.amazingrace.model.view.RoutesViewModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.service.ServiceException.ServiceErrorCode;

public class RouteActivity extends AbstractActivity<RoutesViewModel> implements AdapterView.OnItemClickListener {

    //region Activity Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        if (application.getLoggedUser() != null) {
            viewModel = new RoutesViewModel(application.getLoggedUser());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel == null) {
            openInvalidUserAccountDialog();
        } else {
            prepareView(Boolean.TRUE);
        }
    }

    @Override
    public void onBackPressed() {
        openCloseApplicationDialog();
    }
    //endregion

    //region Utilities
    private void prepareView(boolean reload) {
        ListView listView = (ListView) findViewById(R.id.listRoute);
        final RouteArrayAdapter adapter = new RouteArrayAdapter(RouteActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if (reload) {
            loadItems(adapter);
        } else {
            adapter.addAll(viewModel.getRoutes());
        }
    }

    private void loadItems(final RouteArrayAdapter adapter) {
        Objects.requireNonNull(adapter);
        new AsyncTask<Object, Object, AsyncTaskResult<List<RouteModel>>>() {
            @Override
            protected AsyncTaskResult<List<RouteModel>> doInBackground(Object... params) {
                List<RouteModel> routes = null;
                Exception exception = null;
                try {
                    routes = viewModel.loadRoutes();
                } catch (Exception e) {
                    exception = e;
                }

                return new AsyncTaskResult<List<RouteModel>>(routes, exception);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                openProgressDialog(R.string.progress_loading_routes);
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<List<RouteModel>> result) {
                super.onPostExecute(result);
                closeProgressDialog();
                // Error occurred
                if (result.exception != null) {
                    // ServiceException occurred
                    if (result.exception instanceof ServiceException) {
                        handleServiceException(((ServiceException) result.exception));
                    } else {
                        Toast.makeText(RouteActivity.this, R.string.error_unknown, Toast.LENGTH_LONG).show();
                    }
                }
                // Lists where loaded
                else {
                    adapter.clear();
                    adapter.addAll(result.result);
                }
            }
        }.execute();
    }
    //endregion


    //region Listener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RouteActivity.this, CheckpointActivity.class);
        intent.putExtra(AmazingRaceApplication.EXTRA_ROUTE, ((RouteModel) parent.getItemAtPosition(position)));
        startActivity(intent);
    }
    //endregion
}
