package at.fh.ooe.moc5.amazingrace.model.view;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.model.json.CheckpointModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.json.SecretRequestModel;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.service.ServiceFactory;

/**
 * Created by Thomas on 12/25/2015.
 */
public class CheckpointViewModel implements Serializable {

    private String answer;
    private final UserContextModel userContext;
    private final RouteModel route;

    private RestServiceProxy proxy;

    public CheckpointViewModel(UserContextModel userContext, RouteModel route) {
        Objects.requireNonNull(userContext, "View model needs user context set");
        Objects.requireNonNull(route, "View model needs route set");

        this.userContext = userContext;
        this.route = route;
        proxy = ServiceFactory.createRestServiceProxy();
    }

    //region Actions
    public boolean validateSecret() throws ServiceException {
        if (!isValid()) {
            return Boolean.FALSE;
        }

        return proxy.visitCheckpoint(new SecretRequestModel(userContext.getCredentialsModel().userName, userContext.getCredentialsModel().password, getNextCheckpoint().getId(), answer));
    }

    public void reloadRoute() {

    }

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
