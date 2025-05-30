package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;

public interface PushFilter {

    public void setSuccessor(PushFilter successor);

    public void push(Face f);
}
