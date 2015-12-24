package at.fh.ooe.moc5.amazingrace.service;

/**
 * Created by Thomas on 12/24/2015.
 */
public class ServiceFactory {
    private ServiceFactory() {
    }

    public static RestServiceProxy createRestServiceProxy() {
        return new RestServiceProxy();
    }
}
