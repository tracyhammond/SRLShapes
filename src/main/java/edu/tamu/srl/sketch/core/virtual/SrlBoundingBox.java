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
 *
 * @author gigemjt
 */
@SuppressWarnings({ "PMD.CloneMethodMustImplementCloneable", "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods", "PMD.ExcessivePublicCount" })
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
     * @param topLeftCorner
     *         The point that is assigned as the top left corner. Should be lower in value.
     * @param bottomRightCorner
     *         The point that is assigned as the bottom right corner.  Should be larger in value.
     */
    public SrlBoundingBox(final SrlPoint topLeftCorner, final SrlPoint bottomRightCorner) {
        setTopLeftCorner(topLeftCorner);
        setBottomRightCorner(bottomRightCorner);
    }

    /**
     * Constructor.
     *
     * @param minX
     *         the smallest x value.
     * @param minY
     *         the smallest y value.
     * @param maxX
     *         the largest x value.
     * @param maxY
     *         the largest y value.
     */
    public SrlBoundingBox(final double minX, final double minY, final double maxX, final double maxY) {
        this(new SrlPoint(minX, minY), new SrlPoint(maxX, maxY));
    }

    /**
     * Copy constructor.
     *
     * @param srlBoundingBox
     *         object that is being copied.
     */
    public SrlBoundingBox(final SrlBoundingBox srlBoundingBox) {
        super(srlBoundingBox);
    }

    /**
     * @param boxes
     *         The objects we are taking the union of.
     * @return A {@link edu.tamu.srl.sketch.core.virtual.SrlBoundingBox} that represents the union.
     */
    public static SrlBoundingBox union(final SrlBoundingBox... boxes) {
        double maxX = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        for (int i = 0; i < boxes.length; i++) {
            maxX = Math.max(boxes[i].getMaxX(), maxX);
            maxY = Math.max(boxes[i].getMaxY(), maxY);

            minX = Math.min(boxes[i].getMinX(), minX);
            minY = Math.min(boxes[i].getMinY(), minY);
        }
        return new SrlBoundingBox(minX, minY, maxX, maxY);
    }

    /**
     * @param boxes
     *         The objects we are taking the intersect of.
     * @return A {@link edu.tamu.srl.sketch.core.virtual.SrlBoundingBox} that represents the intersect.
     * If no intersection exists than we return null.
     */
    public static SrlBoundingBox intersect(final SrlBoundingBox... boxes) {
        double maxX = Double.MAX_VALUE;
        double minX = Double.MIN_VALUE;
        double maxY = Double.MAX_VALUE;
        double minY = Double.MIN_VALUE;
        for (int i = 0; i < boxes.length; i++) {
            maxX = Math.min(boxes[i].getMaxX(), maxX);
            maxY = Math.min(boxes[i].getMaxY(), maxY);

            minX = Math.max(boxes[i].getMinX(), minX);
            minY = Math.max(boxes[i].getMinY(), minY);
        }
        if (minX > maxX || minY > maxY) {
            return null;
        }
        return new SrlBoundingBox(minX, minY, maxX, maxY);
    }

    /**
     * Get the SrlPoint corresponding to the bottom right corner of this bounding
     * box.
     *
     * @return the point that is the bottom right corner of this bounding box.
     */
    public final SrlPoint getBottomRightCorner() {
        return new SrlPoint(mBottomRightCorner);
    }

    /**
     * Sets the bottom right corner.
     *
     * @param bottomRightCorner
     *         The {@link edu.tamu.srl.sketch.core.virtual.SrlPoint} that represents the bottom right corner.
     */
    public final void setBottomRightCorner(final SrlPoint bottomRightCorner) {
        mBottomRightCorner = bottomRightCorner;
    }

    /**
     * Get the SrlPoint corresponding to the top-left corner of this bounding box.
     * This method assumes screen coordinates.
     *
     * @return the top left point of this bounding box.
     */
    public final SrlPoint getTopLeftCorner() {
        return new SrlPoint(mTopLeftCorner);
    }

    /**
     * Sets the top left corner.
     *
     * @param topLeftCorner
     *         The {@link edu.tamu.srl.sketch.core.virtual.SrlPoint} that represents the top left corner.
     */
    public final void setTopLeftCorner(final SrlPoint topLeftCorner) {
        mTopLeftCorner = topLeftCorner;
    }

    /**
     * Translate the object by the amount x,y.
     *
     * @param xOffset
     *         the amount in the x direction to move the object by.
     * @param yOffset
     *         the amount in the y direction to move the object by.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void translate(final double xOffset, final double yOffset) {
        getTopLeftCorner().translate(xOffset, yOffset);
        getBottomRightCorner().translate(xOffset, yOffset);
    }

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xFactor
     *         the x-factor
     * @param yFactor
     *         the y-factor
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void scale(final double xFactor, final double yFactor) {
        getBottomRightCorner().scale(xFactor, yFactor);
        getTopLeftCorner().scale(xFactor, yFactor);
    }

    /**
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians
     *         the number of radians to rotate
     * @param xCenter
     *         the x-coordinate to rotate from
     * @param yCenter
     *         the y-coordinate to rotate from
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
     * @param other
     *         the other SrlObject.
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
     * @param other
     *         the other SrlObject.
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
       return new SrlPoint((mTopLeftCorner.getX() + mBottomRightCorner.getX()) / 2.0,
               (mTopLeftCorner.getY() + mBottomRightCorner.getY()) / 2.0);
    }

    /**
     * Attempts to find the closest distance to this other component.
     *
     * @param srlComponent
     *         the component we are trying to find this distance to.
     * @return the distance between the components.
     * <b>NOTE: due to possible heuristics used this method is not commutative.</b>  <pre>Meaning that:
     *
     * <code>shape1.distance(shape2) == shape2.distance(shape1);</code>
     * May be false.
     * </pre>
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double distance(final AbstractSrlComponent srlComponent) {
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
     * @return The width of the bounding box.
     */
    public final double getWidth() {
        return getMaxX() - getMinX();
    }

    /**
     * @return the height of the bounding box.
     */
    public final double getHeight() {
        return getMaxY() - getMinY();
    }

    /**
     * @return the largest X value. (larger x values are denoted as being on the right hand side of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double getMaxX() {
        return Math.max(mTopLeftCorner.getX(), mBottomRightCorner.getX());
    }

    /**
     * @return the largest Y value. (larger Y values are denoted as being at the bottom the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double getMaxY() {
        return Math.max(mTopLeftCorner.getY(), mBottomRightCorner.getY());
    }

    /**
     * @return the smallest X value. (smaller x values are denoted as being on the left hand side of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double getMinX() {
        return Math.min(mTopLeftCorner.getX(), mBottomRightCorner.getX());
    }

    /**
     * @return the smallest Y value. (smaller Y values are denoted as being at the top of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double getMinY() {
        return Math.min(mTopLeftCorner.getY(), mBottomRightCorner.getY());
    }

    /**
     * Get the area of the bounding box.
     *
     * @return the area of the bounding box.
     */
    public final double getArea() {
        return getWidth() * getHeight();
    }

    /**
     * Get the perimeter of the bounding box.
     *
     * @return the perimeter of the bounding box.
     */
    public final double getPerimeter() {
        return 2 * getWidth() + 2 * getHeight();
    }

    /**
     * This bounding box is above the given {@code y} value.
     *
     * @param y
     *         the y value to compare against.
     * @return {@code true} if the bottom edge of the bounding box is above the
     * given {@code y} value; {@code false} otherwise.
     */
    @SuppressWarnings("PMD.ShortVariable")
    public final boolean isAbove(final double y) {
        return getBottom() < y;
    }

    /**
     * This bounding box is below the given {@code y} value.
     *
     * @param y
     *         the y value to compare against.
     * @return {@code true} if the top edge of the bounding box is below the
     * given {@code y} value; {@code false} otherwise.
     */
    @SuppressWarnings("PMD.ShortVariable")
    public final boolean isBelow(final double y) {
        return getTop() > y;
    }

    /**
     * This bounding box is to the left of the given {@code x} value.
     *
     * @param x
     *         the x value to compare against.
     * @return {@code true} if the right edge of the bounding box is to the left
     * of the given {@code x} value; {@code false} otherwise.
     */
    @SuppressWarnings("PMD.ShortVariable")
    public final boolean isLeftOf(final double x) {
        return getRight() < x;
    }

    /**
     * This bounding box is to the right of the given {@code x} value.
     *
     * @param x
     *         the x value to compare against.
     * @return {@code true} if the left edge of the bounding box is to the right
     * of the given {@code x} value; {@code false} otherwise.
     */
    @SuppressWarnings("PMD.ShortVariable")
    public final boolean isRightOf(final double x) {
        return getLeft() > x;
    }

    /**
     * Get the percent of the bounding box that is contained within the other
     * bounding box. If this bounding box is outside of other's bounding box
     * then we return 0.0. If it is fully contained within other's bounding box
     * then we return 1.0. If it is partially contained in other's bounding box
     * then we return a value between 0.0 and 1.0 denoting the percentage of the
     * bounding box that is contained within other's bounding box.
     *
     * @param other
     *         bounding box of other shape
     * @return percentage of bounding box that is contained within other's
     * bounding box
     */
    public final double getPercentContained(final SrlBoundingBox other) {
        if (other.contains(this)) {
            return 1.0;
        }
        if (!other.intersects(this)) {
            return 0.0;
        }
        final SrlBoundingBox intersection = SrlBoundingBox.intersect(this, other);
        final double area = intersection.getWidth() * intersection.getHeight();
        return area / getArea();
    }

    /**
     * Get the length of the diagonal of the bounding box.
     *
     * @return length of the diagonal of the bounding box.
     */
    public final double getDiagonalLength() {
        return Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight());
    }

    /**
     * Get the angle of the diagonal of the bounding box, i.e. the angle between
     * the line along the bottom of the bounding box and the line through the
     * bottom left and top right corners of the box.
     *
     * @return angle of the diagonal of the bounding box.
     */
    public final double getDiagonalAngle() {
        return Math.atan2(getHeight(), getWidth());
    }

    /**
     * @param point
     *         The point we are checking to see if it is contained within the bounding box
     * @return true if the point is contained within the bounding box.
     * Points on the edge of the bounding box are considered to be contained.
     */
    public final boolean contains(final SrlPoint point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * @param x
     *         The specified X coordinate.
     * @param y
     *         The specified Y coordinate.
     * @return true if the point is contained within the bounding box.
     * Points on the edge of the bounding box are considered to be contained.
     */
    @SuppressWarnings("PMD.ShortVariable")
    public final boolean contains(final double x, final double y) {
        return x <= this.getMaxX() && x >= this.getMinX()
                && y >= this.getMinY() && y <= this.getMaxY();
    }

    /**
     * @param other
     *         What we are checking to see if it is contained.
     * @return true if the other {@link edu.tamu.srl.sketch.core.virtual.SrlBoundingBox}
     * is contained within this {@link edu.tamu.srl.sketch.core.virtual.SrlBoundingBox}.
     * Points on the edge of the bounding box are considered to be contained.
     */
    public final boolean contains(final SrlBoundingBox other) {
        return this.contains(other.getTopLeftCorner()) && this.contains(other.getBottomRightCorner());
    }

    /**
     * @param other
     *         What we are checking to see if it intersects
     * @return true if the other {@link edu.tamu.srl.sketch.core.virtual.SrlBoundingBox}
     * intersects within this {@link edu.tamu.srl.sketch.core.virtual.SrlBoundingBox}.
     * Points on the edge of the bounding box are considered to be intersecting.
     */
    public final boolean intersects(final SrlBoundingBox other) {
        return this.contains(other.getTopLeftCorner()) || this.contains(other.getBottomRightCorner());
    }

    /**
     * Get the SrlPoint corresponding to the top-center point on this bounding box.
     *
     * @return the top-center point of this bounding box, or center point on the
     * top edge of this bounding box.
     */
    public final SrlPoint getTopCenterPoint() {
        return new SrlPoint(getCenterX(), getTop());
    }

    /**
     * Get the SrlPoint corresponding to the top-right corner of this bounding
     * box.
     *
     * @return The top-right corner of this bounding box.
     */
    public final SrlPoint getTopRightCorner() {
        return new SrlPoint(getRight(), getTop());
    }

    /**
     * Get the SrlPoint corresponding to the center of the left edge of this
     * bounding box.
     *
     * @return the Point that is the center of the left edge of this bounding
     * box.
     */
    public final SrlPoint getLeftCenterPoint() {
        return new SrlPoint(getLeft(), getCenterY());
    }

    /**
     * Get the SrlPoint corresponding to the center point of the right edge of
     * this bounding box.
     *
     * @return the point that is the center of the right edge of this bounding
     * box.
     */
    public final SrlPoint getRightCenterPoint() {
        return new SrlPoint(getRight(), getCenterY());
    }

    /**
     * Get the SrlPoint corresponding to the bottom left corner of this bounding
     * box.
     *
     * @return the point that is the bottom left corner of this bounding box.
     */
    public final SrlPoint getBottomLeftCorner() {
        return new SrlPoint(getLeft(), getBottom());
    }

    /**
     * Get the SrlPoint corresponding to the center point on the bottom edge of
     * this bounding box.
     *
     * @return the point that is the center of the bottom edge of this bounding
     * box.
     */
    public final SrlPoint getBottomCenterPoint() {
        return new SrlPoint(getCenterX(), getBottom());
    }

    /**
     * Make a bigger bounding box that is {@code amt} larger in all 4
     * directions. So the net change in x and y directions is both 2 {@code amt}
     * . {@code amt} is not allowed to be negative and uses the absolute value.
     *
     * @param amt
     *         the amount to expand ({@code minX} - {@code amt}, {@code maxX}
     *         + {@code amt}, etc.).
     * @return the expanded bounding box.
     */
    public final SrlBoundingBox expand(final double amt) {
        final double absAmt = Math.abs(amt);
        return new SrlBoundingBox(getMinX() - absAmt, getMinY() - absAmt, getMaxX() + absAmt, getMaxY() + absAmt);
    }

    /**
     * Make a bigger bounding box using a multiplicative factor. The center is
     * still in the same spot {@code newWidth} = getWidth() * {@code factor};
     * same for height.
     *
     * @param factor
     *         the amount to expand.
     * @return the expanded bounding box.
     */
    public final SrlBoundingBox grow(final double factor) {
        return new SrlBoundingBox(getMinX() - getWidth() * factor / 2.0,
                getMinY() - getHeight() * factor / 2.0, getMaxX() + getWidth() * factor / 2.0,
                getMaxY() + getHeight() * factor / 2.0);
    }

    /**
     * Make a bigger bounding box using a multiplicative factor. The center is
     * still in the same spot {@code newWidth} = getWidth() * {@code factor}.
     *
     * @param factor
     *         the amount to expand.
     * @return the expanded bounding box.
     */
    public final SrlBoundingBox growWidth(final double factor) {
        return new SrlBoundingBox(getMinX() - getWidth() * factor / 2.0,
                getMinY(), getMaxX() + getWidth() * factor / 2.0, getMaxY());
    }

    /**
     * Make a bigger bounding box using a multiplicative factor. The center is
     * still in the same spot {@code newHeight} = getHeight() * {@code factor}.
     *
     * @param factor
     *         the amount to expand.
     * @return the expanded bounding box.
     */
    public final SrlBoundingBox growHeight(final double factor) {
        return new SrlBoundingBox(getMinX(), getMinY() - getHeight() * factor
                / 2.0, getMaxX(), getMaxY()
                + getHeight()
                * factor / 2.0);
    }

    /**
     * Make a smaller bounding box that is {@code amt} smaller in all 4
     * directions. The net change in x and y directions is 2 * {@code amt}.
     * {@code amt} is not allowed to be negative, and this method uses the
     * absolute value. If the bounding box is too small and contracting by
     * {@code amt} would result in a bounding box of size &lt; 0, this method
     * returns a bounding box of size 0 at the center of this bounding box.
     *
     * @param amt
     *         the amount to contract by (&gt; 0).
     * @return the contracted bounding box.
     */
    public final SrlBoundingBox contract(final double amt) {
        final double absAmt = Math.abs(amt);

        SrlBoundingBox ret = null;

        // too small to shrink so we return a point sized bounding box.
        if (this.getHeight() < 2 * absAmt || this.getWidth() < 2 * absAmt) {
            ret = new SrlBoundingBox(this.getCenterX(), this.getCenterY(), this
                    .getCenterX(), this.getCenterY());
        } else {
            ret = new SrlBoundingBox(this.getMinX() + absAmt, this.getMinY() + absAmt, this.getMaxX() - absAmt, this.getMaxY() - absAmt);
        }
        return ret;
    }

    /**
     * @return a new bounding that is expanded on all sides by 1.
     */
    public final SrlBoundingBox increment() {
        return new SrlBoundingBox(this.getMinX() - 1, this.getMinY() - 1, this.getMaxX() + 1, this.getMaxY() + 1);
    }

    /**
     * Returns the X coordinate of the center of the framing
     * rectangle of the <code>Shape</code> in <code>double</code>
     * precision.
     *
     * @return the X coordinate of the center of the framing rectangle
     * of the <code>Shape</code>.
     */
    public final double getCenterX() {
        return (getMinX() + getMaxX()) / 2.0;
    }

    /**
     * Returns the Y coordinate of the center of the framing
     * rectangle of the <code>Shape</code> in <code>double</code>
     * precision.
     *
     * @return the Y coordinate of the center of the framing rectangle
     * of the <code>Shape</code>.
     */
    public final double getCenterY() {
        return (getMinY() + getMaxY()) / 2.0;
    }
}
