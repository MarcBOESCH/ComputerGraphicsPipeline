package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer implements PushFilter<Pair<Face, Color>, Void> {
    private final GraphicsContext gc;
    private final RenderingMode style;

    public Renderer(GraphicsContext gc, RenderingMode style) {
        this.gc = gc;
        this.style = style;
    }

    @Override
    public void setSuccessor(PushPipe<Void> successor) {
        // IGNORE
    }

    @Override
    public void push(Pair<Face, Color> data) {
            Face face = data.fst();
            Color color = data.snd();

            // Extrahiere die drei Eckpunkte (Screen-Koordinaten)
            double x1 = face.getV1().getX();
            double y1 = face.getV1().getY();
            double x2 = face.getV2().getX();
            double y2 = face.getV2().getY();
            double x3 = face.getV3().getX();
            double y3 = face.getV3().getY();

            // Arrays für Polygon-Aufrufe
            double[] xPoints = { x1, x2, x3 };
            double[] yPoints = { y1, y2, y3 };

            switch (style) {
                case POINT:
                    // Jeden Eckpunkt als kleinen Punkt zeichnen
                    gc.setStroke(color);
                    // Da JavaFX keinen drawPoint hat, nutzen wir strokeLine mit identischen Koordinaten
                    gc.strokeLine(x1, y1, x1, y1);
                    gc.strokeLine(x2, y2, x2, y2);
                    gc.strokeLine(x3, y3, x3, y3);
                    break;

                case WIREFRAME:
                    // Dreiecksumriss zeichnen
                    gc.setStroke(color);
                    gc.strokePolygon(xPoints, yPoints, 3);
                    break;

                case FILLED:
                    // Dreieck flächig füllen
                    gc.setFill(color);
                    gc.fillPolygon(xPoints, yPoints, 3);
                    break;
            }
        }

}
