package at.fh.ooe.moc5.amazingrace.model.view;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.model.json.CheckpointModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;

/**
 * Created by Thomas on 12/25/2015.
 */
public class CheckpointViewModel implements Serializable {

    private String answer;
    private final UserContextModel userContext;
    private final RouteModel route;

    public CheckpointViewModel(UserContextModel userContext, RouteModel route) {
        Objects.requireNonNull(userContext, "View model needs user context set");
        Objects.requireNonNull(route, "View model needs route set");

        this.userContext = userContext;
        this.route = route;
    }

    //region Actions
    public boolean isValid() {
        return (answer != null) && (!answer.trim().isEmpty());
    }

    //endregion
    //region Getter and Setter
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public UserContextModel getUserContext() {
        return userContext;
    }

    public RouteModel getRoute() {
        return route;
    }

    public CheckpointModel getNextCheckpoint() {
        return route.getNextCheckpoint();
    }

    public List<CheckpointModel> getVisitedCheckpoints() {
        return route.getVisitedCheckpoints();
    }
    //endregion
}
