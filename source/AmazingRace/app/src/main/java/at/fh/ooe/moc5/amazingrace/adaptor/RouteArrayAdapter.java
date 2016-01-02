package at.fh.ooe.moc5.amazingrace.adaptor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Objects;

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
        ((TextView) convertView.findViewById(R.id.routeNameLabel)).setText(route.toItemString(getContext().getString(R.string.finished), getContext().getString(R.string.done)));

        return convertView;
    }
}
