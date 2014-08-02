package edu.tamu.srl.object;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import edu.tamu.srl.object.shape.primitive.SrlPoint;
import edu.tamu.srl.object.shape.primitive.SrlRectangle;

/**
 * Object data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class SrlObject implements Cloneable{
//Comparable<SrlObject>,
	/**
	 * Map of miscellaneous attributes (to store any attributes given for points
	 * in a SketchML file that are not saved in other variables here).
	 */
	private Map<String, String> m_attributes = null;

	
	/**
	 * Each object has a unique ID associated with it.
	 */
	private	UUID m_id = UUID.randomUUID();
	
	/**
	 * The name of the object, such as "triangle1"
	 */
	private String m_name = "";
	
	/**
	 * The creation time of the object.
	 */
	private long m_time = (new Date()).getTime();
	
	/**
	 * An object can be created by a user 
	 * (like drawing a shape, or speaking a phrase)
	 * or it can be created by a system
	 * (like a recognition of a higher level shape)
	 */
	private boolean m_isUserCreated = false;
	
	/**
	 * A list of possible interpretations for an object
	 */
	public abstract SrlObject clone();
	
	/**
	 * Clones all of the information to the object sent in
	 * @param cloned the new clone object
	 * @return the same cloned object (superfluous return)
	 */
	protected void makeInputAClone(SrlObject makeThisAClone){
		makeThisAClone.m_id = m_id;
		makeThisAClone.m_isUserCreated = m_isUserCreated;
		makeThisAClone.m_name = m_name;
		makeThisAClone.m_time = m_time;
		
	}

	/**
	 * @return unique UUID for an object
	 */
	public UUID getId() {
		return m_id;
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
	 * @return the string name of the object
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * An object can have a name, such as "triangle1". 
	 * @param name object name
	 */
	public void setName(String name) {
		m_name = name;
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
	 * Sets the time the object was created. This probably should 
	 * only be used when loading in pre-existing objects.
	 * @param time the time the object was created.
	 */
	public void setTime(long time) {
		m_time = time;
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
	 * Compares two objects based on their time stamps.
	 * Might be better to compare based on another method, 
	 * but haven't decided yet what that is.
	 * @param o the object to compare it to
	 * return 0 if they are equal
	 */
	public int compareTo(SrlObject o){
		return ((Long)getTime()).compareTo((Long)o.getTime());
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
	 * Get the bounding box of the stroke
	 * This returns an awt shape. 
	 * Use getBoundingSRLRectangle to get the SRL shape
	 * @return the bounding box of the stroke
	 */
	public SrlRectangle getBoundingBox() {
		return new SrlRectangle(new SrlPoint(getMinX(), getMinY()), 
				new SrlPoint(getMinX() + getWidth(), getMinY() + getHeight()));
	}
	
	public abstract double getMinX();
	public abstract double getMinY();
	public abstract double getMaxX();
	public abstract double getMaxY();
	
	/**
	 * Returns the width of the object
	 * @return the width of the object
	 */
	public double getWidth(){
		return getMaxX() - getMinX();
	}
	
	/**
	 * Returns the height of the object
	 * @return the height of the object
	 */
	public double getHeight(){
		return getMaxY() - getMinY();
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
	 * This returns the length of the diagonal of the bounding box. 
	 * This might be a better measure of perceptual size than area
	 * @return Euclidean distance of bounding box diagonal
	 */
	public double getLengthOfDiagonal(){
		return Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth());
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
	 * Returns the angle of the diagonal of the bounding box of the shape
	 * @return angle of the diagonal of the bounding box of the shape
	 */
	public double getBoundingBoxDiagonalAngle() {
		return Math.atan(getHeight()/getWidth());
	}
}
