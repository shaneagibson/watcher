package uk.co.epsilontechnologies.watcher;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.HashMap;
import java.util.Map;

public class Watcher {

    private final Server server;

    private final Map<Request,RequestCaptor[]> requestCaptors = new HashMap<Request, RequestCaptor[]>();

    public Watcher(final String contextPath, final int port, final String rewriteHost, final int rewritePort) {
        this.server = new Server(port);
        this.server.setHandler(new ProxyHandler(contextPath, rewriteHost, rewritePort));
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
            requestCaptors.clear();
            server.stop();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Action get(final String uri, final Parameters parameters, final Headers headers) {
        return new Action(this, new Request("GET", uri, "", parameters, headers));
    }

    public Action get(final String uri, final Headers headers) {
        return get(uri, new Parameters(), headers);
    }

    public Action get(final String uri, final Parameters parameters) {
        return get(uri, parameters, new Headers());
    }

    public Action get(final String uri) {
        return get(uri, new Parameters(), new Headers());
    }

    public Action head(final String uri, final Parameters parameters, final Headers headers) {
        return new Action(this, new Request("HEAD", uri, "", parameters, headers));
    }

    public Action head(final String uri, final Headers headers) {
        return head(uri, new Parameters(), headers);
    }

    public Action head(final String uri, final Parameters parameters) {
        return head(uri, parameters, new Headers());
    }

    public Action head(final String uri) {
        return head(uri, new Parameters(), new Headers());
    }

    public Action trace(final String uri, final Parameters parameters, final Headers headers) {
        return new Action(this, new Request("TRACE", uri, "", parameters, headers));
    }

    public Action trace(final String uri, final Headers headers) {
        return trace(uri, new Parameters(), headers);
    }

    public Action trace(final String uri, final Parameters parameters) {
        return trace(uri, parameters, new Headers());
    }

    public Action trace(final String uri) {
        return trace(uri, new Parameters(), new Headers());
    }

    public Action options(final String uri, final Parameters parameters, final Headers headers) {
        return new Action(this, new Request("OPTIONS", uri, "", parameters, headers));
    }

    public Action options(final String uri, final Headers headers) {
        return options(uri, new Parameters(), headers);
    }

    public Action options(final String uri, final Parameters parameters) {
        return options(uri, parameters, new Headers());
    }

    public Action options(final String uri) {
        return options(uri, new Parameters(), new Headers());
    }

    public Action delete(final String uri, final Parameters parameters, final Headers headers) {
        return new Action(this, new Request("DELETE", uri, "", parameters, headers));
    }

    public Action delete(final String uri, final Headers headers) {
        return delete(uri, new Parameters(), headers);
    }

    public Action delete(final String uri, final Parameters parameters) {
        return delete(uri, parameters, new Headers());
    }

    public Action delete(final String uri) {
        return delete(uri, new Parameters(), new Headers());
    }

    public Action put(final String uri, final String body, final Parameters parameters, final Headers headers) {
        return new Action(this, new Request("PUT", uri, body, parameters, headers));
    }

    public Action put(final String uri, final String body, final Parameters parameters) {
        return put(uri, body, parameters, new Headers());
    }

    public Action put(final String uri, final String body, final Headers headers) {
        return put(uri, body, new Parameters(), headers);
    }

    public Action put(final String uri, final String body) {
        return put(uri, body, new Parameters(), new Headers());
    }

    public Action post(final String uri, final String body, final Parameters parameters, final Headers headers) {
        return new Action(this, new Request("POST", uri, body, parameters, headers));
    }

    public Action post(final String uri, final String body, final Parameters parameters) {
        return post(uri, body, parameters, new Headers());
    }

    public Action post(final String uri, final String body, final Headers headers) {
        return post(uri, body, new Parameters(), headers);
    }

    public Action post(final String uri, final String body) {
        return post(uri, body, new Parameters(), new Headers());
    }

    public void setCaptors(final Request request, final RequestCaptor[] requestCaptors) {
        this.requestCaptors.put(request, requestCaptors);
    }

    class ProxyHandler extends ServletContextHandler {

        private final WatchingProxyServlet watchingProxyServlet;

        public ProxyHandler(final String contextPath, final String rewriteHost, final int rewritePort) {
            super(ServletContextHandler.SESSIONS);
            this.watchingProxyServlet = new WatchingProxyServlet(contextPath, rewriteHost, rewritePort, requestCaptors);
            this.addServlet(new ServletHolder(watchingProxyServlet), "/*");
        }

    }

}