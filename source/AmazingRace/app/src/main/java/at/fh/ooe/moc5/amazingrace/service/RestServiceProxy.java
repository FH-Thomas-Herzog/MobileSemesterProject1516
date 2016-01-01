package at.fh.ooe.moc5.amazingrace.service;

import java.io.Serializable;
import java.util.List;

import at.fh.ooe.moc5.amazingrace.model.json.CredentialsRequestModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteRequestModel;
import at.fh.ooe.moc5.amazingrace.model.json.SecretRequestModel;

/**
 * Created by Thomas on 12/25/2015.
 */
public interface RestServiceProxy extends Serializable {
    boolean checkCredentials(CredentialsRequestModel model) throws ServiceException;

    boolean visitCheckpoint(SecretRequestModel model) throws ServiceException;

    List<RouteModel> getRoutes(CredentialsRequestModel model) throws ServiceException;

    boolean resetRoute(RouteRequestModel model) throws ServiceException;

    boolean resetAllRoutes(CredentialsRequestModel model) throws ServiceException;
}
