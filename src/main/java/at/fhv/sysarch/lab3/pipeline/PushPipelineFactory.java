package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
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

        //perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
        } else {
            // 5. TODO perform projection transformation
        }

        // TODO 6. perform perspective division to screen coordinates

        // TODO 7. feed into the sink (renderer)

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
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
                Mat4 rotationMatrix = Matrices.rotate(rotation, pd.getModelRotAxis()); // Rotationsmatrix um Y-Achse
                Mat4 modelTranslationMatrix = pd.getModelTranslation().multiply(rotationMatrix); // Model-Matrix

                // ModelView-Transformation
                Mat4 modelView = pd.getViewTransform().multiply(modelTranslationMatrix);

                // ModelView-Filter updaten
                ((RotationFilter)rotationFilter).setModelViewMatrix(modelView); // ModelView-Matrix im RotationFilter setzten

                // Rendering triggern
                ((ModelSource)sourceModel).run(model);
            }
        };
    }
}