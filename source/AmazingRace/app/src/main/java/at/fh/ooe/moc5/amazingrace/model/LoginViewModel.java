package at.fh.ooe.moc5.amazingrace.model;

import android.app.Service;

import org.apache.commons.lang3.StringUtils;

import at.fh.ooe.moc5.amazingrace.service.RestServiceProxy;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // <editor-fold desc="Actions">
    public boolean loginAction() throws RestServiceProxy.ServiceException {
        return restProxy.checkCredentials(username, password);
    }
    // <editor-fold>
}
