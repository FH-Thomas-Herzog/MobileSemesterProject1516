package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Thomas on 12/25/2015.
 */
public class CheckpointRequestModel extends CredentialsRequestModel implements Serializable {

    @SerializedName("CheckpointId")
    public final String checkpointId;
    @SerializedName("Secret")
    public final String secret;

    public CheckpointRequestModel(String username, String password, String checkpointId, String secret) {
        super(username, password);
        this.checkpointId = checkpointId;
        this.secret = secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckpointRequestModel that = (CheckpointRequestModel) o;
        return Objects.equals(checkpointId, that.checkpointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), checkpointId);
    }
}
