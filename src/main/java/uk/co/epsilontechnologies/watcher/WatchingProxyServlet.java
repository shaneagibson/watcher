package uk.co.epsilontechnologies.watcher;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class WatchingProxyServlet extends ProxyServlet {

    private final String rewriteHost;
    private final int rewritePort;
    private final Map<Request,RequestCaptor[]> requestCaptors;
    private final RequestMatcher requestMatcher;

    public WatchingProxyServlet(
            final String contextPath,
            final String rewriteHost,
            final int rewritePort,
            final Map<Request,RequestCaptor[]> requestCaptors) {
        this.rewriteHost = rewriteHost;
        this.rewritePort = rewritePort;
        this.requestCaptors = requestCaptors;
        this.requestMatcher = new RequestMatcher(contextPath);
    }

    @Override
    protected void service(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
        final WatchableHttpServletRequestWrapper watchableHttpServletRequestWrapper = new WatchableHttpServletRequestWrapper(httpServletRequest);
        for (final Request request : requestCaptors.keySet()) {
            if (requestMatcher.matches(request, watchableHttpServletRequestWrapper)) {
                for (final RequestCaptor requestCaptor : requestCaptors.get(request)) {
                    requestCaptor.populate(watchableHttpServletRequestWrapper);
                }
            }
        }
        super.service(watchableHttpServletRequestWrapper, httpServletResponse);
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
        return new HttpClient(sslContextFactory);
    }

    @Override
    protected void customizeProxyRequest(
            final org.eclipse.jetty.client.api.Request proxyRequest,
            final HttpServletRequest request) {
        proxyRequest.getHeaders().remove("Host");
    }

}