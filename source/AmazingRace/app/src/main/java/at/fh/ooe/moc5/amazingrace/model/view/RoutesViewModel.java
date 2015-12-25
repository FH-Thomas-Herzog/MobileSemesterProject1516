package at.fh.ooe.moc5.amazingrace.model.view;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
import at.fh.ooe.moc5.amazingrace.service.ServiceFactory;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;

/**
 * Created by Thomas on 12/25/2015.
 */
public class RoutesViewModel implements Serializable {

    private UserContextModel userContext;
    private RouteModel selectedRoute;
    private List<RouteModel> routes;

    private RestServiceProxy proxy;

    public RoutesViewModel(UserContextModel userContext) {
        Objects.requireNonNull(userContext, "UserContext must not be null");
        this.userContext = userContext;
        proxy = ServiceFactory.createRestServiceProxy();
    }

    //region
    public List<RouteModel> loadRoutes() throws ServiceException {
        routes = proxy.getRoutes(userContext.getCredentialsModel());
        return routes;
    }

    //endregion
    //region Getter and Setter
    public RouteModel getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(RouteModel selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public List<RouteModel> getRoutes() {
        return routes;
    }
    //endregion
}
