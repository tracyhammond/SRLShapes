package edu.tamu.srl.sketch.core.virtual;

import edu.tamu.srl.sketch.core.abstracted.SrlVirtualObject;

import java.util.List;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * A bounding box is typically used to find the convex polygon that can encompass all of the points of an
 * {@link edu.tamu.srl.sketch.core.abstracted.SrlObject}.
 */
public class SrlConvexHull extends SrlVirtualObject {

    /**
     * The list of points that make up the convex hull.
     */
    private List<SrlPoint> mPoints;
}
