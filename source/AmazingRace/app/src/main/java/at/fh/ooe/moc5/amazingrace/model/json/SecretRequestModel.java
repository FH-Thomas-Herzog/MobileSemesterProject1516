package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Thomas on 12/25/2015.
 */
public class SecretRequestModel extends CredentialsRequestModel implements Serializable {

    @SerializedName("CheckpointId")
    public final String checkpointId;
    @SerializedName("Secret")
    public final String secret;

    public SecretRequestModel(String username, String password, String checkpointId, String secret) {
        super(username, password);
        this.checkpointId = checkpointId;
        this.secret = secret;
    }
}
