package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class LightningFilter implements PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private PushPipe<Pair<Face, Color>> successor;
    private Vec3 lightPos;

    public LightningFilter(Vec3 lightPos) {
        this.lightPos = lightPos;
    }

    @Override
    public void setSuccessor(PushPipe<Pair<Face, Color>> successor) {
        this.successor = successor;
    }

    @Override
    public void push(Pair<Face, Color> data) {
        Face face = data.fst();
        Color baseColor = data.snd();

        // Drei Eckkoordinaten aus Face holen
        Vec3 p1 = face.getV1().toVec3();
        Vec3 p2 = face.getV2().toVec3();
        Vec3 p3 = face.getV3().toVec3();

        // Flächenmittelpunkt berechnen
        Vec3 faceCenter = p1.add(p2).add(p3).multiply(1f / 3f);

        // Kanten berechnen
        Vec3 edge1 = p2.subtract(p1);
        Vec3 edge2 = p3.subtract(p1);

        // Flächennormale berechnen
        Vec3 rawNormal = edge1.cross(edge2);
        float length = rawNormal.getLength();
        Vec3 faceNormal;
        if (length != 0f) {
            // Normalen normalisieren
            faceNormal = new Vec3(
                    rawNormal.getX() / length,
                    rawNormal.getY() / length,
                    rawNormal.getZ() / length
            );
        } else {
            // Falls das Dreieck Länge = 0 hat, wird das Normal auf (0,0,1) gesetzt
            faceNormal = new Vec3(0f, 0f, 1f);
        }

        // Lichtrichtung normalisieren
        Vec3 rawLightDir = lightPos.subtract(faceCenter);
        float rawLightDirLen = rawLightDir.getLength();
        Vec3 lightDir;
        if (rawLightDirLen != 0f) {
            lightDir = new Vec3(
                    rawLightDir.getX() / rawLightDirLen,
                    rawLightDir.getY() / rawLightDirLen,
                    rawLightDir.getZ() / rawLightDirLen
            );
        } else {
            lightDir = new Vec3(0f, 0f, 1f);
        }

        float dot = faceNormal.dot(lightDir);
        if (dot < 0f) {
            dot = 0f;
        } else if (dot > 1f) {
            dot = 1f;
        }

        Color shadedColor = new Color(
                baseColor.getRed() * dot,
                baseColor.getGreen() * dot,
                baseColor.getBlue() * dot,
                baseColor.getOpacity()
        );

        if (successor != null) {
            successor.push(new Pair<>(face, shadedColor));
        }


    }
}
