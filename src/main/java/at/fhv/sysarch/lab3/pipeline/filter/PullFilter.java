package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;

public interface PullFilter<I, O> {

    public O pull();
    public void setPredecessor(Pipe<I> predecessor);

}
