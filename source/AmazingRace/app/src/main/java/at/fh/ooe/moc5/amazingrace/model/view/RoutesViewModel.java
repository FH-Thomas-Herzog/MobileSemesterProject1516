package at.fh.ooe.moc5.amazingrace.model.view;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteRequestModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceProxy;
import at.fh.ooe.moc5.amazingrace.service.ServiceProxyFactory;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;

/**
 * Created by Thomas on 12/25/2015.
 */
public class RoutesViewModel implements Serializable {

    private UserContextModel userContext;
    private RouteModel selectedRoute;
    private List<RouteModel> routes;

    private ServiceProxy proxy;

    public RoutesViewModel(UserContextModel userContext) {
        Objects.requireNonNull(userContext, "UserContext must not be null");
        this.userContext = userContext;
        proxy = ServiceProxyFactory.createServiceProxy();
    }

    //region Actions

    /**
     * Loads the routes
     *
     * @return the found routes
     * @throws ServiceException if the proxy service throw an exception
     */
    public List<RouteModel> loadRoutes() throws ServiceException {
        routes = proxy.loadRoutes(userContext.getCredentialsModel());
        Collections.sort(routes, new Comparator<RouteModel>() {
            @Override
            public int compare(RouteModel lhs, RouteModel rhs) {
                if (lhs.isDone() == rhs.isDone()) {
                    if (lhs.getVisitedCheckpoints().size() == rhs.getVisitedCheckpoints().size()) {
                        return lhs.getName().toUpperCase().compareTo(rhs.getName().toUpperCase());
                    } else {
                        return Integer.valueOf(rhs.getVisitedCheckpoints().size())
                                .compareTo(lhs.getVisitedCheckpoints().size());
                    }
                } else {
                    return Boolean.valueOf(lhs.isDone()).compareTo(rhs.isDone());
                }
            }
        });
        return routes;
    }

    /**
     * Resets the given route
     *
     * @param model the route to reset
     * @return true if reset, false otherwise
     * @throws ServiceException if the proxy service throw an exception
     */
    public boolean resetRoute(RouteModel model) throws ServiceException {
        return proxy.resetRoute(new RouteRequestModel(userContext.getCredentialsModel(), model.getId()));
    }

    /**
     * Resets all routes
     *
     * @return true if reset, false otherwise
     * @throws ServiceException if the proxy service throw an exception
     */
    public boolean resetAllRoutes() throws ServiceException {
        return proxy.resetAllRoutes(userContext.getCredentialsModel());
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
