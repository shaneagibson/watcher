package uk.co.epsilontechnologies.watcher;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class WatchingProxyServlet extends ProxyServlet {

    private final String rewriteHost;
    private final int rewritePort;
    private final List<RequestInvocation> watchedInvocations;

    public WatchingProxyServlet(
            final String rewriteHost,
            final int rewritePort,
            final List<RequestInvocation> watchedInvocations) {
        this.rewriteHost = rewriteHost;
        this.rewritePort = rewritePort;
        this.watchedInvocations = watchedInvocations;
    }

    @Override
    public void service(final ServletRequest servletRequest, final ServletResponse servletResponse) throws ServletException, IOException {
        final WatchableHttpServletRequestWrapper watchableHttpServletRequestWrapper = new WatchableHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        watchedInvocations.add(new RequestInvocation(watchableHttpServletRequestWrapper));
        super.service(watchableHttpServletRequestWrapper, servletResponse);
    }

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    @Override
    protected URI rewriteURI(final HttpServletRequest request) {
        final String path = request.getRequestURI();
        final String queryString = request.getQueryString();
        return URI.create("http://"+rewriteHost+":"+rewritePort+"/"+path+(queryString == null ? "" : "?" + queryString)).normalize();
    }

    @Override
    protected HttpClient newHttpClient() {
        final SslContextFactory sslContextFactory = new SslContextFactory();
        final HttpClient httpClient = new HttpClient(sslContextFactory);
        return httpClient;
    }

    @Override
    protected void customizeProxyRequest(
            final Request proxyRequest,
            final HttpServletRequest request) {
        proxyRequest.getHeaders().remove("Host");
    }

}