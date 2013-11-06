package uk.co.epsilontechnologies.watcher;

public class BodyRequestCaptor implements RequestCaptor<String> {

    private String capturedValue;

    public BodyRequestCaptor() {
        super();
    }

    @Override
    public void populate(final WatchableHttpServletRequestWrapper watchableHttpServletRequestWrapper) {
        capturedValue = watchableHttpServletRequestWrapper.getBody();
    }

    @Override
    public String getCapturedValue() {
        return capturedValue;
    }

}