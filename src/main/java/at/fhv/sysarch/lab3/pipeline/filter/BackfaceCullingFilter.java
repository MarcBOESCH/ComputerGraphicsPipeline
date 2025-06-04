package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

/**
 * BackfaceCullingFilter entfernt alle Dreiecke (Faces), die von der Kamera weggedreht sind.
 * Dazu berechnen wir in View-Space die geometrischen Flächennormalen mittels Kreuzprodukt
 * (v2 - v1) × (v3 - v1) und prüfen dessen Z-Komponente:
 *
 * Ist faceNormal.z > 0, zeigt die Fläche zur Kamera → weiterreichen.
 * Ist faceNormal.z ≤ 0, zeigt die Fläche weg bzw. ist kantig → verwerfen.
 */
public class BackfaceCullingFilter implements PushFilter<Face, Face>{
    private PushPipe<Face> successor;

    @Override
    public void setSuccessor(PushPipe<Face> successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face face) {
        // Drei Eckkoordinaten aus Face holen (View-Space)
        Vec4 v1 = face.getV1();
        Vec4 v2 = face.getV2();
        Vec4 v3 = face.getV3();
        Vec3 p1 = new Vec3(v1.getX(), v1.getY(), v1.getZ());
        Vec3 p2 = new Vec3(v2.getX(), v2.getY(), v2.getZ());
        Vec3 p3 = new Vec3(v3.getX(), v3.getY(), v3.getZ());

        // Kanten berechnen
        Vec3 edge1 = p2.subtract(p1);
        Vec3 edge2 = p3.subtract(p1);

        // Flächennormale durch Kreuzprodukt berechnen
        Vec3 faceNormal = edge1.cross(edge2);

        // Z-Komponente testen:
        if (faceNormal.getZ() > 0) {
            successor.push(face);
        }
    }
}
