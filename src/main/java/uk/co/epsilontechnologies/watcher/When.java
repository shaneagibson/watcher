package uk.co.epsilontechnologies.watcher;

public class When {

    private final Watcher watcher;
    private final Request request;

    public When(final Watcher watcher, final Request request) {
        this.watcher = watcher;
        this.request = request;
    }

    public void thenCapture(final RequestCaptor... requestCaptors) {
        this.watcher.setCaptors(request, requestCaptors);
    }

    Watcher getWatcher() {
        return watcher;
    }

    Request getRequest() {
        return request;
    }

}