package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.pipeline.filter.PullFilter;

public interface PullPipe<T> {
    public T pull();

    public PullFilter<?, T> getFilter();
    public void setPredecessor(PullFilter<?, T> pullFilter);
}
