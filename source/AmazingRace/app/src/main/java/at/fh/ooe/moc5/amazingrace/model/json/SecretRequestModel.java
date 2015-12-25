package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thomas on 12/25/2015.
 */
public class SecretRequestModel {

    @SerializedName("CheckpointId")
    private final String checkpointId;
    @SerializedName("Secret")
    private final String secret;

    public SecretRequestModel(String checkpointId, String secret) {
        this.checkpointId = checkpointId;
        this.secret = secret;
    }
}
