package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.filter.*;
import at.fhv.sysarch.lab3.pipeline.pipe.Pipe;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: pull from the source (model)
        PullFilter<Object, Face> sourceModel = new ModelSource();
        Pipe<Face> sourcePipe = new Pipe<>();
        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        PullFilter<Face, Face> rotationFilter = new RotationFilter();
        sourcePipe.setPredecessor(sourceModel);
        rotationFilter.setPredecessor(sourcePipe);
        // TODO 2. perform backface culling in VIEW SPACE

        PullFilter<Face, Face> backfaceCullingFilter = new BackfaceCullingFilter();
        Pipe<Face> backfacePipe = new Pipe<>();
        backfaceCullingFilter.setPredecessor(backfacePipe);
        backfacePipe.setPredecessor(rotationFilter);
        // TODO 3. perform depth sorting in VIEW SPACE
        PullFilter<Face, Face> depthSortingFilter = new DepthSortingFilter();
        Pipe<Face> depthSortingPipe = new Pipe<>();
        depthSortingFilter.setPredecessor(depthSortingPipe);
        depthSortingPipe.setPredecessor(backfaceCullingFilter);
        // TODO 4. add coloring (space unimportant)
        ColorFilter colorFilter = new ColorFilter(pd.getModelColor());
        Pipe<Face> colorPipe = new Pipe<>();
        colorFilter.setPredecessor(colorPipe);
        colorPipe.setPredecessor(depthSortingFilter);

        Pipe<Pair<Face, Color>> projectionPipe = new Pipe<>();
        ProjectionFilter projectionFilter = new ProjectionFilter(pd.getProjTransform());

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            Pipe<Pair<Face, Color>> lightingPipe = new Pipe<>();
            LightningFilter lightFilter = new LightningFilter(pd.getLightPos());
            lightingPipe.setPredecessor(colorFilter);
            lightFilter.setPredecessor(lightingPipe);

            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates#
            projectionPipe.setPredecessor(lightFilter);
            projectionFilter.setPredecessor(projectionPipe);

        } else {
            // 5. TODO perform projection transformation
            projectionPipe.setPredecessor(colorFilter);
            projectionFilter.setPredecessor(projectionPipe);
        }

        // TODO 6. perform perspective division to screen coordinates
            Pipe<Pair<Face, Color>> transformPipe = new Pipe<>();
            transformPipe.setPredecessor(projectionFilter);
            ScreenTransformationFilter transfromationFilter = new ScreenTransformationFilter(pd.getViewportTransform());
            transfromationFilter.setPredecessor(transformPipe);


        // TODO 7. feed into the sink (renderer)
        Pipe<Pair<Face, Color>> renderPipe = new Pipe<>();
        Renderer renderer = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode());
        renderPipe.setPredecessor(transfromationFilter);
        renderer.setPredecessor(renderPipe);

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the fraction
        return new AnimationRenderer(pd) {
            // TODO rotation variable goes in here
                private float rotation = 0f;
                private float angularSpeed = 0.5f;
            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {
                // TODO compute rotation in radians
                rotation += fraction * angularSpeed;
                // TODO create new model rotation matrix using pd.getModelRotAxis and Matrices.rotate
                Mat4 rotationMatrix = Matrices.rotate(rotation, new Vec3(0f, 1f, 0f));
                Mat4 modelTranslationMatrix = pd.getModelTranslation().multiply(rotationMatrix);
                // TODO compute updated model-view tranformation


                // TODO update model-view filter
                Mat4 modelView = pd.getViewTransform().multiply(modelTranslationMatrix);
                // TODO trigger rendering of the pipeline
                renderer.pull();
            }
        };
    }
}