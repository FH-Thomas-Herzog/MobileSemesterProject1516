package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Thomas on 12/25/2015.
 */
public class CredentialsRequestModel implements Serializable {

    @SerializedName("UserName")
    public final String userName;
    @SerializedName("Password")
    public final String password;

    public CredentialsRequestModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String toQueryString() {
        return String.format("userName=%s&password=%s", userName, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredentialsRequestModel that = (CredentialsRequestModel) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
