package uk.co.epsilontechnologies.watcher;

public interface RequestCaptor<T> {

    void populate(WatchableHttpServletRequestWrapper watchableHttpServletRequestWrapper);

    T getCapturedValue();

}
