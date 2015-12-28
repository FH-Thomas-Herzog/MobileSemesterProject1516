package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Thomas on 12/25/2015.
 */
public class RouteModel implements Serializable {

    @SerializedName("Id")
    private String id;
    @SerializedName("Name")
    private String name;
    @SerializedName("VisitedCheckpoints")
    private List<CheckpointModel> visitedCheckpoints;
    @SerializedName("NextCheckpoint")
    private CheckpointModel nextCheckpoint;

    public RouteModel() {
    }

    //region Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CheckpointModel> getVisitedCheckpoints() {
        return visitedCheckpoints;
    }

    public void setVisitedCheckpoints(List<CheckpointModel> visitedCheckpoints) {
        this.visitedCheckpoints = visitedCheckpoints;
    }

    public CheckpointModel getNextCheckpoint() {
        return nextCheckpoint;
    }

    public void setNextCheckpoint(CheckpointModel nextCheckpoint) {
        this.nextCheckpoint = nextCheckpoint;
    }
    //endregion

    //region Utilities
    public boolean isDone() {
        return nextCheckpoint == null;
    }
    //endregion

    public String toItemString(final String finishedString, final String doneString) {
        Objects.requireNonNull(finishedString);
        Objects.requireNonNull(doneString);

        final StringBuilder sb = new StringBuilder(name).append(" (");
        if (isDone()) {
            sb.append(finishedString);
        } else {
            sb.append(visitedCheckpoints.size()).append("x").append(doneString);
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteModel that = (RouteModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
