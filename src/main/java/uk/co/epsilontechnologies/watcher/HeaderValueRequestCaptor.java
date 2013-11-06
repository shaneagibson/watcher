package uk.co.epsilontechnologies.watcher;

public class HeaderValueRequestCaptor implements RequestCaptor<String> {

    private final String headerName;

    private String capturedValue;

    public HeaderValueRequestCaptor(final String headerName) {
        this.headerName = headerName;
    }

    @Override
    public void populate(final WatchableHttpServletRequestWrapper watchableHttpServletRequestWrapper) {
        this.capturedValue = watchableHttpServletRequestWrapper.getHeader(headerName);
    }

    @Override
    public String getCapturedValue() {
        return capturedValue;
    }

}