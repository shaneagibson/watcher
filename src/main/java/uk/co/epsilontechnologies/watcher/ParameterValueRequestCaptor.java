package uk.co.epsilontechnologies.watcher;

public class ParameterValueRequestCaptor implements RequestCaptor<String> {

    private final String parameterName;

    private String capturedValue;

    public ParameterValueRequestCaptor(final String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public void populate(final WatchableHttpServletRequestWrapper watchableHttpServletRequestWrapper) {
        this.capturedValue = watchableHttpServletRequestWrapper.getParameter(parameterName);
    }

    @Override
    public String getCapturedValue() {
        return capturedValue;
    }

}