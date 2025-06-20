package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.filter.*;
import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;
import at.fhv.sysarch.lab3.pipeline.pipe.PushPipe;
import com.hackoeur.jglm.*;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;


public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {

        // push from the source (model)
        PushFilter<Void, Face> sourceModel = new ModelSource();
        PushPipe<Face> sourcePipe = new Pipe<>();
        sourceModel.setSuccessor(sourcePipe);

        // perform model-view transformation from model to VIEW SPACE coordinates
        PushFilter<Face, Face> rotationFilter = new RotationFilter();
        sourcePipe.setSuccessor(rotationFilter);

        // perform backface culling in VIEW SPACE
        PushPipe<Face> backfaceCullingPipe = new Pipe<>();
        rotationFilter.setSuccessor(backfaceCullingPipe);
        PushFilter<Face, Face> backfaceCullingFilter = new BackfaceCullingFilter();
        backfaceCullingPipe.setSuccessor(backfaceCullingFilter);

        // perform depth sorting in VIEW SPACE
        PushPipe<Face> depthSortingPipe = new Pipe<>();
        backfaceCullingFilter.setSuccessor(depthSortingPipe);
        PushFilter<Face, Face> depthSortingFilter = new DepthSortingFilter();
        depthSortingPipe.setSuccessor(depthSortingFilter);

        // add coloring (space unimportant)
        PushPipe<Face> colorPipe = new Pipe<>();
        depthSortingFilter.setSuccessor(colorPipe);
        PushFilter<Face, Pair<Face, Color>> colorFilter = new ColorFilter(pd.getModelColor());
        colorPipe.setSuccessor(colorFilter);

        PushPipe<Pair<Face, Color>> projectionPipe = new Pipe<>();
        PushFilter<Pair<Face, Color>, Pair<Face, Color>> projectionFilter = new ProjectionFilter(pd.getProjTransform());

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            PushPipe<Pair<Face, Color>> lightPipe = new Pipe<>();
            colorFilter.setSuccessor(lightPipe);
            PushFilter<Pair<Face, Color>, Pair<Face, Color>> lightFilter = new LightningFilter(pd.getLightPos());
            lightPipe.setSuccessor(lightFilter);

            // perform projection transformation on VIEW SPACE coordinates
            lightFilter.setSuccessor(projectionPipe);

            projectionPipe.setSuccessor(projectionFilter);

        } else {
            colorFilter.setSuccessor(projectionPipe);
            projectionPipe.setSuccessor(projectionFilter);
        }

        // perform perspective division to screen coordinates
        PushPipe<Pair<Face, Color>> transformationPipe = new Pipe<>();
        projectionFilter.setSuccessor(transformationPipe);
        PushFilter<Pair<Face, Color>, Pair<Face, Color>> transformationFilter = new ScreenTransformationFilter(pd.getViewportTransform());
        transformationPipe.setSuccessor(transformationFilter);

        // feed into the sink (renderer)
        PushPipe<Pair<Face, Color>> renderingPipe = new Pipe<>();
        transformationFilter.setSuccessor(renderingPipe);
        PushFilter<Pair<Face, Color>, Void> renderer = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode());
        renderingPipe.setSuccessor(renderer);

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the fraction
        return new AnimationRenderer(pd) {

            private float rotation = 0f;
            private float angluarSpeed = 0.5f;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {
                // Rotation in rad berechnen
                rotation += fraction * angluarSpeed;

                // Rotationsmatrix erzeugen
                Mat4 rotationMatrix = Matrices.rotate(rotation, new Vec3(0f, 1f, 0f)); // Rotationsmatrix um Y-Achse
                Mat4 modelTranslationMatrix = pd.getModelTranslation().multiply(rotationMatrix); // Model-Matrix

                // ModelView-Transformation
                Mat4 modelView = pd.getViewTransform().multiply(modelTranslationMatrix);

                // ModelView-Filter updaten
                ((RotationFilter)rotationFilter).setModelViewMatrix(modelView); // ModelView-Matrix im RotationFilter setzten

                // Rendering triggern
                ((ModelSource)sourceModel).run(model);
                ((DepthSortingFilter)depthSortingFilter).sortAndPush();
            }
        };
    }
}