package at.fh.ooe.moc5.amazingrace.model.view;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import at.fh.ooe.moc5.amazingrace.model.json.CredentialsRequestModel;

/**
 * Created by Thomas on 12/24/2015.
 * This model represents an logged user for the application.
 */
public class UserContextModel implements Serializable {

    private final CredentialsRequestModel model;
    private final Calendar loginDate;

    public UserContextModel(CredentialsRequestModel model) {
        Objects.requireNonNull(model);
        this.model = model;
        this.loginDate = Calendar.getInstance();
    }

    //region Getter and Setter
    public CredentialsRequestModel getCredentialsModel() {
        return model;
    }

    public Calendar getLoginDate() {
        return loginDate;
    }
    //endregion
}
