package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

public class ScaleFilter implements PushFilter {


    private PushFilter successor;

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face f) {

        Mat4 scaleMatrix = new Mat4(new Vec4(50,0,0,0), new Vec4(0,50,0,0), new Vec4(0,0,50,0), new Vec4(0,0,0,1));

        Vec4 vec4_1 = f.getV1();
        Vec4 vec4_2 = f.getV2();
        Vec4 vec4_3 = f.getV3();

        vec4_1 = scaleMatrix.multiply(vec4_1);
        vec4_2 = scaleMatrix.multiply(vec4_2);
        vec4_3 = scaleMatrix.multiply(vec4_3);

        f = new Face(vec4_1, vec4_2, vec4_3, f);

        successor.push(f);
    }
}
