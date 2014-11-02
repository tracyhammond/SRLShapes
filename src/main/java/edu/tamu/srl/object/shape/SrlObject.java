package edu.tamu.srl.object.shape;

import edu.tamu.srl.settings.IdGenerator;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
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
public abstract class SrlObject implements Comparable<SrlObject>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
	 * Default comparator using time.
	 */
	private static Comparator<SrlObject> timeComparator;


    /**
     * Map of miscellaneous attributes (to store any attributes given for points
     * in a SketchML file that are not saved in other variables here).
     */
    private HashMap<String, String> mAttributes = new HashMap<String, String>();

    /**
     * The bounding box of the objects
     */

    /**
     * Stores the color of the object
     */
    private Color mColor = null;
    private transient SrlRectangle mBoundingBox;

    /**
     * The convex hull of the objects
     */
    private transient Polygon mConvexHull;

    /**
     * Description of the object
     */
    private String mDescription = "";

    /**
     * Each object has a unique ID associated with it.
     */
    private UUID mId = UUID.randomUUID();

    /**
     * An object can be created by a user (like drawing a shape, or speaking a
     * phrase) or it can be created by a system (like a recognition of a higher
     * level shape)
     */
    private boolean mIsUserCreated = false;

    /**
     * The name of the object, such as "triangle1"
     */
    private String mName = "";

    /**
     * The creation time of the object.
     */
    private long mTime = (new Date()).getTime();
    // System.currentTimeMillis();

    /**
     * The type of the object (such as "Line", "Stroke", etc.)
     */
    private String mType = "";

	/**
	 * Gets the comparator for the time values. Compares two objects based on
	 * their time stamps. Might be better to compare based on another method,
	 * but haven't decided yet what that is.
	 *
     * The Comparator will return 0 if the times are equal.
     *
	 * @return the comparator for the time values
	 */
	public static Comparator<SrlObject> getTimeComparator() {

		if (timeComparator == null)
			timeComparator = new Comparator<SrlObject>() {
				@Override
				public int compare(final SrlObject arg0, final SrlObject arg1) {
					return (arg0.getTime() < arg1.getTime()) ? -1 : (arg0
							.getTime() > arg1.getTime()) ? 1 : 0;
				}
			};
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

	public SrlObject() {
		setId(IdGenerator.nextId());
		mBoundingBox = null;
		mConvexHull = null;
	}

	public SrlObject(SrlObject o) {
		this.mAttributes = o.getAttributes();
		this.mBoundingBox = o.getBoundingBox();
		this.mColor = o.getColor();
		this.mConvexHull = o.getConvexHull();
		this.mDescription = o.getDescription();
		this.mId = o.getId();
		this.mIsUserCreated = o.isUserCreated();
		this.mName = o.getName();
		this.mTime = o.getTime();
		this.mType = o.getType();
	}

	/**
	 * A constructor to create srlobject
	 * 
	 * @param color
	 * @param name
	 * @param description
	 * @param type
	 */
	public SrlObject(Color color, String name, String description, String type) {
		setColor(color);
		setName(name);
		setType(type);
		setDescription(description);
	}

	/**
	 * Applies a 2D affine transform.
	 * 
	 * @param xform
	 *            the 2D affine transform
	 */
	public void applyTransform(AffineTransform xform) {

		applyTransform(xform, new HashSet<SrlObject>());
	}

	/**
	 * Applies a 2D affine transform.
	 * 
	 * @param xform
	 *            the 2D affine transform
	 * @param xformed
	 *            the SComponent objects to transform to (???)
	 */
	protected abstract void applyTransform(AffineTransform xform,
			Set<SrlObject> xformed);

	/**
	 * Calculates the bounding box.
	 */
	protected abstract void calculateBBox();

	/**
	 * Default uses time. You can also use x or y to compare
	 */
	public int compareTo(SrlObject o) {
		return getTimeComparator().compare(this, o);
	}

	/**
	 * Copies what A is into what B is
	 * 
	 * @param A
	 * @param B
	 */
	protected void copyAIntoB(SrlObject A, SrlObject B) {
		B.mId = A.mId;
		B.mIsUserCreated = A.mIsUserCreated;
		B.mName = A.mName;
		B.mTime = A.mTime;
		B.mColor = A.mColor;
		B.mAttributes = A.getAttributes();
		B.mBoundingBox = A.getBoundingBox();
		B.mConvexHull = A.getConvexHull();
	}

	/**
	 * Copy the information from the argument into this object
	 * 
	 * @param unchangedObject
	 */
	protected void copyFrom(SrlObject unchangedObject) {
		copyAIntoB(unchangedObject, this);

	}

	/**
	 * Clones all of the information to the object sent in
	 * 
	 * @param editableObject
	 *            the new clone object
	 * @return the same cloned object (superfluous return)
	 */
	protected void copyInto(SrlObject editableObject) {
		copyAIntoB(this, editableObject);
	}

	/**
	 * Return the distance from the point specified by (x,y) to this point
	 * 
	 * @param x
	 *            the x value of the other point
	 * @param y
	 *            the y value of the other point
	 * @return the distance
	 */
	public double distance(double x, double y) {
		double xdiff = x - getCenterX();
		double ydiff = y - getCenterY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	/**
	 * Return the distance from point rp to this point.
	 * 
	 * @param o
	 *            the other point
	 * @return the distance
	 */
	public double distance(SrlObject o) {
		return distance(o.getCenterX(), o.getCenterY());
	}

	public boolean equals(SrlObject o) {
		if (getId().equals(o.getId())) {
			return true;
		}
		return false;
	}

	/**
	 * Looks deep into two components to check equality.
	 * 
	 * @param other
	 *            the other SComponent
	 * @return true if content is equal, false otherwise
	 */
	public abstract boolean equalsByContent(SrlObject other);

	/**
	 * Flags an external update. Resets the bounding box and convex hull when
	 * moved
	 */
	public void flagExternalUpdate() {
		setTime(-1);
		mBoundingBox = null;
		mConvexHull = null;
	}

	/**
	 * Returns the length times the height See also getLengthOfDiagonal() 
	 * returns area of shape
	 */
	public double getArea() {
		return getHeight() * getWidth();
	}

	/**
	 * Gets the value for the given attribute.
	 * 
	 * @param key
	 *            String name of key
	 * @return the value of the given attribute, or null if that attribute is
	 *         not set.
	 */
	public String getAttribute(String key) {
		return mAttributes.get(key);
	}

	/**
	 * Copy of the attributes.
	 * 
	 * @return a clone of the attribute map
	 */
	public HashMap<String, String> getAttributes() {
		HashMap<String, String> attrcopy = new HashMap<String, String>();
		if (mAttributes != null)
			for (Map.Entry<String, String> entry : mAttributes.entrySet())
				attrcopy.put(entry.getKey(), entry.getValue());
		return attrcopy;
	}

	/**
	 * Get the bounding box of the object.
     * Use
	 * getBoundingSRLRectangle to get the SRL shape
	 * 
	 * @return the bounding box of the stroke
	 */
	public SrlRectangle getBoundingBox() {
		return new SrlRectangle(new SrlPoint(getMinX(), getMinY()),
				new SrlPoint(getMinX() + getWidth(), getMinY() + getHeight()));
	}

	/**
	 * Returns the angle of the diagonal of the bounding box of the shape
	 * 
	 * @return angle of the diagonal of the bounding box of the shape
	 */
	public double getBoundingBoxDiagonalAngle() {
		return Math.atan(getHeight() / getWidth());
	}

	/**
	 * Gets the center point of the stroke
	 * 
	 * @return the center of the bounding box
	 */
	public SrlPoint getCenter() {
		return new SrlPoint(getCenterX(), getCenterY());
	}

	/**
	 * Returns the center x of a shape.
	 * 
	 * @return center x of a shape
	 */
	public double getCenterX() {
		return (getMinX() + getMaxX()) / 2.0;
	}

	/**
	 * Returns the center y of a shape
	 * 
	 * @return center y of a shape
	 */
	public double getCenterY() {
		return (getMinY() + getMaxY()) / 2.0;
	}

	/**
	 * Gets the color of the object
	 * 
	 * @return
	 */
	public Color getColor() {
		return mColor;
	}

	/**
	 * Gets the convex hull.
	 * 
	 * @return the convex hull
	 */
	public Polygon getConvexHull() {

		return mConvexHull;
	}

	/**
	 * Get the description for this object
	 * 
	 * @return
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Returns the height of the object
	 * 
	 * @return the height of the object
	 */
	public double getHeight() {
		return getMaxY() - getMinY();
	}

	/**
	 * @return unique UUID for an object
	 */
	public UUID getId() {
		return mId;
	}

	/**
	 * This returns the length of the diagonal of the bounding box. This might
	 * be a better measure of perceptual size than area
	 * 
	 * @return Euclidean distance of bounding box diagonal
	 */
	public double getLengthOfDiagonal() {
		return Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth());
	}

	public abstract double getMaxX();

	public abstract double getMaxY();

	public abstract double getMinX();

	public abstract double getMinY();

	/**
	 * An object can have a name, such as "triangle1".
	 * 
	 * @return the string name of the object
	 */
	public String getName() {
		return mName;
	}

	/**
	 * This function just returns the same thing as the length of the diagonal
	 * as it is a good measure of size.
	 * 
	 * @return size of the object.
	 */
	public double getSize() {
		return getLengthOfDiagonal();
	}

	/**
	 * Gets the time associated with the object. The default time is the time it
	 * was created
	 * 
	 * @return the time the object was created.
	 */
	public long getTime() {
		return mTime;
	}

	/**
	 * Get the Type for this object
	 * 
	 * @return
	 */
	public String getType() {
		return mType;
	}

	/**
	 * Returns the width of the object
	 * 
	 * @return the width of the object
	 */
	public double getWidth() {
		return getMaxX() - getMinX();
	}

	/**
	 * Checks if this object has the given attribute.
	 * 
	 * @param key
	 *            The string name of the key of the attribute
	 * @return true if this object has the given key
	 */
	public boolean hasAttribute(String key) {
		return mAttributes.containsKey(key);
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * An object can be created by a user (like drawing a shape, or speaking a
	 * phrase) or it can be created by a system (like a recognition of a higher
	 * level shape) default is false if not explicitly set
	 * 
	 * @return true if a user created the shape
	 */
	public boolean isUserCreated() {
		return mIsUserCreated;
	}

	/**
	 * Removes the value and key of the specified attribute
	 * 
	 * @param key
	 *            the name of the attribute
	 * @return the value for the removed key, or null if key did not exist
	 */
	public String removeAttribute(String key) {
		return mAttributes.remove(key);
	}

	/**
	 * Rotates the SrlObject from the origin.
	 * 
	 * @param radians
	 *            the number of radians to rotate
	 */
	public void rotate(double radians) {

		applyTransform(AffineTransform.getRotateInstance(radians));
	}

	/**
	 * Rotates the SComponent from the given x- and y-coordinate.
	 * 
	 * @param radians
	 *            the number of radians to rotate
	 * @param xcenter
	 *            the x-coordinate to rotate from
	 * @param ycenter
	 *            the y-coordinate to rotate from
	 */
	public void rotate(double radians, double xcenter, double ycenter) {

		applyTransform(AffineTransform.getRotateInstance(radians, xcenter,
				ycenter));
	}

	/**
	 * Scales the SComponent by the given x- and y-factor.
	 * 
	 * @param xfactor
	 *            the x-factor
	 * @param yfactor
	 *            the y-factor
	 */
	public void scale(double xfactor, double yfactor) {

		applyTransform(AffineTransform.getScaleInstance(xfactor, yfactor));
	}

	/**
	 * Sets an attribute value. Will overwrite any value currently set for that
	 * attribute
	 * 
	 * @param key
	 *            attribute name (Must be string)
	 * @param value
	 *            attribute value (Must be string)
	 * @return the old value of the attribute, or null if none was set
	 */
	public String setAttribute(String key, String value) {
		return mAttributes.put(key, value);
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

	public void setBoundingBox(SrlRectangle r) {
		mBoundingBox = r;
	}

	/**
	 * Sets the color of the object
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		mColor = color;
	}

	public void setConvexHull(Polygon p) {
		mConvexHull = p;
	}

	/**
	 * Set the description for this object
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		mDescription = description;
	}

	/**
	 * In general you should not be setting a UUID unless you are loading in
	 * pre-existing objects with pre-existing UUIDs.
	 * 
	 * @param id
	 *            the unique id for the object
	 */
	public void setId(UUID id) {
		mId = UUID.fromString(id.toString());
	}

	/**
	 * An object can have a name, such as "triangle1".
	 * 
	 * @param name
	 *            object name
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * Sets the time the object was created. This probably should only be used
	 * when loading in pre-existing objects.
	 * 
	 * @param time
	 *            the time the object was created.
	 */
	public void setTime(long time) {
		mTime = time;
	}

	/**
	 * Set the Type for this object
	 * 
	 * @param type
	 */
	public void setType(String type) {
		mType = type;
	}

	/**
	 * An object can be created by a user (like drawing a shape, or speaking a
	 * phrase) or it can be created by a system (like a recognition of a higher
	 * level shape)
	 * 
	 * @param isUserCreated
	 *            true if the user created the shape, else false
	 */

	public void setUserCreated(boolean isUserCreated) {
		mIsUserCreated = isUserCreated;
	}

	/**
	 * Should be overwritten, but doesn't have to be Returns name and
	 * description of object
	 */
	public String toString() {
		return "SrlObject Name:" + getName() + " Description: "
				+ getDescription();
	}

	/**
	 * String that provides the name, description and guid
	 * @return
	 */
	public String toStringLong() {
		return "Type:" + getType() + " Name: " + getName() + " Description:"
				+ getDescription() + " UUID:" + getId();
	}


	/**
	 * Translate the object by the amount x,y
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void translate(double x, double y);

	/**
	 * Get the perimeter of the bounding box.
	 * @return the perimeter of the bounding box.
	 */
	public double getPerimeter() {
		return 2 * getWidth() + 2 * getHeight();
	}
	
	/**
	 * Get the y value for the top of the box.
	 * 
	 * @return the y value for the top of the box.
	 */
	public double getTop() {
		return getMinY();
	}

	/**
	 * Get the y value for the bottom of the box.
	 * 
	 * @return the y value for the bottom of the box.
	 */
	public double getBottom() {
		return getMaxY();
	}

	/**
	 * Get the x value for the left of the box.
	 * 
	 * @return the x value for the left of the box.
	 */
	public double getLeft() {
		return getMinX();
	}

	/**
	 * Get the x value for the right of the box.
	 * 
	 * @return the x value for the right of the box.
	 */
	public double getRight() {
		return getMaxX();
	}
	
	/**
	 * This bounding box is above the given value
	 * 
	 * @param y
	 *            the y value to compare against.
	 * @return if the bottom edge of the bounding box is above the
	 *         given value; otherwise.
	 */
	public boolean isAbove(double y) {
		return getBottom() < y;
	}

	/**
	 * This bounding box is below the given value.
	 * 
	 * @param y
	 *            the y value to compare against.
	 * @return  if the top edge of the bounding box is below the
	 *         given value; otherwise.
	 */
	public boolean isBelow(double y) {
		return getTop() > y;
	}

	/**
	 * This bounding box is to the left of the given value.
	 * 
	 * @param x
	 *            the x value to compare against.
	 * @return  if the right edge of the bounding box is to the left
	 *         of the given value;  otherwise.
	 */
	public boolean isLeftOf(double x) {
		return getRight() < x;
	}

	/**
	 * This bounding box is to the right of the given value.
	 * 
	 * @param x
	 *            the x value to compare against.
	 * @return if the left edge of the bounding box is to the right
	 *         of the given value; otherwise.
	 */
	public boolean isRightOf(double x) {
		return getLeft() > x;
	}

	
	/**
	 * Get the angle of the diagonal of the bounding box, i.e. the angle between
	 * the line along the bottom of the bounding box and the line through the
	 * bottom left and top right corners of the box.
	 * 
	 * @return angle of the diagonal of the bounding box.
	 */
	public double getDiagonalAngle() {
		return Math.atan2(getHeight(), getWidth());
	}

    /**
     * @return the counter that is used in the creation of a UUID for each new {@linkplain SrlObject} that is created.
     */
    public static long getCounter() {
        return counter;
    }
}
