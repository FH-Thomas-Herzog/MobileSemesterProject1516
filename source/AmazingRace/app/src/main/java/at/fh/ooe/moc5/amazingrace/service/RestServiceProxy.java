package at.fh.ooe.moc5.amazingrace.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Thomas on 12/24/2015.
 */
public class RestServiceProxy {

    public static final String REST_URL = "https://demo.nexperts.com/MOC5/AmazingRaceService/AmazingRaceService.svc";
    public static final String CHECK_CREDENTIALS = "/CheckCredentials";
    public static final String GET_ROUTES_METHOD = "/GetRoutes";
    public static final String VISITED_POINTS = "/InformAboutVisitedCheckpoint";
    public static final String RESET_ALL_ROUTES_METHOD = "/ResetAllRoutes";
    public static final String RESET_ROUTE_METHOD = "/ResetRoute";

    public static final int DEFAULT_TIME_OUT = 3000;

    public boolean checkCredentials(String username, String password) throws ServiceException {
        Objects.requireNonNull(username, "Cannot check credentials for null username");
        Objects.requireNonNull(password, "Cannot check credentials for null password");

        try {
            URL url = new URL(new StringBuilder(REST_URL).append(CHECK_CREDENTIALS).append("?userName=").append(username).append("&password=").append(password).toString());
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(DEFAULT_TIME_OUT);
            urlConnection.setUseCaches(Boolean.FALSE);

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                final String response = reader.readLine();
                if ((!"true".equals(response)) && (!"false".equals(response))) {
                    throw new ServiceException(ServiceErrorCode.INVALID_REQUEST);
                }
                return Boolean.parseBoolean(response);
            } catch (ServiceException se) {
                throw se;
            } catch (SocketTimeoutException ste) {
                throw new ServiceException(ServiceErrorCode.TIMEOUT, ste);
            } catch (Exception e) {
                throw new ServiceException(ServiceErrorCode.UNKNOWN, e);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ServiceErrorCode.UNKNOWN, e);
        }
    }

    public static enum ServiceErrorCode {
        INVALID_REQUEST,
        TIMEOUT,
        UNKNOWN
    }

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
