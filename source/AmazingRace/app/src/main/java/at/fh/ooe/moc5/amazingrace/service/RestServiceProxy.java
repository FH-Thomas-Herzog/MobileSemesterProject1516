package at.fh.ooe.moc5.amazingrace.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import at.fh.ooe.moc5.amazingrace.model.json.CredentialsRequestModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;

/**
 * Created by Thomas on 12/24/2015.
 * <p/>
 * Class for communicating with the rest backend.
 */
public class RestServiceProxy {

    public static final String REST_URL = "https://demo.nexperts.com/MOC5/AmazingRaceService/AmazingRaceService.svc";
    public static final String CHECK_CREDENTIALS = "/CheckCredentials";
    public static final String GET_ROUTES_METHOD = "/GetRoutes";
    public static final String VISITED_POINTS = "/InformAboutVisitedCheckpoint";
    public static final String RESET_ALL_ROUTES_METHOD = "/ResetAllRoutes";
    public static final String RESET_ROUTE_METHOD = "/ResetRoute";

    public static final int DEFAULT_TIME_OUT = 3000;

    /**
     * Checks the credentials if they map to an valid user.
     *
     * @param model the model holding the user credentials
     * @return true if the credentials map to an valid user
     * @throws ServiceException if the request failed.
     * @see at.fh.ooe.moc5.amazingrace.service.RestServiceProxy.ServiceErrorCode for the ServiceException contained error codes
     */
    public boolean checkCredentials(CredentialsRequestModel model) throws ServiceException {
        Objects.requireNonNull(model, "Cannot check credentials for null model");

        try {
            URL url = new URL(new StringBuilder(REST_URL).append(CHECK_CREDENTIALS).append("?").append(model.toQueryString()).toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(DEFAULT_TIME_OUT);
            connection.setUseCaches(Boolean.FALSE);

            final String response = readResponse(connection);
            if ((!"true".equals(response)) && (!"false".equals(response))) {
                throw new ServiceException(ServiceErrorCode.INVALID_REQUEST);
            }
            return Boolean.parseBoolean(response);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ServiceErrorCode.UNKNOWN, e);
        }
    }

    /**
     * Gets the available routes for the given user.
     *
     * @param model the model holding the user credentials
     * @return the list of found routes
     * @throws ServiceException if an error occurs during the request
     * @see at.fh.ooe.moc5.amazingrace.service.RestServiceProxy.ServiceErrorCode for the ServiceException contained error code
     */
    public List<RouteModel> getRoutes(CredentialsRequestModel model) throws ServiceException {
        Objects.requireNonNull(model, "Cannot get routes with missing credentials");

        if (!checkCredentials(model)) {
            throw new ServiceException(ServiceErrorCode.INVALID_CREDENTIALS);
        }

        try {
            URL url = new URL(new StringBuilder(REST_URL).append(GET_ROUTES_METHOD).append("?").append(model.toQueryString()).toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(DEFAULT_TIME_OUT);
            connection.setUseCaches(Boolean.FALSE);

            final String response = readResponse(connection);
            GsonBuilder builder = new GsonBuilder();
            return builder.create().fromJson(response, new TypeToken<List<RouteModel>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            throw new ServiceException(ServiceErrorCode.INVALID_REQUEST, e);
        } catch (Exception e) {
            throw new ServiceException(ServiceErrorCode.UNKNOWN, e);
        }
    }

    /**
     * Reads the response from the connection.
     *
     * @param connection the connection to read response from
     * @return the response string
     * @throws ServiceException if an error occurs
     * @see at.fh.ooe.moc5.amazingrace.service.RestServiceProxy.ServiceErrorCode for the ServiceException contained error code
     */
    private String readResponse(HttpsURLConnection connection) throws ServiceException {
        Objects.requireNonNull(connection, "Cannot read from null connection");
        try {
            if (connection.getResponseCode() != 200) {
                throw new ServiceException(ServiceErrorCode.REQUEST_NOT_OK);
            }
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString();
            }
        } catch (SocketTimeoutException ste) {
            throw new ServiceException(ServiceErrorCode.TIMEOUT, ste);
        } catch (Exception e) {
            throw new ServiceException(ServiceErrorCode.UNKNOWN, e);
        }
    }

    /**
     * Enumeration which defines the common service errors
     */
    public static enum ServiceErrorCode {
        INVALID_REQUEST,
        TIMEOUT,
        UNKNOWN,
        REQUEST_NOT_OK,
        INVALID_CREDENTIALS
    }

    /**
     * Exception class for exception thrown in Service classes
     */
    public static final class ServiceException extends Exception {

        private ServiceErrorCode errorCode;

        public ServiceException() {
            this(null, null, null);
        }

        public ServiceException(String detailMessage) {
            this(null, detailMessage, null);
        }

        public ServiceException(String detailMessage, Throwable throwable) {
            this(null, detailMessage, throwable);
        }

        public ServiceException(Throwable throwable) {
            this(null, null, throwable);
        }

        public ServiceException(ServiceErrorCode errorCode) {
            this(errorCode, null, null);
        }

        public ServiceException(ServiceErrorCode errorCode, String detailMessage) {
            this(errorCode, detailMessage, null);
        }


        public ServiceException(ServiceErrorCode errorCode, Throwable throwable) {
            this(errorCode, null, throwable);
        }

        public ServiceException(ServiceErrorCode errorCode, String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
            this.errorCode = errorCode;
        }

        public ServiceErrorCode getErrorCode() {
            return errorCode;
        }
    }
}
