package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.filter.*;
import com.hackoeur.jglm.*;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import javax.xml.transform.Source;


public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: push from the source (model)

        // TODO: improve connecting filters, add pipes...

        PushFilter sourceModel = new ModelSource();

        PushFilter rotFilter = new RotationFilter();
        PushFilter scaleFilter = new ScaleFilter();
        PushFilter translationFilter = new TranslationFilter();
        PushFilter renderer = new Renderer(pd.getGraphicsContext(), pd.getModelColor());

        sourceModel.setSuccessor(rotFilter);
        rotFilter.setSuccessor(scaleFilter);
        scaleFilter.setSuccessor(translationFilter);
        translationFilter.setSuccessor(renderer);

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates

        // TODO 2. perform backface culling in VIEW SPACE

        // TODO 3. perform depth sorting in VIEW SPACE

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
            // TODO rotation variable goes in here

            private int counter = 0;
            private double startPos = Math.random()*1000;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {

                pd.getGraphicsContext().setStroke(Color.WHITE);
                pd.getGraphicsContext().strokeLine(startPos+counter,startPos+ counter,startPos+counter+100,startPos+counter+100);
                counter++;


                // TODO: use generic parameters for passing objects
                ((ModelSource)sourceModel).run(model);

                // TODO compute rotation in radians

                // TODO create new model rotation matrix using pd.modelRotAxis

                // TODO compute updated model-view tranformation

                // TODO update model-view filter

                // TODO trigger rendering of the pipeline

            }
        };
    }
}