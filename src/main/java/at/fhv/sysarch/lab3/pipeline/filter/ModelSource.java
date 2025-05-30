package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

public class ModelSource implements PushFilter {
    private PushFilter successor;

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face f) {

        // CANNOT BE USED
    }

    public void run(Model model) {
        model.getFaces().forEach(f -> successor.push(f));
    }
}
