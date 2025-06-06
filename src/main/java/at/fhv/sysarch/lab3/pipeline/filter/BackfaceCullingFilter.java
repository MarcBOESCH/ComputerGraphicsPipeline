package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PullPipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Vec3;

/**
 * BackfaceCullingFilter entfernt alle Dreiecke (Faces), die von der Kamera weggedreht sind.
 * Dazu berechnen wir in View-Space die geometrischen Flächennormalen mittels Kreuzprodukt
 * (v2 - v1) × (v3 - v1) und prüfen dessen Z-Komponente:
 *
 * Ist faceNormal.z > 0, zeigt die Fläche zur Kamera → weiterreichen.
 * Ist faceNormal.z ≤ 0, zeigt die Fläche weg bzw. ist kantig → verwerfen.
 */
public class BackfaceCullingFilter implements PushFilter<Face, Face>, PullFilter<Face, Face> {
    private PushPipe<Face> successor;
    private PullPipe<Face> predecessor;

    @Override
    public void setSuccessor(PushPipe<Face> successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face face) {
        // Drei Eckkoordinaten aus Face holen (View-Space)
        Vec3 p1 = face.getV1().toVec3();
        Vec3 p2 = face.getV2().toVec3();
        Vec3 p3 = face.getV3().toVec3();

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

    @Override
    public Face pull() {
        Face data = this.predecessor.pull();
        if(data != null) {
            // Drei Eckkoordinaten aus Face holen (View-Space)
            Vec3 p1 = data.getV1().toVec3();
            Vec3 p2 = data.getV2().toVec3();
            Vec3 p3 = data.getV3().toVec3();

            // Kanten berechnen
            Vec3 edge1 = p2.subtract(p1);
            Vec3 edge2 = p3.subtract(p1);

            // Flächennormale durch Kreuzprodukt berechnen
            Vec3 faceNormal = edge1.cross(edge2);

            // Z-Komponente testen:
            if (faceNormal.getZ() > 0) {
                return data;
            }
            // If the face is not visible, pull the next one
            return pull();
        }
        return null;
    }

    @Override
    public void setPredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }
}
