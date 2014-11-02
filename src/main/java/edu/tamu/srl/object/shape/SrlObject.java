package edu.tamu.srl.object.shape;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Object data class.
 *
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class SrlObject implements Comparable<SrlObject> {

    /**
     * Default comparator using time.
     */
    private static Comparator<SrlObject> timeComparator;

    /**
     * Each object has a unique ID associated with it.
     */
    private final UUID mId;

    /**
     * The creation time of the object.
     */
    private final long mTime;

    /**
     * Map of miscellaneous attributes (to store any attributes given for points
     * in a SketchML file that are not saved in other variables here).
     */
    private Map<String, String> mAttributes = new HashMap<String, String>();

    /**
     * The bounding box of the objects.
     */
    private transient SrlRectangle mBoundingBox;

    /**
     * The convex hull of the objects.
     */
    private transient Polygon mConvexHull;

    /**
     * Description of the object.
     */
    private String mDescription = "";

    /**
     * An object can be created by a user (like drawing a shape, or speaking a
     * phrase) or it can be created by a system (like a recognition of a higher
     * level shape).
     */
    private boolean mIsUserCreated = false;

    /**
     * The name of the object, such as "triangle1".
     */
    private String mName = "";

    /**
     * The type of the object (such as "Line", "Stroke", etc.).
     */
    private String mType = "";

    /**
     * Default constructor.
     * <p/>
     * Creates an object with an id and a time.
     */
    public SrlObject() {
        mId = UUID.randomUUID();
        mTime = System.currentTimeMillis();
        mBoundingBox = null;
        mConvexHull = null;

    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     *
     * @param o the object that is being copied.
     */
    public SrlObject(final SrlObject o) {
        this.mTime = o.getTime();
        this.mAttributes = o.getAttributes();
        this.mBoundingBox = o.getBoundingBox();
        this.mConvexHull = o.getConvexHull();
        this.mDescription = o.getDescription();
        this.mId = o.getId();
        this.mIsUserCreated = o.isUserCreated();
        this.mName = o.getName();
        this.mType = o.getType();
    }

    /**
     * Creates a new object with a name, description and type.
     *
     * @param name        The name of the shape (shape1, shape2)
     * @param description A description of what the shape is, useful when debugging.
     * @param type        The interpretation of the shape.
     */
    public SrlObject(final String name, final String description, final String type) {
        this();
        setName(name);
        setType(type);
        setDescription(description);
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param id   The unique identifier of the shape.
     */
    public SrlObject(final long time, final UUID id) {
        this.mId = id;
        this.mTime = time;
        mBoundingBox = null;
        mConvexHull = null;
    }

    /**
     * @param time creates an object with the given time but a random id.
     */
    public SrlObject(final long time) {
        this(time, UUID.randomUUID());
    }

    /**
     * Gets the comparator for the time values. Compares two objects based on
     * their time stamps. Might be better to compare based on another method,
     * but haven't decided yet what that is.
     * <p/>
     * The Comparator will return 0 if the times are equal.
     *
     * @return the comparator for the time values
     */
    public static Comparator<SrlObject> getTimeComparator() {

        if (timeComparator == null) {
            timeComparator = new Comparator<SrlObject>() {
                @Override
                public int compare(final SrlObject arg0, final SrlObject arg1) {
                    return (arg0.getTime() < arg1.getTime()) ? -1 : (arg0
                            .getTime() > arg1.getTime()) ? 1 : 0;
                }
            };
        }
        return timeComparator;
    }

    /**
     * Gets the comparator for the x-values.
     *
     * @return the comparator for the x-values
     */
    public static Comparator<SrlObject> getXComparator() {

        return new Comparator<SrlObject>() {

            @Override
            public int compare(final SrlObject arg0, final SrlObject arg1) {
                return Double.compare(arg0.getBoundingBox().getMinX(), arg1
                        .getBoundingBox().getMinX());
            }

        };
    }

    /**
     * Gets the comparator for the y-values.
     *
     * @return the comparator for the y-values
     */
    public static Comparator<SrlObject> getYComparator() {

        return new Comparator<SrlObject>() {

            @Override
            public int compare(final SrlObject arg0, final SrlObject arg1) {
                return Double.compare(arg0.getBoundingBox().getMinY(), arg1
                        .getBoundingBox().getMinY());
            }

        };
    }

    /**
     * Applies a 2D affine transform.
     *
     * @param xform the 2D affine transform
     */
    public final void applyTransform(final AffineTransform xform) {
        applyTransform(xform, new HashSet<SrlObject>());
    }

    /**
     * Applies a 2D affine transform.
     *
     * @param xform   the 2D affine transform
     * @param xformed the SComponent objects to transform to (???)
     */
    protected abstract void applyTransform(AffineTransform xform,
            Set<SrlObject> xformed);

    /**
     * Calculates the bounding box.
     */
    protected abstract void calculateBBox();

    /**
     * Called to calculate the convex hull of the object.
     */
    protected abstract void calculateConvexHull();

    /**
     * Default uses time. You can also use x or y to compare.
     *
     * @param o The object that this instance is being compared to.
     * @return a value that depends on the result of the comparator used.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public int compareTo(final SrlObject o) {
        return getTimeComparator().compare(this, o);
    }

    /**
     * @return A cloned object that is an instance of {@link SrlObject}.  This cloned object is only a shallow copy.
     */
    @Override
    public abstract Object clone();

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    public abstract SrlObject deepClone();

    /**
     * Return the distance from the point specified by (x,y) to the center of this object.
     *
     * @param x the x value of the other point
     * @param y the y value of the other point
     * @return the distance
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double distance(final double x, final double y) {
        final double xdiff = x - getCenterX();
        final double ydiff = y - getCenterY();
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }

    /**
     * Return the distance from point rp to this point.
     *
     * @param o the other point
     * @return the distance
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double distance(final SrlObject o) {
        return distance(o.getCenterX(), o.getCenterY());
    }

    @Override
    /**
     * Checks if two {@link SrlObject} are equal.
     * This default implementation only checks that the two ids equal.
     * @param other the other object.
     * @return by default. true if the two ids are equal.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public boolean equals(final Object other) {
        if (!(other instanceof SrlObject)) {
            return false;
        }
        return getId().equals(((SrlObject) other).getId());
    }

    /**
     * Looks deep into two components to check equality.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    public abstract boolean equalsByContent(SrlObject other);

    /**
     * Flags an external update. Resets the bounding box and convex hull when
     * moved
     */
    @SuppressWarnings("checkstyle:designforextension")
    public void flagExternalUpdate() {
        mBoundingBox = null;
        mConvexHull = null;
    }

    /**
     * Returns the length times the height See also getLengthOfDiagonal().
     * @return area of shape.
     */
    public final double getArea() {
        return getHeight() * getWidth();
    }

    /**
     * Gets the value for the given attribute.
     *
     * @param key String name of key
     * @return the value of the given attribute, or null if that attribute is
     * not set.
     */
    public final String getAttribute(final String key) {
        return mAttributes.get(key);
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
    public final SrlRectangle getBoundingBox() {
        if (this.mBoundingBox == null) {
            this.calculateBBox();
        }
        // if calculating the bounding box did not set the bounding box.
        if (this.mBoundingBox == null) {
            this.mBoundingBox = new SrlRectangle(new SrlPoint(getMinX(), getMinY()),
                    new SrlPoint(getMinX() + getWidth(), getMinY() + getHeight()));
        }
        return this.mBoundingBox;
    }

    /**
     * @param r a {@link edu.tamu.srl.object.shape.SrlRectangle}.  This method will accept a null value.
     */
    public final void setBoundingBox(final SrlRectangle r) {
        mBoundingBox = r;
    }

    /**
     * Returns the bounding box of the object as a clone to the actual instance.
     * If the bounding box has not been calculated or one does not exist this may return null.
     * <p/>
     * If a method that never returns null is needed call {@link #getBoundingBox()}.
     *
     * @return A clone of the internal bounding box.  null if the internal bounding box is null.
     */
    protected final SrlRectangle getRawBoundingBox() {
        if (this.mBoundingBox == null) {
            return null;
        }
        return (SrlRectangle) this.mBoundingBox.clone();
    }

    /**
     * Gets the center point of the stroke.
     *
     * @return the center of the bounding box.
     */
    public final SrlPoint getCenter() {
        return new SrlPoint(getCenterX(), getCenterY());
    }

    /**
     * Returns the center x of a shape.
     *
     * @return center x of a shape
     */
    public final double getCenterX() {
        return (getMinX() + getMaxX()) / 2.0;
    }

    /**
     * Returns the center y of a shape.
     *
     * @return center y of a shape
     */
    public final double getCenterY() {
        return (getMinY() + getMaxY()) / 2.0;
    }

    /**
     * Gets the convex hull.
     *
     * @return the convex hull
     */
    public final Polygon getConvexHull() {
        return mConvexHull;
    }

    /**
     * @param p a {@link java.awt.Polygon}.  This method will accept a null value.
     */
    public final void setConvexHull(final Polygon p) {
        mConvexHull = p;
    }

    /**
     * Get the description for this object.
     *
     * @return The description for this object.
     */
    public final String getDescription() {
        return mDescription;
    }

    /**
     * Set the description for this object.
     *
     * @param description The description of the shape.  This can be useful for debugging.
     */
    public final void setDescription(final String description) {
        mDescription = description;
    }

    /**
     * Returns the height of the object.
     *
     * @return the height of the object
     */
    public final double getHeight() {
        return getMaxY() - getMinY();
    }

    /**
     * @return unique UUID for an object
     */
    public final UUID getId() {
        return mId;
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
     * An object can have a name, such as "triangle1".
     *
     * @return the string name of the object
     */
    public final String getName() {
        return mName;
    }

    /**
     * An object can have a name, such as "triangle1".
     *
     * @param name object name
     */
    public final void setName(final String name) {
        mName = name;
    }

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
     * Gets the time associated with the object. The default time is the time it
     * was created
     *
     * @return the time the object was created.
     */
    public final long getTime() {
        return mTime;
    }

    /**
     * @return The Type for this object.
     */
    public final String getType() {
        return mType;
    }

    /**
     * Set the Type for this object.
     *
     * @param type The interpretation of the object.
     */
    public final void setType(final String type) {
        mType = type;
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

    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public int hashCode() {
        return getId().hashCode();
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
     * Translate the SComponent by the given x- and y-increment.
     *
     * @param xincrement
     *            the x-increment
     * @param yincrement
     *            the y-increment
     */
    // public void translate(double xincrement, double yincrement) {

    // applyTransform(AffineTransform.getTranslateInstance(xincrement,
    // yincrement));
    // }

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
     * Rotates the SrlObject from the origin.
     *
     * @param radians the number of radians to rotate
     */
    public final void rotate(final double radians) {
        applyTransform(AffineTransform.getRotateInstance(radians));
    }

    /**
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians the number of radians to rotate
     * @param xCenter the x-coordinate to rotate from
     * @param yCenter the y-coordinate to rotate from
     */
    public final void rotate(final double radians, final double xCenter, final double yCenter) {
        applyTransform(AffineTransform.getRotateInstance(radians, xCenter,
                yCenter));
    }

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xfactor the x-factor
     * @param yfactor the y-factor
     */
    @SuppressWarnings("checkstyle:designforextension")
    public void scale(final double xfactor, final double yfactor) {
        applyTransform(AffineTransform.getScaleInstance(xfactor, yfactor));
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
     * Can be overwritten, but doesn't have to be return the name and
     * description of object.
     * @return By default returns the name and description of the object.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public String toString() {
        return "SrlObject Name:" + getName() + " Description: "
                + getDescription();
    }

    /**
     * String that provides the name, description and guid.
     *
     * @return by default a String that provides the name, description and {@link UUID}.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public String toStringDetailed() {
        return "Type:" + getType() + " Name: " + getName() + " Description:"
                + getDescription() + " UUID:" + getId();
    }

    /**
     * Translate the object by the amount x,y.
     *
     * @param x the amount in the x direction to move the object by.
     * @param y the amount in the y direction to move the object by.
     */
    public abstract void translate(final double x, final double y);

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
