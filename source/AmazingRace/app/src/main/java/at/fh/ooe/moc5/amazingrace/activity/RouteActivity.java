package at.fh.ooe.moc5.amazingrace.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.AmazingRaceApplication;
import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.adaptor.RouteArrayAdapter;
import at.fh.ooe.moc5.amazingrace.model.json.CheckpointModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.task.AsyncTaskResult;
import at.fh.ooe.moc5.amazingrace.model.view.RoutesViewModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;

public class RouteActivity extends AbstractActivity<RoutesViewModel> implements AdapterView.OnItemClickListener {

    //region Activity Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        if (application.getLoggedUser() != null) {
            viewModel = new RoutesViewModel(application.getLoggedUser());
        }
        // Register route list for context menu
        registerForContextMenu(findViewById(R.id.listRoute));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (validViewModel) {
            prepareView(Boolean.TRUE);
        }
    }

    @Override
    public void onBackPressed() {
        openCloseApplicationDialog();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //
        if (R.id.listRoute == v.getId()) {
            onCreateRouteListContextMenu(menu, (ListView) v, (AdapterView.AdapterContextMenuInfo) menuInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MenuGroup.OPTIONS.value, MenuId.RESET_ALL_ROUTES.value, 0, R.string.action_reset_all);
        menu.add(MenuGroup.OPTIONS.value, MenuId.CLOSE.value, 0, R.string.action_close);
        return Boolean.TRUE;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (MenuId.getMenuIdForValue(item.getItemId())) {
            case RESET_ALL_ROUTES:
                resetRoute(null);
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
    private void onCreateRouteListContextMenu(ContextMenu menu, ListView v,
                                              AdapterView.AdapterContextMenuInfo menuInfo) {
        RouteModel route = (RouteModel) v.getItemAtPosition(menuInfo.position);
        if (route.getNextCheckpoint() != null) {
            menu.add(MenuGroup.ROUTE_ITEM.value, MenuId.PLAY.value, 0, R.string.action_play);
            if (!route.getVisitedCheckpoints().isEmpty()) {
                menu.add(MenuGroup.ROUTE_ITEM.value, MenuId.RESET.value, 1, R.string.action_reset);
            }
        } else {
            menu.add(MenuGroup.ROUTE_ITEM.value, MenuId.RESET.value, 0, R.string.action_reset);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (MenuGroup.getMenuGroupForValue(item.getGroupId())) {
            case ROUTE_ITEM:
                return handleRouteItemSelected(item);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private boolean handleRouteItemSelected(MenuItem item) {
        final RouteModel model = (RouteModel) ((ListView) findViewById(R.id.listRoute)).getItemAtPosition(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        switch (MenuId.getMenuIdForValue(item.getItemId())) {
            case PLAY:
                goToCheckpointActivity(model);
                return Boolean.TRUE;
            case RESET:
                resetRoute(model);
                return Boolean.TRUE;
            default:
                return Boolean.FALSE;
        }
    }


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

    private void resetRoute(final RouteModel model) {
        new AsyncTask<Object, Object, AsyncTaskResult<Boolean>>() {
            @Override
            protected AsyncTaskResult<Boolean> doInBackground(Object... params) {
                Boolean result = null;
                Exception exception = null;
                try {
                    if (model != null) {
                        result = viewModel.resetRoute(model);
                    } else {
                        result = viewModel.resetAllRoutes();
                    }
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
                        Toast.makeText(RouteActivity.this, R.string.error_unknown, Toast.LENGTH_LONG).show();
                    }
                }
                // Reset failed on rest method
                else if (!result.result) {
                    Toast.makeText(RouteActivity.this, R.string.error_route_reset_failed, Toast.LENGTH_LONG).show();
                }
                // Reset ok
                else {
                    prepareView(Boolean.TRUE);
                }
            }
        }.execute();
    }

    private void goToCheckpointActivity(RouteModel model) {
        Intent intent = new Intent(RouteActivity.this, CheckpointActivity.class);
        intent.putExtra(AmazingRaceApplication.EXTRA_ROUTE, model);
        startActivity(intent);
    }
    //endregion


    //region Listener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        goToCheckpointActivity(((RouteModel) parent.getItemAtPosition(position)));
    }
    //endregion
}
