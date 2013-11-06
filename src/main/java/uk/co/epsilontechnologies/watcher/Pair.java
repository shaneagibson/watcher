package uk.co.epsilontechnologies.watcher;

public class Pair {

    private final String name;
    private final String value;

    public Pair(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
