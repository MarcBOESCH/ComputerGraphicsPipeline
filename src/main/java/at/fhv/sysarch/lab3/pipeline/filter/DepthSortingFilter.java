package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.Vec4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sortiert alle gesammelten Faces nach ihrem durchschnittlichen Z-Wert in aufsteigender Reihenfolge
 * (weit entfernte Faces zuerst) und ruft dann
 * successor.push(face) auf jedem sortierten Face auf.
 */
public class DepthSortingFilter implements PushFilter<Face, Face> {

    private PushPipe<Face> successor;

    private List<Face> faceQueue = new ArrayList<>();

    @Override
    public void setSuccessor(PushPipe<Face> successor) {
        this.successor = successor;
    }

    /**
     * Fügt ein Face in den internen Puffer ein. Die eigentliche Weiterleitung
     * geschieht erst in sortAndPush().
     */
    @Override
    public void push(Face face) {
        faceQueue.add(face);
    }

    /**
     * Sortiert alle gesammelten Faces nach ihrem durchschnittlichen Z-Wert in aufsteigender Reihenfolge
     * (weit entfernte Faces zuerst) und ruft dann successor.push(face)
     * auf jedem sortierten Face auf. Anschließend wird die Queue geleert.
     */
    public void sortAndPush() {
        // Queue anhand des durchschnittlichen Z-Werts sortieren
        faceQueue.sort(Comparator.comparingDouble(this::averageZ));

        // Jedes sortierte Face pushen
        for (Face f : faceQueue) {
            if (successor != null) {
                successor.push(f);
            }
        }

        // Queue für den nächsten Frame leeren
        faceQueue.clear();
    }

    private double averageZ(Face face) {
        Vec4 v1 = face.getV1();
        Vec4 v2 = face.getV2();
        Vec4 v3 = face.getV3();

        return (v1.getZ() + v2.getZ() + v3.getZ()) / 3.0;
    }
}
