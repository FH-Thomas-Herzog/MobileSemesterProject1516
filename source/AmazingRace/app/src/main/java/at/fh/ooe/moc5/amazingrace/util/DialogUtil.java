package at.fh.ooe.moc5.amazingrace.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.R;

/**
 * Created by Thomas on 12/24/2015.
 * Utility for creating dialogs.
 */
public class DialogUtil {

    /**
     * Creates a error alert dialog.
     *
     * @param ctx      the context to create the alert dialog for
     * @param message  the resource id of the alert dialog error message
     * @param listener the button listener
     * @return the created alert dialog
     */
    public static AlertDialog createErrorDialog(final Context ctx, final int message, final DialogInterface.OnClickListener listener) {
        Objects.requireNonNull(ctx, "Cannot create dialog for null context");
        Objects.requireNonNull(listener, "Listener for buttons mut be given");

        return new AlertDialog.Builder(ctx)
                .setTitle(R.string.dialog_title_error)
                .setMessage(message)
                .setPositiveButton(R.string.action_ok, listener)
                .create();
    }

    /**
     * Creates a yes, no alert dialog
     *
     * @param ctx      the context to create the alert dialog for
     * @param title    the resource id of the alert dialog title
     * @param message  the resource id of the alert dialog message
     * @param listener the yes, no listener
     * @return the created alert dialog
     */
    public static AlertDialog createYesNoAlertDialog(final Context ctx, final int title, final int message, final DialogInterface.OnClickListener listener) {
        Objects.requireNonNull(ctx, "Cannot create dialog for null context");
        Objects.requireNonNull(listener, "Listener for buttons mut be given");

        return new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.action_yes, listener)
                .setNegativeButton(R.string.action_no, listener)
                .create();
    }

    /**
     * Creates a alert dialog with an custom view as content.
     *
     * @param ctx      the context to create dialog for
     * @param viewId   the resource id of the view
     * @param title    the resource id of the alert dialog title
     * @param listener the button listener
     * @return the created alert dialog
     */
    public static AlertDialog createCustomContentDialog(final Context ctx, final int viewId, final int title, final DialogInterface.OnClickListener listener) {
        Objects.requireNonNull(ctx, "Cannot create dialog for null context");
        Objects.requireNonNull(listener, "Listener for buttons mut be given");

        return new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setView(viewId)
                .setPositiveButton(R.string.action_ok, listener)
                .create();
    }
}
