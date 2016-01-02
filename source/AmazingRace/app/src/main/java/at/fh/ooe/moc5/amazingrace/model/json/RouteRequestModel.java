package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RouteRequestModel that = (RouteRequestModel) o;
        return Objects.equals(routeId, that.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), routeId);
    }
}
