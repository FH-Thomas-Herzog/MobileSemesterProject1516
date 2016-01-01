package at.fh.ooe.moc5.amazingrace.adaptor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.model.json.CheckpointModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;

/**
 * Created by Thomas on 12/25/2015.
 */
public class CheckpointArrayAdapter extends ArrayAdapter<CheckpointModel> {

    public CheckpointArrayAdapter(Context ctx) {
        super(ctx, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.view_checkpoint_item, null);
        }

        final CheckpointModel checkpoint = getItem(position);
        final String label = (checkpoint.isUnvisited()) ? (new StringBuilder(checkpoint.getName()).append(" (").append(getContext().getText(R.string.open)).append(")").toString()) : checkpoint.getName();
        ((TextView) convertView.findViewById(R.id.visitedCheckpointNameLabel)).setText(label);

        return convertView;
    }
}
