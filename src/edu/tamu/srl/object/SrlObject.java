package edu.tamu.srl.object;

import java.awt.Color;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.tamu.srl.object.shape.primitive.SrlPoint;
import edu.tamu.srl.object.shape.primitive.SrlRectangle;

/**
 * Object data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class SrlObject implements Cloneable, Comparable<SrlObject>, Serializable{
	
	/**
	 * Default comparator using time
	 */
	protected static Comparator<SrlObject> timeComparator;
	
	/**
	 * Gets the comparator for the time values.
	 * Compares two objects based on their time stamps.
	 * Might be better to compare based on another method, 
	 * but haven't decided yet what that is.
	 * @param o the object to compare it to
	 * return 0 if they are equal
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
	 * Map of miscellaneous attributes (to store any attributes given for points
	 * in a SketchML file that are not saved in other variables here).
	 */
	private Map<String, String> m_attributes = null;
	
	/**
	 * Stores the color of the object
	 */
	private Color m_color = null;

	/**
	 * Description of the object
	 */
	private String m_description = "";

	/**
	 * Each object has a unique ID associated with it.
	 */
	private	UUID m_id = UUID.randomUUID();

	/**
	 * An object can be created by a user 
	 * (like drawing a shape, or speaking a phrase)
	 * or it can be created by a system
	 * (like a recognition of a higher level shape)
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

	/**
	 * The type of the object (such as "Line", "Stroke", etc.)
	 */
	private String m_type = "";
	
	/**
	 * A list of possible interpretations for an object
	 */
	public abstract SrlObject clone();
	
	/**
	 * Default uses time. You can also use x or y to compare
	 */
	public int compareTo(SrlObject o){
		return getTimeComparator().compare(this, o);
	}
	
	/**
	 * Copies what A is into what B is
	 * @param A
	 * @param B
	 */
	protected void copyAIntoB(SrlObject A, SrlObject B){
		B.m_id = A.m_id;
		B.m_isUserCreated = A.m_isUserCreated;
		B.m_name = A.m_name;
		B.m_time = A.m_time;
		B.m_color = A.m_color;
		B.m_attributes = A.getAttributes();
	}
	
	/**
	 * Copy the information from the argument into this object
	 * @param unchangedObject
	 */
	protected void copyFrom(SrlObject unchangedObject){
		copyAIntoB(unchangedObject, this);
	}

	/**
	 * Clones all of the information to the object sent in
	 * @param cloned the new clone object
	 * @return the same cloned object (superfluous return)
	 */
	protected void copyInto(SrlObject editableObject){
		copyAIntoB(this, editableObject);	
	}
	
	/**
	 * Returns the length times the height
	 * See also getLengthOfDiagonal()
	 * return area of shape
	 */
	public double getArea(){
		return getHeight() * getWidth();
	}

	/**
	 * Gets the value for the given attribute.
	 * 
	 * @param key String name of key
	 * @return the value of the given attribute, or null if that attribute is
	 *         not set.
	 */
	public String getAttribute(String key) {
		return m_attributes.get(key);
	}

	/**
	 * Copy of the attributes. 
	 * @return a clone of the attribute map
	 */
	public Map<String, String> getAttributes() {
		Map<String, String> attrcopy = new HashMap<String, String>();
		if (m_attributes != null)
			for (Map.Entry<String, String> entry : m_attributes.entrySet())
				attrcopy.put(entry.getKey(), entry.getValue());
		return attrcopy;
	}

	/**
	 * Get the bounding box of the stroke
	 * This returns an awt shape. 
	 * Use getBoundingSRLRectangle to get the SRL shape
	 * @return the bounding box of the stroke
	 */
	public SrlRectangle getBoundingBox() {
		return new SrlRectangle(new SrlPoint(getMinX(), getMinY()), 
				new SrlPoint(getMinX() + getWidth(), getMinY() + getHeight()));
	}

	/**
	 * Returns the angle of the diagonal of the bounding box of the shape
	 * @return angle of the diagonal of the bounding box of the shape
	 */
	public double getBoundingBoxDiagonalAngle() {
		return Math.atan(getHeight()/getWidth());
	}

	/**
	 * Returns the center x of a shape.
	 * @return center x of a shape
	 */
	public double getCenterX(){
		return (getMinX() + getMaxX())/2.0;
	}

	/**
	 * Returns the center y of a shape
	 * @return center y of a shape
	 */
	public double getCenterY(){
		return (getMinY() + getMaxY())/2.0;
	}
	
	/**
	 * Gets the color of the object
	 * @return
	 */
	public Color getColor() {
		return m_color;
	}
	
	/**
	 * Get the description for this object
	 * @return
	 */
	public String getDescription() {
		return m_description;
	}
	


	/**
	 * Returns the height of the object
	 * @return the height of the object
	 */
	public double getHeight(){
		return getMaxY() - getMinY();
	}
	
	/**
	 * @return unique UUID for an object
	 */
	public UUID getId() {
		return m_id;
	}
	
	/**
	 * This returns the length of the diagonal of the bounding box. 
	 * This might be a better measure of perceptual size than area
	 * @return Euclidean distance of bounding box diagonal
	 */
	public double getLengthOfDiagonal(){
		return Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth());
	}
	
	public abstract double getMaxX();
	public abstract double getMaxY();
	public abstract double getMinX();
	public abstract double getMinY();
	
	/**
	 * An object can have a name, such as "triangle1". 
	 * @return the string name of the object
	 */
	public String getName() {
		return m_name;
	}
	
	/**
	 * This function just returns the same thing as the length of the diagonal
	 * as it is a good measure of size.
	 * @return size of the object.
	 */
	public double getSize(){
		return getLengthOfDiagonal();
	}
	
	/**
	 * Gets the time associated with the object. 
	 * The default time is the time it was created
	 * @return the time the object was created.
	 */
	public long getTime() {
		return m_time;
	}
	
	/**
	 * Get the Type for this object
	 * @return
	 */
	public String getType() {
		return m_type;
	}
	
	/**
	 * Returns the width of the object
	 * @return the width of the object
	 */
	public double getWidth(){
		return getMaxX() - getMinX();
	}
	
	/**
	 * Checks if this object has the given attribute.
	 * 
	 * @param key The string name of the key of the attribute
	 * @return true if this object has the given key
	 */
	public boolean hasAttribute(String key) {
		return m_attributes.containsKey(key);
	}

	/**
	 * An object can be created by a user 
	 * (like drawing a shape, or speaking a phrase)
	 * or it can be created by a system
	 * (like a recognition of a higher level shape)
	 * default is false if not explicitly set
	 * @return true if a user created the shape
	 */
	public boolean isUserCreated() {
		return m_isUserCreated;
	}
	
	/**
	 * Removes the value and key of the specified attribute
	 * 
	 * @param key  the name of the attribute
	 * @return the value for the removed key, or null if key did not exist
	 */
	public String removeAttribute(String key) {
		return m_attributes.remove(key);
	}
	
	/**
	 * Sets an attribute value. Will overwrite any value currently set for that
	 * attribute 
	 * 
	 * @param key   attribute name (Must be string)
	 * @param value attribute value (Must be string)
	 * @return the old value of the attribute, or null if none was set
	 */
	public String setAttribute(String key, String value) {
		return m_attributes.put(key, value);
	}
	
	/**
	 * Sets the color of the object
	 * @param color
	 */
	public void setColor(Color color) {
		m_color = color;
	}
	
	/**
	 * Set the description for this object
	 * @param description
	 */
	public void setDescription(String description) {
		m_description = description;
	}
	
	/**
	 * In general you should not be setting a UUID unless you are
	 * loading in pre-existing objects with pre-existing UUIDs.
	 * @param id the unique id for the object
	 */
	public void setId(UUID id) {
		m_id = UUID.fromString(id.toString());
	}

	/**
	 * An object can have a name, such as "triangle1". 
	 * @param name object name
	 */
	public void setName(String name) {
		m_name = name;
	}

	/**
	 * Sets the time the object was created. This probably should 
	 * only be used when loading in pre-existing objects.
	 * @param time the time the object was created.
	 */
	public void setTime(long time) {
		m_time = time;
	}

	/**
	 * Set the Type for this object
	 * @param type
	 */
	public void setType(String type) {
		m_type = type;
	}

	/**
	 * An object can be created by a user 
	 * (like drawing a shape, or speaking a phrase)
	 * or it can be created by a system
	 * (like a recognition of a higher level shape)
	 * @param isUserCreated true if the user created the shape, else false
	 */

	public void setUserCreated(boolean isUserCreated) {
		m_isUserCreated = isUserCreated;
	}

	/**
	 * Translate the object by the amount x,y
	 * @param x
	 * @param y
	 */
	public abstract void translate(double x, double y);

	/**
	 * Should be overwritten, but doesn't have to be
	 * Returns name and description of object
	 */
	public String toString(){
		return "SrlObject Name:" + getName() + " Description: " + getDescription();
	}
	
}
