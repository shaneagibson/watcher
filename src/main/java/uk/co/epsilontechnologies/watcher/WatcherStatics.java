package uk.co.epsilontechnologies.watcher;

public final class WatcherStatics {

    private WatcherStatics() {
        super();
    }

    public static When when(final Action action) {
        return new When(action.getWatcher(), action.getRequest());
    }

    public static Headers headers(final Pair... pairs) {
        return new Headers(pairs);
    }

    public static Pair pair(final String name, final String value) {
        return new Pair(name, value);
    }

    public static Parameters parameters(final Pair... pairs) {
        return new Parameters(pairs);
    }

    public static Request request(
            final String method,
            final String path,
            final String body,
            final Parameters parameters,
            final Headers headers) {
        return new Request(method, path, body, parameters, headers);
    }

}
