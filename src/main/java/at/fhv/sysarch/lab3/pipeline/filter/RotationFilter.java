package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PullPipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

/**
 * RotationFilter rotiert jedes Face um die Y-Achse (Model‐Rotation),
 * wendet anschließend die View-Transformation an und erzeugt ein neues Face.
 * Das neue Face wird an successor.push(...) weitergegeben.
 */
public class RotationFilter implements PushFilter<Face, Face>, PullFilter<Face, Face> {
    private PushPipe<Face> successor;
    private Mat4 modelViewMatrix;
    private PullPipe<Face> predecessor;

    @Override
    public void setSuccessor(PushPipe<Face> successor) {
        this.successor = successor;
    }

    public void setModelViewMatrix(Mat4 modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }

    public void setRotationTime(float time) {
        // PROCESS
    }

    @Override
    public void push(Face f) {
        if (modelViewMatrix == null) {
            return;
        }
        // Eckpunkte transformieren
        Vec4 newV1 = modelViewMatrix.multiply(f.getV1());
        Vec4 newV2 = modelViewMatrix.multiply(f.getV2());
        Vec4 newV3 = modelViewMatrix.multiply(f.getV3());

        Vec4 newN1 = modelViewMatrix.multiply(f.getN1());
        Vec4 newN2 = modelViewMatrix.multiply(f.getN2());
        Vec4 newN3 = modelViewMatrix.multiply(f.getN3());

        Face transformed = new Face(newV1, newV2, newV3, newN1, newN2, newN3);

        if (successor != null) {
            successor.push(transformed);
        }
    }


    @Override
    public Face pull() {
        Face data = this.predecessor.pull();
        if (data != null){
            //?
            if (modelViewMatrix == null) {
                return data;
            }
            Vec4 newV1 = modelViewMatrix.multiply(data.getV1());
            Vec4 newV2 = modelViewMatrix.multiply(data.getV2());
            Vec4 newV3 = modelViewMatrix.multiply(data.getV3());

            Vec4 newN1 = modelViewMatrix.multiply(data.getN1());
            Vec4 newN2 = modelViewMatrix.multiply(data.getN2());
            Vec4 newN3 = modelViewMatrix.multiply(data.getN3());

            return new Face(newV1, newV2, newV3, newN1, newN2, newN3);
        }
        return null;
    }

    @Override
    public void setPredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }
}
