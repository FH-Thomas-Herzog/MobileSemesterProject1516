package at.fh.ooe.moc5.amazingrace.service;

/**
 * Created by Thomas on 12/24/2015.
 */
public class ServiceProxyFactory {

    private static ServiceProxy proxy;

    private ServiceProxyFactory() {
    }

    public static ServiceProxy createServiceProxy() {
        if (proxy == null) {
            proxy = new RestServiceProxyImpl();
        }
        return proxy;
    }
}
