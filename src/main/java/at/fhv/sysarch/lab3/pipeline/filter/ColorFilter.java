package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import javafx.scene.paint.Color;


public class ColorFilter implements PushFilter<Face, Pair<Face, Color>> {

    private PushPipe<Pair<Face, Color>> successor;
    private Color color;

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
}
