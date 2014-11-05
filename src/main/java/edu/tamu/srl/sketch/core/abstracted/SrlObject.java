package edu.tamu.srl.sketch.core.abstracted;

import edu.tamu.srl.sketch.core.virtual.SrlBoundingBox;
import edu.tamu.srl.sketch.core.virtual.SrlConvexHull;
import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    /**
     * Default constructor.
     * <p/>
     * Creates an object with an id and a time.
     */
    public SrlObject() {
        super();
        mAttributes = new HashMap<>();
    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     *
     * @param o the object that is being copied.
     */
    public SrlObject(final SrlObject o) {
        super(o);
        this.mAttributes = o.getAttributes();
        this.mBoundingBox = o.getBoundingBox();
        this.mConvexHull = o.getConvexHull();
        this.mIsUserCreated = o.isUserCreated();
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param id   The unique identifier of the shape.
     */
    public SrlObject(final long time, final UUID id) {
        super(time, id);
        mAttributes = new HashMap<>();
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param id   The unique identifier of the shape.
     * @param isUserCreated true if the user created the stroke instead of the computer.
     */
    public SrlObject(final long time, final UUID id, final boolean isUserCreated) {
        this(time, id);
        this.mIsUserCreated = isUserCreated;
    }

    /**
     * Copy of the attributes.
     *
     * @return a clone of the attribute map
     */
    public final Map<String, String> getAttributes() {
        final HashMap<String, String> attrcopy = new HashMap<String, String>();
        if (mAttributes != null) {
            for (Map.Entry<String, String> entry : mAttributes.entrySet()) {
                attrcopy.put(entry.getKey(), entry.getValue());
            }
        }
        return attrcopy;
    }

    /**
     * Get the bounding box of the object.
     * <p/>
     * Calculates the bounding box if the rectangle is null and does not exist using {@link #calculateBBox()}.
     *
     * @return the bounding box of the object.  This method will never return null.
     */
    public final SrlBoundingBox getBoundingBox() {
        if (this.mBoundingBox == null) {
            this.calculateBBox();
        }
        // if calculating the bounding box did not set the bounding box.
        if (this.mBoundingBox == null) {
            this.mBoundingBox = new SrlBoundingBox(new SrlPoint(getMinX(), getMinY()),
                    new SrlPoint(getMinX() + getWidth(), getMinY() + getHeight()));
        }
        return this.mBoundingBox;
    }

    /**
     * Gets the convex hull.
     *
     * @return the convex hull
     */
    public final SrlConvexHull getConvexHull() {
        if (this.mConvexHull == null) {
            this.calculateConvexHull();
        }
        return mConvexHull;
    }

    /**
     * An object can be created by a user (like drawing a shape, or speaking a
     * phrase) or it can be created by a system (like a recognition of a higher
     * level shape) default is false if not explicitly set.
     *
     * @return true if a user created the shape.
     */
    public final boolean isUserCreated() {
        return mIsUserCreated;
    }

    /**
     * Calculates the bounding box.
     */
    protected abstract void calculateBBox();

    /**
     * Called to calculate the convex hull of the object.
     */
    protected abstract void calculateConvexHull();

    /**
     * Returns the height of the object.
     *
     * @return the height of the object
     */
    public final double getHeight() {
        return getMaxY() - getMinY();
    }

    /**
     * This returns the length of the diagonal of the bounding box. This might
     * be a better measure of perceptual size than area
     *
     * @return Euclidean distance of bounding box diagonal
     */
    public final double getLengthOfDiagonal() {
        return Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth());
    }

    /**
     * @return the largest X value. (larger x values are denoted as being on the right hand side of the screen.
     */
    public abstract double getMaxX();

    /**
     * @return the largest Y value. (larger Y values are denoted as being at the bottom the screen.
     */
    public abstract double getMaxY();

    /**
     * @return the smallest X value. (smaller x values are denoted as being on the left hand side of the screen.
     */
    public abstract double getMinX();

    /**
     * @return the smallest Y value. (smaller Y values are denoted as being at the top of the screen.
     */
    public abstract double getMinY();

    /**
     * This function just returns the same thing as the length of the diagonal
     * as it is a good measure of size.
     *
     * @return size of the object.
     */
    public final double getSize() {
        return getLengthOfDiagonal();
    }

    /**
     * Returns the width of the object.
     *
     * @return the width of the object.
     */
    public final double getWidth() {
        return getMaxX() - getMinX();
    }

    /**
     * Checks if this object has the given attribute.
     *
     * @param key The string name of the key of the attribute
     * @return true if this object has the given key
     */
    public final boolean hasAttribute(final String key) {
        return mAttributes.containsKey(key);
    }

    /**
     * An object can be created by a user (like drawing a shape, or speaking a
     * phrase) or it can be created by a system (like a recognition of a higher
     * level shape).
     *
     * @param isUserCreated true if the user created the shape, else false.
     */

    public final void setUserCreated(final boolean isUserCreated) {
        mIsUserCreated = isUserCreated;
    }

    /**
     * Removes the value and key of the specified attribute.
     *
     * @param key the name of the attribute
     * @return the value for the removed key, or null if key did not exist
     */
    public final String removeAttribute(final String key) {
        return mAttributes.remove(key);
    }

    /**
     * Sets an attribute value. Will overwrite any value currently set for that
     * attribute.
     *
     * @param key   attribute name (Must be string)
     * @param value attribute value (Must be string)
     * @return the old value of the attribute, or null if none was set
     */
    public final String setAttribute(final String key, final String value) {
        return mAttributes.put(key, value);
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
     * This bounding box is above the given value.
     *
     * @param y the y value to compare against.
     * @return if the bottom edge of the bounding box is above the
     * given value; otherwise.
     */
    public final boolean isAbove(final double y) {
        return getBottom() < y;
    }

    /**
     * This bounding box is below the given value.
     *
     * @param y the y value to compare against.
     * @return if the top edge of the bounding box is below the
     * given value; otherwise.
     */
    public final boolean isBelow(final double y) {
        return getTop() > y;
    }

    /**
     * This bounding box is to the left of the given value.
     *
     * @param x the x value to compare against.
     * @return if the right edge of the bounding box is to the left
     * of the given value;  otherwise.
     */
    public final boolean isLeftOf(final double x) {
        return getRight() < x;
    }

    /**
     * This bounding box is to the right of the given value.
     *
     * @param x the x value to compare against.
     * @return if the left edge of the bounding box is to the right
     * of the given value; otherwise.
     */
    public final boolean isRightOf(final double x) {
        return getLeft() > x;
    }

    /**
     * Get the angle of the diagonal of the bounding box, i.e. the angle between
     * the line along the bottom of the bounding box and the line through the
     * bottom left and top right corners of the box.
     *
     * @return angle of the diagonal of the bounding box.
     */
    public final double getBoundingBoxDiagonalAngle() {
        return Math.atan2(getHeight(), getWidth());
    }
}