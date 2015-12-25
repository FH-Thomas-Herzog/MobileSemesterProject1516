package at.fh.ooe.moc5.amazingrace.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.R;

/**
 * Created by Thomas on 12/24/2015.
 */
public class DialogUtil {

    public static AlertDialog createErrorDialog(final Context ctx, final String message, final DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.dialog_title_error))
                .setMessage(message)
                .setPositiveButton(ctx.getText(R.string.action_ok), listener)
                .create();
    }

    public static AlertDialog createAlertDialog(final Context ctx, final String title, final String message, final DialogInterface.OnClickListener listener) {
        Objects.requireNonNull(ctx, "Cannot create dialog for null context");
        return new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(ctx.getText(R.string.action_yes), listener)
                .setNegativeButton(ctx.getText(R.string.action_no), listener)
                .create();
    }
}
