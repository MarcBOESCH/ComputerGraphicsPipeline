package at.fhv.sysarch.lab3.pipeline.pipe;

import at.fhv.sysarch.lab3.pipeline.filter.PullFilter;

public interface PullPipe<T> {
    public T pull();
    public void setPredecessor(PullFilter<?, T> pullFilter);
}
