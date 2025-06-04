package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ProjectionFilter implements PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private PushPipe<Pair<Face, Color>> successor;
    private Mat4 projectionMatrix;

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
}
