package edu.tamu.srl.sketch.core.virtual;

import edu.tamu.srl.sketch.core.abstracted.AbstractSrlComponent;
import edu.tamu.srl.sketch.core.abstracted.SrlVirtualObject;

/**
 * Created by gigemjt on 11/3/14.
 * <br>
 * A bounding box is typically used to find the smallest horizontal/vertical rectangle that can fit around an
 * {@link edu.tamu.srl.sketch.core.abstracted.SrlObject}.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 * @author gigemjt
 */
@SuppressWarnings({ "PMD.CloneMethodMustImplementCloneable", "PMD.AvoidDuplicateLiterals" })
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

    /**
     * Translate the object by the amount x,y.
     *
     * @param xOffset the amount in the x direction to move the object by.
     * @param yOffset the amount in the y direction to move the object by.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void translate(final double xOffset, final double yOffset) {
        getTopLeftCorner().translate(xOffset, yOffset);
        getBottomRightCorner().translate(xOffset, yOffset);
    }

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xFactor the x-factor
     * @param yFactor the y-factor
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void scale(final double xFactor, final double yFactor) {
        getBottomRightCorner().scale(xFactor, yFactor);
        getTopLeftCorner().scale(xFactor, yFactor);
    }

    /**
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians the number of radians to rotate
     * @param xCenter the x-coordinate to rotate from
     * @param yCenter the y-coordinate to rotate from
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void rotate(final double radians, final double xCenter, final double yCenter) {
        getTopLeftCorner().rotate(radians, xCenter, yCenter);
        getBottomRightCorner().rotate(radians, xCenter, yCenter);
    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.sketch.core.abstracted.AbstractSrlComponent}.
     * This cloned object is only a shallow copy.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlBoundingBox(this);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public AbstractSrlComponent deepClone() {
        return new SrlBoundingBox(this);
    }

    /**
     * Performs a shallow equals.
     * By default this is called by the {@link #equals(Object)} method.
     *
     * @param other the other SrlObject.
     * @return true if the boundaries of the box are the same (getMinX == other.getMinX), etc
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean shallowEquals(final AbstractSrlComponent other) {
        if (!(other instanceof SrlBoundingBox)) {
            return false;
        }
        return getMaxX() == ((SrlBoundingBox) other).getMaxX() && getMaxY() == ((SrlBoundingBox) other).getMaxY()
                && getMinX() == ((SrlBoundingBox) other).getMinX() && getMinY() == ((SrlBoundingBox) other).getMinY();
    }

    /**
     * Looks deep into two components to check equality.
     * It would probably be smart to also have this call shallowEquals.
     *
     * @param other the other SrlObject.
     * @return true if shallowEquals returns true and a deep inspection shows they are equal.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean deepEquals(final AbstractSrlComponent other) {
        return shallowEquals(other)
                && mTopLeftCorner.shallowEquals(((SrlBoundingBox) other).mTopLeftCorner)
                && mBottomRightCorner.shallowEquals(((SrlBoundingBox) other).mBottomRightCorner);
    }

    /**
     * Get a Point that corresponds to the center of this component. The
     * only thing that will be stored in this Point are X and Y values. Thus,
     * you should /only/ use the interface's getX() and getY() methods, and
     * should not cast the point to a concrete implementation. Time of the point
     * is set to 0.
     *
     * @return the Point with X and Y coordinates set to the center of this
     * bounding box.
     */
    @Override public final SrlPoint getCenterPoint() {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * Get the y value for the top of the box.
     *
     * @return the y value for the top of the box.
     */
    public final double getTop() {
        return getMinY();
    }

    /**
     * Get the y value for the bottom of the box.
     *
     * @return the y value for the bottom of the box.
     */
    public final double getBottom() {
        return getMaxY();
    }

    /**
     * Get the x value for the left of the box.
     *
     * @return the x value for the left of the box.
     */
    public final double getLeft() {
        return getMinX();
    }

    /**
     * Get the x value for the right of the box.
     *
     * @return the x value for the right of the box.
     */
    public final double getRight() {
        return getMaxX();
    }

    /**
     * @return the largest X value. (larger x values are denoted as being on the right hand side of the screen.
     */
    public double getMaxX() {
        return Math.max(mTopLeftCorner.getX(), mBottomRightCorner.getX());
    }

    /**
     * @return the largest Y value. (larger Y values are denoted as being at the bottom the screen.
     */
    public double getMaxY() {
        return Math.max(mTopLeftCorner.getY(), mBottomRightCorner.getY());
    }

    /**
     * @return the smallest X value. (smaller x values are denoted as being on the left hand side of the screen.
     */
    public double getMinX() {
        return Math.min(mTopLeftCorner.getX(), mBottomRightCorner.getX());
    }

    /**
     * @return the smallest Y value. (smaller Y values are denoted as being at the top of the screen.
     */
    public double getMinY() {
        return Math.min(mTopLeftCorner.getY(), mBottomRightCorner.getY());
    }

    /**
     * @param boxes The objects we are taking the union of.
     * @return A {@link edu.tamu.srl.sketch.core.virtual.SrlBoundingBox} that represents the union.
     */
    public static SrlBoundingBox union(SrlBoundingBox... boxes) {
        double maxX = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        for (int i = 0; i < boxes.length; i++) {
            maxX = Math.max(boxes[i].getMaxX(), maxX);
            maxY = Math.max(boxes[i].getMaxY(), maxY);

            minX = Math.max(boxes[i].getMinX(), minX);
            minY = Math.max(boxes[i].getMinY(), minY);
        }
        return new SrlBoundingBox(minX, minY, maxX, maxY);
    }
}
