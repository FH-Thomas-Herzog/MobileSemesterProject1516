package at.fh.ooe.moc5.amazingrace.activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.util.DialogUtil;

public class RouteActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
    }

    @Override
    public void onBackPressed() {
        DialogUtil.createAlertDialog(this, getString(R.string.warning_dialog_title), getString(R.string.warning_want_quit), this).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // Do nothing beacuse user wants to stay in the app
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                finishAffinity();
                break;
        }
    }
}
