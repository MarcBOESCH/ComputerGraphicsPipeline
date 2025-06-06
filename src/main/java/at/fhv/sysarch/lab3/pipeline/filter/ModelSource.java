package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PullPipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;

/**
 * ModelSource ist die Quelle (Source) aller Face-Objekte.
 * Er implementiert PushFilter<Void, Face>, sodass er seine Faces
 * an eine PushPipe<Face> weiterreichen kann.
 */
public class ModelSource implements PushFilter<Void, Face>, PullFilter<Object, Face> {
    private PushPipe<Face> successor;
    private Model model;
    private int index = 0;

    @Override
    public void setSuccessor(PushPipe<Face> successor) {
        this.successor = successor;
    }

    @Override
    public void push(Void input) {

        // run() wird stattdessen verwendet
    }

    public void run(Model model) {
        if (successor == null) {
            return;
        }
        model.getFaces().forEach(f -> successor.push(f));
    }



    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public Face pull() {
        if(this.index < this.model.getFaces().size()) {
            return this.model.getFaces().get(this.index++);
        }
        else{
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");

        }
    }

    public void reset() {this.index = 0;}

    @Override
    public void setPredecessor(Pipe<Object> predecessor) {
        //gits ned meine freunde
    }
}
