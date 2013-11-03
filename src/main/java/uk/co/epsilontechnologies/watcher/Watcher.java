package uk.co.epsilontechnologies.watcher;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.ArrayList;
import java.util.List;

public class Watcher {

    private final Server server;

    private final List<RequestInvocation> watchedInvocations = new ArrayList<RequestInvocation>();

    public Watcher(final int port, final String rewriteHost, final int rewritePort) {
        this.server = new Server(port);
        this.server.setHandler(new ProxyHandler(rewriteHost, rewritePort));
    }

    public void start() {
        try {
            server.start();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            watchedInvocations.clear();
            server.stop();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    class ProxyHandler extends ServletContextHandler {

        private final WatchingProxyServlet watchingProxyServlet;

        public ProxyHandler(final String rewriteHost, final int rewritePort) {
            super(ServletContextHandler.SESSIONS);
            this.watchingProxyServlet = new WatchingProxyServlet(rewriteHost, rewritePort, watchedInvocations);
            this.addServlet(new ServletHolder(watchingProxyServlet), "/*");
        }

    }

    public List<RequestInvocation> getWatchedInvocations() {
        return this.watchedInvocations;
    }

}