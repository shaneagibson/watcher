package uk.co.epsilontechnologies.watcher;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.servlet.http.Cookie;
import java.util.*;

public class RequestInvocation {

    private final String contextPath;
    private final String requestUri;
    private final String queryString;
    private final List<Cookie> cookies;
    private final Map<String,String> headers;
    private final String method;
    private final String content;

    public RequestInvocation(final WatchableHttpServletRequestWrapper httpServletRequestWrapper) {
        this.requestUri = httpServletRequestWrapper.getRequestURI();
        this.contextPath = httpServletRequestWrapper.getContextPath();
        this.queryString = httpServletRequestWrapper.getQueryString();
        this.cookies = parseCookies(httpServletRequestWrapper);
        this.headers = parseHeaders(httpServletRequestWrapper);
        this.method = httpServletRequestWrapper.getMethod();
        this.content = httpServletRequestWrapper.getBody();
    }

    private Map<String, String> parseHeaders(final WatchableHttpServletRequestWrapper httpServletRequestWrapper) {
        final Map<String,String> result = new HashMap<String,String>();
        final Enumeration<String> headerNames = httpServletRequestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            final String headerValue = httpServletRequestWrapper.getHeader(headerName);
            result.put(headerName, headerValue);
        }
        return result;
    }

    private List<Cookie> parseCookies(final WatchableHttpServletRequestWrapper httpServletRequestWrapper) {
        if (httpServletRequestWrapper.getCookies() != null) {
            return Arrays.asList(httpServletRequestWrapper.getCookies());
        } else {
            return Collections.emptyList();
        }
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getQueryString() {
        return queryString;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}