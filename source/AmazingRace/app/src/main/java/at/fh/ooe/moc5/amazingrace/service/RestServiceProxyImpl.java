package at.fh.ooe.moc5.amazingrace.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import at.fh.ooe.moc5.amazingrace.model.json.CredentialsRequestModel;
import at.fh.ooe.moc5.amazingrace.model.json.RouteModel;
import at.fh.ooe.moc5.amazingrace.model.json.SecretRequestModel;
import at.fh.ooe.moc5.amazingrace.service.ServiceException.ServiceErrorCode;

/**
 * Created by Thomas on 12/24/2015.
 * <p/>
 * Class for communicating with the rest backend.
 */
public class RestServiceProxyImpl implements RestServiceProxy {

    public static final String REST_URL = "https://demo.nexperts.com/MOC5/AmazingRaceService/AmazingRaceService.svc";
    public static final String CHECK_CREDENTIALS = "/CheckCredentials";
    public static final String GET_ROUTES_METHOD = "/GetRoutes";
    public static final String METHOD_VISIT_CHECKPOINT = "/InformAboutVisitedCheckpoint";
    public static final String RESET_ALL_ROUTES_METHOD = "/ResetAllRoutes";
    public static final String RESET_ROUTE_METHOD = "/ResetRoute";

    public static final int DEFAULT_TIME_OUT = 3000;

    /**
     * Checks the credentials if they map to an valid user.
     *
     * @param model the model holding the user credentials
     * @return true if the credentials map to an valid user
     * @throws ServiceException if the request failed.
     * @see ServiceErrorCode for the ServiceException contained error codes
     */
    @Override
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

    @Override
    public boolean visitCheckpoint(SecretRequestModel model) throws ServiceException {
        Objects.requireNonNull(model, "Cannot check checkpoint for null model");

        if (!checkCredentials(model)) {
            throw new ServiceException(ServiceErrorCode.INVALID_CREDENTIALS);
        }

        try {
            URL url = new URL(new StringBuilder(REST_URL).append(METHOD_VISIT_CHECKPOINT).toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(DEFAULT_TIME_OUT);
            connection.setUseCaches(Boolean.FALSE);
            connection.setDoInput(Boolean.TRUE);
            connection.setDoOutput(Boolean.TRUE);

            // Write Data
            writeData(connection, model);
            // Read response
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
     * @see ServiceErrorCode for the ServiceException contained error code
     */
    @Override
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
     * @see ServiceErrorCode for the ServiceException contained error code
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

    private void writeData(HttpsURLConnection connection, Object jsonModelInstance) throws ServiceException {
        Objects.requireNonNull(connection, "Cannot write to null connection");
        try {
            try (final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
                final String data = new GsonBuilder().create().toJson(jsonModelInstance);
                writer.write(data, 0, data.length());
                writer.flush();
            }
        } catch (JsonSyntaxException jse) {
            throw new ServiceException(ServiceErrorCode.INVALID_REQUEST, jse);
        } catch (SocketTimeoutException ste) {
            throw new ServiceException(ServiceErrorCode.TIMEOUT, ste);
        } catch (Exception e) {
            throw new ServiceException(ServiceErrorCode.UNKNOWN, e);
        }
    }

}
