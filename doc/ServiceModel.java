package moc5.amazingrace;

import com.google.gson.annotations.SerializedName;

class Route {
	@SerializedName("Id")
	private String id;

	public String getId() {
		return id;
	}

	@SerializedName("Name")
	private String name;

	public String getName() {
		return name;
	}

	@SerializedName("VisitedCheckpoints")
	private Checkpoint[] visitedCheckpoints;

	public Checkpoint[] getVisitedCheckpoints() {
		return visitedCheckpoints;
	}

	@SerializedName("NextCheckpoint")
	private Checkpoint nextCheckpoint;

	public Checkpoint getNextCheckpoint() {
		return nextCheckpoint;
	}
}

class Checkpoint {
	@SerializedName("Id")
	private String id;

	public String getId() {
		return id;
	}

	@SerializedName("Number")
	private int number;

	public int getNumber() {
		return number;
	}

	@SerializedName("Name")
	private String name;

	public String getName() {
		return name;
	}

	@SerializedName("Hint")
	private String hint;

	public String getHint() {
		return hint;
	}

	@SerializedName("Latitude")
	private double latitude;

	public double getLatitude() {
		return latitude;
	}

	@SerializedName("Longitude")
	private double longitude;

	public double getLongitude() {
		return longitude;
	}
}

class Request {
	@SerializedName("UserName")
	private String userName;

	public string getUserName() {
		return userName;
	}

	@SerializedName("Password")
	private String password;

	public string getPassword() {
		return password;
	}
}

class CheckpointRequest : Request {
	@SerializedName("CheckpointId")
	private String checkpointId;

	public String getCheckpointId() {
		return checkpointId;
	}

	@SerializedName("Secret")
	private String secret;

	public string getSecret() {
		return secret;
	}
}

class RouteRequest : Request {
	@SerializedName("RouteId")
	private String routeId;

	public String getRouteId() {
		return routeId;
	}
}