package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.pipeline.filter.PullFilter;

public class Pipe<T> implements PullPipe<T> {
    private PullFilter<?, T> pullFilter;

    //Pullpipe implementations:

    public Pipe(PullFilter<?, T> pullFilter) {
        this.pullFilter = pullFilter;
    }

    @Override
    public T pull() {
        return pullFilter.pull();
    }

    @Override
    public PullFilter<?, T> getFilter() {
        return pullFilter;
    }

    @Override
    public void setPredecessor(PullFilter<?, T> pullFilter) {
        this.pullFilter = pullFilter;
    }
}
