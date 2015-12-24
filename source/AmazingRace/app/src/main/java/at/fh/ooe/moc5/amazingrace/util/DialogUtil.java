package at.fh.ooe.moc5.amazingrace.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.R;

/**
 * Created by Thomas on 12/24/2015.
 */
public class DialogUtil {

    public static Dialog createErrorDialog(final Context ctx, final String message, final DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.error_dialog_title))
                .setMessage(message)
                .setPositiveButton(ctx.getText(R.string.ok_action), listener)
                .create();
    }

    public static Dialog createAlertDialog(final Context ctx, final String title, final String message, final DialogInterface.OnClickListener listener) {
        Objects.requireNonNull(ctx, "Cannot create dialog for null context");
        return new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(ctx.getText(R.string.yes_action), listener)
                .setNegativeButton(ctx.getText(R.string.no_action), listener)
                .create();
    }
}
