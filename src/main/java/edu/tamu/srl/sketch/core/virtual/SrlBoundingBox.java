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

    /**
     * Constructor takes two points, and constructs a horizontal rectangle from this.
     *
     * @param topLeftCorner The point that is assigned as the top left corner. Should be lower in value.
     * @param bottomRightCorner The point that is assigned as the bottom right corner.  Should be larger in value.
     */
    public SrlBoundingBox(final SrlPoint topLeftCorner, final SrlPoint bottomRightCorner) {
        setTopLeftCorner(topLeftCorner);
        setBottomRightCorner(bottomRightCorner);
    }

    /**
     * Constructor.
     *
     * @param minX the smallest x value.
     * @param minY the smallest y value.
     * @param maxX the largest x value.
     * @param maxY the largest y value.
     */
    public SrlBoundingBox(final double minX, final double minY, final double maxX, final double maxY) {
        this(new SrlPoint(minX, minY), new SrlPoint(maxX, maxY));
    }

    /**
     * Copy constructor.
     * @param srlBoundingBox object that is being copied.
     */
    public SrlBoundingBox(final SrlBoundingBox srlBoundingBox) {
        super(srlBoundingBox);
    }

    /**
     * Gets the bottom right corner.
     *
     * @return The bottom right corner.
     */
    public final SrlPoint getBottomRightCorner() {
        return mBottomRightCorner;
    }

    /**
     * Sets the bottom right corner.
     *
     * @param bottomRightCorner The {@link edu.tamu.srl.sketch.core.virtual.SrlPoint} that represents the bottom right corner.
     */
    public final void setBottomRightCorner(final SrlPoint bottomRightCorner) {
        mBottomRightCorner = bottomRightCorner;
    }

    /**
     * Gets the top left corner point.
     *
     * @return The top left corner.
     */
    public final SrlPoint getTopLeftCorner() {
        return mTopLeftCorner;
    }

    /**
     * Sets the top left corner.
     *
     * @param topLeftCorner The {@link edu.tamu.srl.sketch.core.virtual.SrlPoint} that represents the top left corner.
     */
    public final void setTopLeftCorner(final SrlPoint topLeftCorner) {
        mTopLeftCorner = topLeftCorner;
    }
}
