package uk.co.epsilontechnologies.watcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExBodyRequestCaptor implements RequestCaptor<List<String>> {

    private final String regEx;
    private final List<String> capturedValues = new ArrayList<String>();

    public RegExBodyRequestCaptor(final String regEx) {
        this.regEx = regEx;
    }

    @Override
    public void populate(final WatchableHttpServletRequestWrapper watchableHttpServletRequestWrapper) {
        final Matcher contentMatcher = Pattern.compile(regEx).matcher(watchableHttpServletRequestWrapper.getBody());
        while (contentMatcher.find()) {
           capturedValues.add(contentMatcher.group(0));
        }
    }

    @Override
    public List<String> getCapturedValue() {
        return capturedValues;
    }

    public String getFirstCapturedValue() {
        if (!capturedValues.isEmpty()) {
            return capturedValues.get(0);
        } else {
            return null;
        }
    }

}