package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class ScreenTransformationFilter implements PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private PushPipe<Pair<Face, Color>> successor;

    private Mat4 viewPortTransformationMatrix;

    public ScreenTransformationFilter(Mat4 viewPortTransformationMatrix) {
        this.viewPortTransformationMatrix = viewPortTransformationMatrix;
    }
    @Override
    public void setSuccessor(PushPipe<Pair<Face, Color>> successor) {
        this.successor = successor;
    }

    @Override
    public void push(Pair<Face, Color> data) {
        Face face = data.fst();

        Face dividedFace = new Face(
                face.getV1().multiply(1f / face.getV1().getW()),
                face.getV2().multiply(1f / face.getV2().getW()),
                face.getV3().multiply(1f / face.getV3().getW()),
                face
        );

        Face transformedFace = new Face(
                viewPortTransformationMatrix.multiply(dividedFace.getV1()),
                viewPortTransformationMatrix.multiply(dividedFace.getV2()),
                viewPortTransformationMatrix.multiply(dividedFace.getV3()),
                dividedFace
        );

        successor.push(new Pair<>(transformedFace, data.snd()));
    }
}
