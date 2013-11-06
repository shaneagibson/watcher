package uk.co.epsilontechnologies.watcher;

import org.apache.commons.lang3.builder.ToStringBuilder;

class Request {

    private final String method;
    private final String uri;
    private final String body;
    private final Parameters parameters;
    private final Headers headers;

    Request(
            final String method,
            final String uri,
            final String body,
            final Parameters parameters,
            final Headers headers) {
        this.method = method;
        this.uri = uri;
        this.body = body;
        this.parameters = parameters;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public Headers getHeaders() {
        return headers;
    }

    public Parameters getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
