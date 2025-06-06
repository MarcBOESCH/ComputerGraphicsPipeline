package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PullPipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class ProjectionFilter implements PushFilter<Pair<Face, Color>, Pair<Face, Color>>, PullFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private PushPipe<Pair<Face, Color>> successor;
    private Mat4 projectionMatrix;
    private PullPipe<Pair<Face, Color>> predecessor;

    public ProjectionFilter(Mat4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public void setSuccessor(PushPipe<Pair<Face, Color>> successor) {
        this.successor = successor;
    }

    @Override
    public void push(Pair<Face, Color> data) {
        Face face = data.fst();

        Face projectedFace = new Face(
                projectionMatrix.multiply(face.getV1()),
                projectionMatrix.multiply(face.getV2()),
                projectionMatrix.multiply(face.getV3()),
                face
        );

        if (successor != null) {
            successor.push(new Pair<>(projectedFace, data.snd()));
        }
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> data = predecessor.pull();
        if(data == null){
            return null;
        }
        Face face = data.fst();

        Face projectedFace = new Face(
                projectionMatrix.multiply(face.getV1()),
                projectionMatrix.multiply(face.getV2()),
                projectionMatrix.multiply(face.getV3()),
                face
        );

        return new Pair<>(projectedFace, data.snd());
    }

    @Override
    public void setPredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }
}
