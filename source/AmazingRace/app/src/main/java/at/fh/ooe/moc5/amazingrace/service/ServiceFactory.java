package at.fh.ooe.moc5.amazingrace.service;

/**
 * Created by Thomas on 12/24/2015.
 */
public class ServiceFactory {

    private static ServiceProxy proxy;

    private ServiceFactory() {
    }

    public static ServiceProxy createServiceProxy() {
        if (proxy == null) {
            proxy = new RestServiceProxyImpl();
        }
        return proxy;
    }
}
