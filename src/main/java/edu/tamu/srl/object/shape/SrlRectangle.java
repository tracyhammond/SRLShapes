/**
 *
 */
package edu.tamu.srl.object.shape;

/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlRectangle extends SrlShape {

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
     * @param topLeftCorner The point that is assigned as the top left corner. should be lower in
     * @param bottomRightCorner
     */
    public SrlRectangle(final SrlPoint topLeftCorner, final SrlPoint bottomRightCorner) {
        setTopLeftCorner(topLeftCorner);
        setBottomRightCorner(bottomRightCorner);
    }

    /**
     * Constructor
     *
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     */
    public SrlRectangle(final double minX, final double minY, final double maxX, final double maxY) {
        this(new SrlPoint(minX, minY), new SrlPoint(maxX, maxY));
    }

    /**
     * Copy constructor.
     * @param srlObjects
     */
    public SrlRectangle(final SrlRectangle srlObjects) {
        super(srlObjects);
    }

    /**
     * Gets the bottom right corner
     *
     * @return
     */
    public final SrlPoint getBottomRightCorner() {
        return mBottomRightCorner;
    }

    /**
     * Sets the bottomright corner
     *
     * @param bottomRightCorner
     */
    public final void setBottomRightCorner(SrlPoint bottomRightCorner) {
        mBottomRightCorner = bottomRightCorner;
    }

    /*
     * Returns the minimum x. This works even if the topleft and bottomright are switched
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
     */
    @Override
    public final double getMinX() {
        return Math.min(mTopLeftCorner.getX(), mBottomRightCorner.getX());
    }

    /*
     * Returns the minimum y. This works even if the topleft and bottomright are switched
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMinY()
     */
    @Override
    public final double getMinY() {
        return Math.min(mTopLeftCorner.getY(), mBottomRightCorner.getY());
    }

    /**
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians the number of radians to rotate
     * @param xCenter the x-coordinate to rotate from
     * @param yCenter the y-coordinate to rotate from
     */
    @Override public final void rotate(final double radians, final double xCenter, final double yCenter) {
        mTopLeftCorner.rotate(radians, xCenter, yCenter);
        mBottomRightCorner.rotate(radians, xCenter, yCenter);
    }

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xfactor the x-factor
     * @param yfactor the y-factor
     */
    @Override public final void scale(final double xfactor, final double yfactor) {
        mTopLeftCorner.scale(xfactor, yfactor);
        mBottomRightCorner.scale(xfactor, yfactor);
    }

    /*
     * Returns the maximum x. This works even if the topleft and bottomright are switched
     * (non-Javadoc)
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxX()
     */
    @Override
    public final double getMaxX() {
        return Math.max(mTopLeftCorner.getX(), mBottomRightCorner.getX());
    }

    /* (non-Javadoc)
     * Returns the maximum y. This works even if the topleft and bottomright are switched
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxY()
     */
    @Override
    public final double getMaxY() {
        return Math.max(mTopLeftCorner.getY(), mBottomRightCorner.getY());
    }

    /**
     * Gets the top left corner point.
     *
     * @return
     */
    public final SrlPoint getTopLeftCorner() {
        return mTopLeftCorner;
    }

    /**
     * Sets the top left corner.
     *
     * @param topLeftCorner
     */
    public final void setTopLeftCorner(final SrlPoint topLeftCorner) {
        mTopLeftCorner = topLeftCorner;
    }

    /**
     * Translates the saved corner points and the subshapes.
     *
     * @param x x amount to translate
     * @param y y amount to translate
     */
    public final void translate(final double x, final double y) {
        mTopLeftCorner.translate(x, y);
        mBottomRightCorner.translate(x, y);
        translateSubShapes(x, y);
    }

    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public boolean equalsByContent(final SrlObject other) {
        if (!(other instanceof SrlRectangle)) {
            return false;
        }
        final SrlRectangle otherrectangle = (SrlRectangle) other;
        if (!getTopLeftCorner().equals(otherrectangle.getTopLeftCorner())) {
            return false;
        }
        if (!getBottomRightCorner().equals(otherrectangle.getBottomRightCorner())) {
            return false;
        }
        return true;
    }

    /**
     * Get the line segment along the top edge of this box.
     *
     * @return the segment along the top edge of this box.
     */
    public final SrlLine getTopSegment() {
        return new SrlLine(getMinX(), getMinY(), getMaxX(), getMinY());
    }

    /**
     * Get the line segment along the bottom edge of this box.
     *
     * @return The segment along the bottom edge of this box.
     */
    public final SrlLine getBottomSegment() {
        return new SrlLine(getMinX(), getMaxY(), getMaxX(), getMaxY());
    }

    /**
     * Get the line segment along the left edge of this box.
     *
     * @return The segment along the left edge of this box.
     */
    public final SrlLine getLeftSegment() {
        return new SrlLine(getMinX(), getMinY(), getMinX(), getMaxY());
    }

    /**
     * Get the line segment along the right edge of this box.
     *
     * @return The segment along the right edge of this box.
     */
    public final SrlLine getRightSegment() {
        return new SrlLine(getMaxX(), getMinY(), getMaxX(), getMaxY());
    }

    /*
     * Just sets the bounding box as itself
     * @see edu.tamu.srl.object.shape.SrlShape#calculateBBox()
     */
    @Override
    protected final void calculateBBox() {
        this.setBoundingBox(this);
    }

    /**
     * Called to calculate the convex hull of the object.
     */
    @Override protected void calculateConvexHull() {

    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.object.shape.SrlObject}.  This cloned object is only a shallow copy.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlRectangle(this);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public SrlObject deepClone() {
        return  new SrlRectangle(this);
    }

}
