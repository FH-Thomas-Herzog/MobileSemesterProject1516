package at.fh.ooe.moc5.amazingrace.model.view;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import at.fh.ooe.moc5.amazingrace.model.json.CredentialsRequestModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceProxy;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.service.ServiceProxyFactory;

/**
 * Created by Thomas on 12/24/2015.
 */
public class LoginViewModel implements Serializable {

    private String username;
    private String password;

    private final ServiceProxy restProxy;

    public LoginViewModel() {
        restProxy = ServiceProxyFactory.createServiceProxy();
    }

    /**
     * Ansers the question if this model is valid whic its is if a username and password are set.
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return (((username != null) && (!StringUtils.isEmpty(username))) && ((password != null) && (!StringUtils.isEmpty(password))));
    }


    //region Actions

    /**
     * Logs the user in by validating the user credentials via the proxy service.
     *
     * @return true if username password are valid, false otherwise
     * @throws ServiceException if an rpoxy service exception occurred
     */
    public UserContextModel loginAction() throws ServiceException {
        final CredentialsRequestModel model = new CredentialsRequestModel(username, password);
        final boolean isValidCredentials = restProxy.validateCredentials(model);
        if (isValidCredentials) {
            return new UserContextModel(model);
        }
        return null;
    }

    /**
     * Resets this model by resetting the username and password
     */
    public void reset() {
        username = null;
        password = null;
    }

    //endregion
    //region Getter and Setter
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    //endregion
}
