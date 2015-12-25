package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Thomas on 12/25/2015.
 */
public class RouteRequestModel implements Serializable {

    @SerializedName("RouteId")
    private String routeId;

    public RouteRequestModel(String routeId) {
        this.routeId = routeId;
    }
}
