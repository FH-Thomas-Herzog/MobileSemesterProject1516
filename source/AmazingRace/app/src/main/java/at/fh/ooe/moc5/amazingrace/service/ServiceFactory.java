package at.fh.ooe.moc5.amazingrace.service;

/**
 * Created by Thomas on 12/24/2015.
 */
public class ServiceFactory {

    private static RestServiceProxy proxy;

    private ServiceFactory() {
    }

    public static RestServiceProxy createRestServiceProxy() {
        if (proxy == null) {
            proxy = new RestServiceProxyImpl();
        }
        return proxy;
    }
}
