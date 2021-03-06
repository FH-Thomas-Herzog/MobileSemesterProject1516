package at.fh.ooe.moc5.amazingrace.model.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Thomas on 12/25/2015.
 */
public class CheckpointModel implements Serializable {

    @SerializedName("Id")
    private String id;
    @SerializedName("Number")
    private int number;
    @SerializedName("Name")
    private String name;
    @SerializedName("Hint")
    private String hint;
    @SerializedName("Latitude")
    private double latitude;
    @SerializedName("Longitude")
    private double longitude;
    private transient boolean unvisited = Boolean.FALSE;

    public CheckpointModel() {
    }

    //region Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isUnvisited() {
        return unvisited;
    }

    public void setUnvisited(boolean unvisited) {
        this.unvisited = unvisited;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckpointModel that = (CheckpointModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
