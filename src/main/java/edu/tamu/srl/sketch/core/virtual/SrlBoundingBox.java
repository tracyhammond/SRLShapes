package edu.tamu.srl.sketch.core.virtual;

import edu.tamu.srl.sketch.core.abstracted.SrlVirtualObject;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * A bounding box is typically used to find the smallest horizontal/vertical rectangle that can fit around an
 * {@link edu.tamu.srl.sketch.core.abstracted.SrlObject}.
 */
public class SrlBoundingBox extends SrlVirtualObject {

    /**
     * Top Left corner of the rectangle.
     * Top Left is defined as a user facing the screen.
     * Top Left should be less than the Bottom Right.
     */
    private SrlPoint mTopLeftCorner;

    /**
     * Bottom Right corner of the rectangle.
     * Bottom Right is defined as a using facing the screen.
     * Top Left should be less than the Bottom Right.
     */
    private SrlPoint mBottomRightCorner;
}
