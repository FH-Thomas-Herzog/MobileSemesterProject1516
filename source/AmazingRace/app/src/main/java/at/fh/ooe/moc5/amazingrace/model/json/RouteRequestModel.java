package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Thomas on 12/25/2015.
 */
public class RouteRequestModel extends CredentialsRequestModel {

    @SerializedName("RouteId")
    public final String routeId;

    public RouteRequestModel(CredentialsRequestModel model, String routeId) {
        this(model.userName, model.password, routeId);
    }

    public RouteRequestModel(String username, String password, String routeId) {
        super(username, password);
        this.routeId = routeId;
    }
}
