package edu.tamu.srl.object.shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
 * Object data class
 * 
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class SrlObject implements Comparable<SrlObject>, Serializable {

	/**
	 * counter will be incremented by 0x10000 for each new SComponent that is
	 * created counter is used as the most significant bits of the UUID
	 * 
	 * initialized to 0x4000 (the version -- 4: randomly generated UUID) along
	 * with 3 bytes of randomness: Math.random()*0x1000 (0x0 - 0xFFF)
	 * 
	 * the randomness further reduces the chances of collision between multiple
	 * sketches created on multiple computers simultaneously
	 */
	public static long counter = 0x4000L | (long) (Math.random() * 0x1000);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default comparator using time
	 */
	protected static Comparator<SrlObject> timeComparator;

	/**
	 * Gets the comparator for the time values. Compares two objects based on
	 * their time stamps. Might be better to compare based on another method,
	 * but haven't decided yet what that is.
	 * 
	 * @param o
	 *            the object to compare it to return 0 if they are equal
	 * @return the comparator for the time values
	 */
	public static Comparator<SrlObject> getTimeComparator() {

		if (timeComparator == null)
			timeComparator = new Comparator<SrlObject>() {
				@Override
				public int compare(SrlObject arg0, SrlObject arg1) {
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
			public int compare(SrlObject arg0, SrlObject arg1) {
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
			public int compare(SrlObject arg0, SrlObject arg1) {
				return Double.compare(arg0.getBoundingBox().getMinY(), arg1
						.getBoundingBox().getMinY());
			}

		};
	}

	/**
	 * Generates a new UUID: based on a counter + time, version 4, variant bits
	 * set time is nanoTime()
	 * 
	 * @return the UUID
	 */
	public static UUID nextID() {

		counter += 0x10000L;
		return new UUID(counter, System.nanoTime() | 0x8000000000000000L);
	}

	/**
	 * Map of miscellaneous attributes (to store any attributes given for points
	 * in a SketchML file that are not saved in other variables here).
	 */
	private HashMap<String, String> m_attributes = new HashMap<String, String>();

	/**
	 * The bounding box of the objects
	 */
	protected transient SrlRectangle m_boundingBox;

	/**
	 * Stores the color of the object
	 */
	private Color m_color = null;

	/**
	 * The convex hull of the objects
	 */
	protected transient Polygon m_convexHull;

	/**
	 * Description of the object
	 */
	private String m_description = "";

	/**
	 * Each object has a unique ID associated with it.
	 */
	private UUID m_id = UUID.randomUUID();

	/**
	 * An object can be created by a user (like drawing a shape, or speaking a
	 * phrase) or it can be created by a system (like a recognition of a higher
	 * level shape)
	 */
	private boolean m_isUserCreated = false;

	/**
	 * The name of the object, such as "triangle1"
	 */
	private String m_name = "";

	/**
	 * The creation time of the object.
	 */
	private long m_time = (new Date()).getTime();
	// System.currentTimeMillis();

	/**
	 * The type of the object (such as "Line", "Stroke", etc.)
	 */
	private String m_type = "";

	public SrlObject() {
		setId(nextID());
		m_boundingBox = null;
		m_convexHull = null;
	}

	public SrlObject(SrlObject o) {
		this.m_attributes = o.getAttributes();
		this.m_boundingBox = o.getBoundingBox();
		this.m_color = o.getColor();
		this.m_convexHull = o.getConvexHull();
		this.m_description = o.getDescription();
		this.m_id = o.getId();
		this.m_isUserCreated = o.isUserCreated();
		this.m_name = o.getName();
		this.m_time = o.getTime();
		this.m_type = o.getType();
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
		B.m_id = A.m_id;
		B.m_isUserCreated = A.m_isUserCreated;
		B.m_name = A.m_name;
		B.m_time = A.m_time;
		B.m_color = A.m_color;
		B.m_attributes = A.getAttributes();
		B.m_boundingBox = A.getBoundingBox();
		B.m_convexHull = A.getConvexHull();
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
	 * @param cloned
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
	 * @param rp
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
		m_boundingBox = null;
		m_convexHull = null;
	}

	/**
	 * Returns the length times the height See also getLengthOfDiagonal() return
	 * area of shape
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
		return m_attributes.get(key);
	}

	/**
	 * Copy of the attributes.
	 * 
	 * @return a clone of the attribute map
	 */
	public HashMap<String, String> getAttributes() {
		HashMap<String, String> attrcopy = new HashMap<String, String>();
		if (m_attributes != null)
			for (Map.Entry<String, String> entry : m_attributes.entrySet())
				attrcopy.put(entry.getKey(), entry.getValue());
		return attrcopy;
	}

	/**
	 * Get the bounding box of the stroke This returns an awt shape. Use
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
		return m_color;
	}

	/**
	 * Gets the convex hull.
	 * 
	 * @return the convex hull
	 */
	public Polygon getConvexHull() {

		return m_convexHull;
	}

	/**
	 * Get the description for this object
	 * 
	 * @return
	 */
	public String getDescription() {
		return m_description;
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
		return m_id;
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
		return m_name;
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
		return m_time;
	}

	/**
	 * Get the Type for this object
	 * 
	 * @return
	 */
	public String getType() {
		return m_type;
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
		return m_attributes.containsKey(key);
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
		return m_isUserCreated;
	}

	/**
	 * Paints this component.
	 * 
	 * @param g
	 *            the graphics component
	 */
	public void paint(Graphics g) {
		paint((Graphics2D) g);
	}

	/**
	 * Paints this component.
	 * 
	 * @param g
	 *            the 2D graphics component
	 */
	public abstract void paint(Graphics2D g);

	/**
	 * Removes the value and key of the specified attribute
	 * 
	 * @param key
	 *            the name of the attribute
	 * @return the value for the removed key, or null if key did not exist
	 */
	public String removeAttribute(String key) {
		return m_attributes.remove(key);
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
		return m_attributes.put(key, value);
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
		m_boundingBox = r;
	}

	/**
	 * Sets the color of the object
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		m_color = color;
	}

	public void setConvexHull(Polygon p) {
		m_convexHull = p;
	}

	/**
	 * Set the description for this object
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		m_description = description;
	}

	/**
	 * In general you should not be setting a UUID unless you are loading in
	 * pre-existing objects with pre-existing UUIDs.
	 * 
	 * @param id
	 *            the unique id for the object
	 */
	public void setId(UUID id) {
		m_id = UUID.fromString(id.toString());
	}

	/**
	 * An object can have a name, such as "triangle1".
	 * 
	 * @param name
	 *            object name
	 */
	public void setName(String name) {
		m_name = name;
	}

	/**
	 * Sets the time the object was created. This probably should only be used
	 * when loading in pre-existing objects.
	 * 
	 * @param time
	 *            the time the object was created.
	 */
	public void setTime(long time) {
		m_time = time;
	}

	/**
	 * Set the Type for this object
	 * 
	 * @param type
	 */
	public void setType(String type) {
		m_type = type;
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
		m_isUserCreated = isUserCreated;
	}

	/**
	 * Should be overwritten, but doesn't have to be Returns name and
	 * description of object
	 */
	public String toString() {
		return "SrlObject Name:" + getName() + " Description: "
				+ getDescription();
	}

	public String toStringLong() {
		return "Type:" + getType() + " Name: " + " Description:"
				+ getDescription() + " UUID:" + getId();
	}

	/**
	 * Translate the object by the amount x,y
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void translate(double x, double y);

}
