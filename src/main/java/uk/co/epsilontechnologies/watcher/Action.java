package uk.co.epsilontechnologies.watcher;

class Action {

    private final Watcher watcher;
    private final Request request;

    public Action(final Watcher watcher, final Request request) {
        this.watcher = watcher;
        this.request = request;
    }

    Watcher getWatcher() {
        return watcher;
    }

    Request getRequest() {
        return request;
    }

}
