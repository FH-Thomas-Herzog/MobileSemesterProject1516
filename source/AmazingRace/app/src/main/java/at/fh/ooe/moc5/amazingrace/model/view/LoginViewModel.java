package at.fh.ooe.moc5.amazingrace.model.view;

import org.apache.commons.lang3.StringUtils;

import at.fh.ooe.moc5.amazingrace.model.json.CredentialsRequestModel;
import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
import at.fh.ooe.moc5.amazingrace.service.ServiceException;
import at.fh.ooe.moc5.amazingrace.service.ServiceFactory;

/**
 * Created by Thomas on 12/24/2015.
 */
public class LoginViewModel {

    private String username;
    private String password;

    private final RestServiceProxy restProxy;

    public LoginViewModel() {
        restProxy = ServiceFactory.createRestServiceProxy();
    }

    public boolean isValid() {
        return (((username != null) && (!StringUtils.isEmpty(username))) && ((password != null) && (!StringUtils.isEmpty(password))));
    }


    //region Actions
    public UserContextModel loginAction() throws ServiceException {
        final CredentialsRequestModel model = new CredentialsRequestModel(username, password);
        final boolean isValidCredentials = restProxy.checkCredentials(model);
        if (isValidCredentials) {
            return new UserContextModel(model);
        }
        return null;
    }

    public void reset() {
        username = null;
        password = null;
    }

    //endregion
    //region Getter and Setter
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //endregion
}
