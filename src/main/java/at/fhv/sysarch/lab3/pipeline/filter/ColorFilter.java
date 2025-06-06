package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import javafx.scene.paint.Color;


public class ColorFilter implements PushFilter<Face, Pair<Face, Color>>, PullFilter<Face, Pair<Face, Color>> {

    private PushPipe<Pair<Face, Color>> successor;
    private Color color;

    private Pipe<Face> predecessor;

    public ColorFilter(Color color) {
        this.color = color;
    }
    @Override
    public void setSuccessor(PushPipe<Pair<Face, Color>> successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face face) {
        if (successor != null) {
            successor.push(new Pair<>(face, color));
        }
    }


    @Override
    public Pair<Face, Color> pull() {
        Face data = this.predecessor.pull();
        if(data != null) {
            return new Pair<>(data, this.color);
        }
        return null;
    }

    @Override
    public void setPredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }
}
