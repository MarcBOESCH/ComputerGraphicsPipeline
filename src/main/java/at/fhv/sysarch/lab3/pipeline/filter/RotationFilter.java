package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

public class RotationFilter implements PushFilter {


    private PushFilter successor;

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    public void setRotationTime(float time) {
        // PROCESS
    }

    @Override
    public void push(Face f) {

        Mat4 rotationMatrix = Matrices.rotate(15, new Vec3(1, 0, 0));

        Vec4 vec4_1 = f.getV1();
        Vec4 vec4_2 = f.getV2();
        Vec4 vec4_3 = f.getV3();

        vec4_1 = rotationMatrix.multiply(vec4_1);
        vec4_2 = rotationMatrix.multiply(vec4_2);
        vec4_3 = rotationMatrix.multiply(vec4_3);

        f = new Face(vec4_1, vec4_2, vec4_3, f);

        successor.push(f);
    }
}
