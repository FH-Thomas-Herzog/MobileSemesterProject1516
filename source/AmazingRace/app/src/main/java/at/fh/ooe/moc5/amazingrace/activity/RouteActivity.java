package at.fh.ooe.moc5.amazingrace.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
import at.fh.ooe.moc5.amazingrace.util.DialogUtil;

public class RouteActivity extends AppCompatActivity implements DialogInterface.OnClickListener, AdapterView.OnItemClickListener {

    private RoutesViewModel viewModel;
    private AmazingRaceApplication application;

    private AlertDialog backButtonDialog;
    private AlertDialog invalidUserDialog;
    private ProgressDialog progress;

    //region Activity Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        application = (AmazingRaceApplication) getApplication();
        viewModel = new RoutesViewModel(application.getLoggedUser());
        prepareView(Boolean.TRUE);
    }

    @Override
    public void onBackPressed() {
        backButtonDialog = DialogUtil.createAlertDialog(this, getString(R.string.dialog_title_warning), getString(R.string.warning_want_quit), this);
        backButtonDialog.show();
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
                progress = ProgressDialog.show(RouteActivity.this, getString(R.string.progress_title), getString(R.string.progress_login), Boolean.TRUE);
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<List<RouteModel>> result) {
                super.onPostExecute(result);
                progress.dismiss();
                // Error occurred
                if (result.exception != null) {
                    // ServiceException occurred
                    if (result.exception instanceof RestServiceProxy.ServiceException) {
                        RestServiceProxy.ServiceErrorCode errorCode = ((RestServiceProxy.ServiceException) result.exception).getErrorCode();
                        if (errorCode != null) {
                            switch (errorCode) {
                                case INVALID_REQUEST:
                                    Toast.makeText(RouteActivity.this, R.string.error_request_invalid, Toast.LENGTH_LONG).show();
                                    break;
                                case TIMEOUT:
                                    Toast.makeText(RouteActivity.this, R.string.error_request_timeout, Toast.LENGTH_SHORT).show();
                                    break;
                                case UNKNOWN:
                                    Toast.makeText(RouteActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                                    break;
                                case INVALID_CREDENTIALS:
                                    invalidUserDialog = DialogUtil.createErrorDialog(RouteActivity.this, getString(R.string.error_user_became_invalid), RouteActivity.this);
                                    invalidUserDialog.show();
                                    return;
                            }
                        } else {
                            Toast.makeText(RouteActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RouteActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
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
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (dialog.equals(backButtonDialog)) {
            onBackButtonDialogClick(dialog, which);
        } else if (dialog.equals(invalidUserDialog)) {
            onInvalidUserDialogClick(dialog, which);
        }
    }

    private void onBackButtonDialogClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                finishAffinity();
                break;
        }
    }

    private void onInvalidUserDialogClick(DialogInterface dialog, int which) {
        application.setLoggedUser(null);
        finishAffinity();
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RouteActivity.this, CheckpointActivity.class);
        intent.putExtra(AmazingRaceApplication.EXTRA_ROUTE, ((RouteModel) parent.getItemAtPosition(position)));
        startActivity(intent);
    }
    //endregion
}
