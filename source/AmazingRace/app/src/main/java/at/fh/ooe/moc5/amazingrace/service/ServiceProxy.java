package at.fh.ooe.moc5.amazingrace.service;

import java.io.Serializable;
import java.util.List;

import at.fh.ooe.moc5.amazingrace.model.json.CredentialsRequestModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteRequestModel;
import at.fh.ooe.moc5.amazingrace.model.json.CheckpointRequestModel;

/**
 * Created by Thomas on 12/25/2015.
 * The specification of the service proxy
 */
public interface ServiceProxy extends Serializable {

    /**
     * Validates the given user credentials.
     *
     * @param model the model containing authentication data
     * @return true if the credentials are valid, false otherwise
     * @throws ServiceException if an service error occurs. See contained error code for details
     */
    boolean validateCredentials(CredentialsRequestModel model) throws ServiceException;

    /**
     * Validates the checkpoint secret.
     *
     * @param model the model containing the checkpoint data and authentication data
     * @return true if valid, false otherwise
     * @throws ServiceException if an service error occurs. See contained error code for details
     */
    boolean validateCheckpointSecret(CheckpointRequestModel model) throws ServiceException;

    /**
     * Loads all routes.
     *
     * @param model the credentials model for authentication
     * @return the loaded routes
     * @throws ServiceException if an service error occurs. See contained error code for details
     */
    List<RouteModel> loadRoutes(CredentialsRequestModel model) throws ServiceException;

    /**
     * Resets the given route.
     *
     * @param model the model containing the route data and authentication data
     * @return true if reset, false otherwise
     * @throws ServiceException if an service error occurs. See contained error code for details
     */
    boolean resetRoute(RouteRequestModel model) throws ServiceException;

    /**
     * Resets all routes.
     *
     * @param model the model containing the authentication data
     * @return true if reset, false otherwise
     * @throws ServiceException if an service error occurs. See contained error code for details
     */
    boolean resetAllRoutes(CredentialsRequestModel model) throws ServiceException;
}
