package edu.tamu.srl.sketch.core.abstracted;

import edu.tamu.srl.sketch.core.virtual.SrlBoundingBox;
import edu.tamu.srl.sketch.core.virtual.SrlConvexHull;

import java.util.Map;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * An abstract class for representing all objects that take up space.
 * Contains methods used by all objects that take up space.
 * All {@link SrlObject}s are able to be recognized.
 */
public abstract class SrlObject extends SrlComponent {

    /**
     * Map of miscellaneous attributes (to store any attributes given for points
     * in a SketchML file that are not saved in other variables here).
     */
    private Map<String, String> mAttributes;

    /**
     * The bounding box of the object.
     * This is the smaller vertical/horizontal rectangle that can encompass all of the points inside the shape.
     */
    private SrlBoundingBox mBoundingBox;

    /**
     * An object can be created by a user (like drawing a shape, or speaking a
     * phrase) or it can be created by a system (like a recognition of a higher
     * level shape).
     */
    private boolean mIsUserCreated = false;

    /**
     * The convex hull of the object.
     * The convex hull is defined as being the smallest convex polygon that can encompass the entire
     * {@link edu.tamu.srl.sketch.core.abstracted.SrlObject}.
     */
    private transient SrlConvexHull mConvexHull;
}
