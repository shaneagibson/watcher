package uk.co.epsilontechnologies.watcher;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractPairs {

    private final Map<String,String> headers = new HashMap();

    public AbstractPairs(final Pair... pairs) {
        for (final Pair pair : pairs) {
            headers.put(pair.getName(), pair.getValue());
        }
    }

    public Map<String,String> get() {
        return headers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}