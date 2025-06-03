package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.pipeline.Pipe;

public interface PullFilter <I, O>{
    public void setPredecessor(Pipe<I> predecessor);
    public O pull();
}
