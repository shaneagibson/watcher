package uk.co.epsilontechnologies.watcher;

import java.util.Map;
import java.util.regex.Pattern;

public class RequestMatcher {

    private final MapMatcher mapMatcher;
    private final StringMatcher stringMatcher;

    public RequestMatcher() {
        this(new MapMatcher(), new StringMatcher());
    }

    public RequestMatcher(final MapMatcher mapMatcher, final StringMatcher stringMatcher) {
        this.mapMatcher = mapMatcher;
        this.stringMatcher = stringMatcher;
    }

    public boolean matches(
            final Request requestToMatch,
            final WatchableHttpServletRequestWrapper requestWrapper) {
        return
                bodyMatches(requestToMatch, requestWrapper) &&
                uriMatches(requestToMatch, requestWrapper) &&
                methodMatches(requestToMatch, requestWrapper) &&
                parametersMatch(requestToMatch, requestWrapper) &&
                headersMatch(requestToMatch, requestWrapper);
    }

    private boolean headersMatch(final Request requestToMatch, final WatchableHttpServletRequestWrapper requestWrapper) {
        return mapMatcher.match(requestWrapper.getHeadersAsMap(), requestToMatch.getHeaders().get());
    }

    private boolean parametersMatch(final Request requestToMatch, final WatchableHttpServletRequestWrapper requestWrapper) {
        return mapMatcher.match(requestWrapper.getParametersAsMap(), requestToMatch.getParameters().get());
    }

    private boolean methodMatches(final Request requestToMatch, final WatchableHttpServletRequestWrapper requestWrapper) {
        return stringMatcher.match(requestWrapper.getMethod(), requestToMatch.getMethod());
    }

    private boolean uriMatches(final Request requestToMatch, final WatchableHttpServletRequestWrapper requestWrapper) {
        return stringMatcher.match(requestWrapper.getRequestURI(), requestToMatch.getURI());
    }

    private boolean bodyMatches(final Request requestToMatch, final WatchableHttpServletRequestWrapper requestWrapper) {
        return stringMatcher.match(requestWrapper.getBody(), requestToMatch.getBody());
    }

    static class MapMatcher {

        private final StringMatcher stringMatcher;

        MapMatcher() {
            this(new StringMatcher());
        }

        MapMatcher(final StringMatcher stringMatcher) {
            this.stringMatcher = stringMatcher;
        }

        public boolean match(
                final Map<String, String> requestMap,
                final Map<String, String> mapToMatch) {
            for (final String primedKey : mapToMatch.keySet()) {
                if (!(requestMap.containsKey(primedKey) && stringMatcher.match(mapToMatch.get(primedKey), requestMap.get(primedKey)))) {
                    return false;
                }
            }
            return true;
        }

    }

    static class StringMatcher {

        public boolean match(
                final String requestString,
                final String stringToMatch) {
            return (stringToMatch != null && stringToMatch.equals(requestString)) || Pattern.matches(stringToMatch, requestString);
        }

    }

}