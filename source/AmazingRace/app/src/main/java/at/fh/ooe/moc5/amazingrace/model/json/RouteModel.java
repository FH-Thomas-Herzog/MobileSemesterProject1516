package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by Thomas on 12/25/2015.
 */
public class RouteModel {

    @SerializedName("Id")
    private String id;
    @SerializedName("Name")
    private String name;
    @SerializedName("VisitedCheckpoints")
    private CheckpointModel[] visitedCheckpoints;
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

    public CheckpointModel[] getVisitedCheckpoints() {
        return visitedCheckpoints;
    }

    public void setVisitedCheckpoints(CheckpointModel[] visitedCheckpoints) {
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
            sb.append(visitedCheckpoints.length).append("x").append(doneString);
        }
        sb.append(")");
        return sb.toString();
    }
}
