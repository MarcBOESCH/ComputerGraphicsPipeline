com.hackoeur.jglm contains the SOURCES of the JGLM Java OpenGL Mathematics Library https://github.com/jroyalty/jglm

It is no longer maintained but has all functionality necessary for this project. Why included as source: Only Version 1.0 is on Maven Central but we need 1.0.1 for some extra features, therefore simply adding a dependency would not have worked. Providing a pre-built JAR for the students would have been problematic because this would require them to use the same Java Version as I do. Solution: simply add resources.
In general: DON'T DO THIS. However, this is a good reason for doing it.

Also I added / cleaned up functionality:
- added Vec2
- Removed warnings due to deprecated Java API usage in JglmConfig and Precision


## Pipeline Functionality:

In general, it works exactly like showcased in the exercises pdf: Pipes connect filters.   

### Push Pipeline:
The order is as follows: The *ModelSource* filter pushes face for face into the *Rotation Filter* through a pipe.    
*Rotation Filter* (Rotating) -> *Backface Culling* (get rid of faces facing away from the camera) -> *DepthSoring* (sorts faces according to their Z axis) -> *Color* (adds color to faces) -> *Projection* (3D to 2D) ->
*Lighting* (if enabled: calculates color according to position of lightsource) -> *TransformationPipeline* (moves the teapots) -> *Rendering* (renders the finished frame)

### Pull Pipeline:
Essentially it is the same order of things but the Renderer calls for the models data and each Filter asks the next closer one for the data source.    
This results in the ModelSource being the last filter which returns a face and each Filter that asked for the data to do their thing before returning it to the next Filter until the face is rendered by the Renderer(=sink).