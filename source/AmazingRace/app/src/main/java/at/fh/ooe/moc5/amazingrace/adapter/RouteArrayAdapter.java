package at.fh.ooe.moc5.amazingrace.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import at.fh.ooe.moc5.amazingrace.R;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;

/**
 * Created by Thomas on 12/25/2015.
 * The adapter for the route items
 */
public class RouteArrayAdapter extends ArrayAdapter<RouteModel> {

    public RouteArrayAdapter(Context ctx) {
        super(ctx, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.view_route_item, null);
        }

        RouteModel route = getItem(position);
        ImageView icon = ((ImageView) convertView.findViewById(R.id.routeItemImage));
        if (route.getNextCheckpoint() == null) {
            icon.setImageResource(R.drawable.done_icon);
        } else {
            icon.setImageResource(R.drawable.app_icon);
        }
        ((TextView) convertView.findViewById(R.id.routeItemText)).setText(route.toItemString(getContext().getString(R.string.done)));

        return convertView;
    }
}
