package edu.tamu.srl.object.shape.primitive;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import edu.tamu.srl.object.SrlObject;

/**
 * Point data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlPoint extends SrlObject implements ISrlPoint, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Because it is serializable, that means we can save to a file, but if we change this class
	 * it might break the ability to read the file back in.
	 */

	/**
	   * Return the distance from the point specified by (x,y) to this point
	   * @param x the x value of the other point
	   * @param y the y value of the other point
	   * @return the distance
	   */
	public static double distance(double x1, double y1 , double x2, double y2) {
		double xdiff = x1 -x2;
		double ydiff = y1 - y2;
	    return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	  }

	/**
	 * A counter that keeps track of where you are in the history of points
	 */
	private transient int m_currentElement = -1;

	/**
	 * Points can have pressure depending on the input device
	 */
	private Double m_pressure = null;

	
	/**
	 * Tilt in the X direction when the point was created.
	 */
	private Double m_tiltX = null;

	/**
	 * Tilt in the Y direction when the point was created.
	 */
	private Double m_tiltY = null;
	
	/**
	 * Holds an history list of the x points 
	 * Purpose is so that we can redo and undo and go back to the original points
	 */
	private ArrayList<Double> m_xList = new ArrayList<Double>();
	
	/**
	 * Holds a history list of the y points 
	 * Purpose is so that we can redo and undo and go back to the original points
	 * Note that this means that we cannot just set one value, and not the other.
	 */
	private ArrayList<Double> m_yList = new ArrayList<Double>();
	
	/**
	 * Creates a point with the initial points at x,y
	 * @param x the initial x point
	 * @param y the initial y point
	 */
	public SrlPoint(double x, double y) {
		setP(x,y);
	}

	/**
	 * Creates a point with the initial points at x,y
	 * @param x the initial x point
	 * @param y the initial y point
	 * @param time the time the point was made
	 */
	public SrlPoint(double x, double y, long time) {
		setP(x,y);
		setTime(time);    
	}

	/**
	 * Create a new point from a mouse event
	 * @param e
	 */
	public SrlPoint(MouseEvent e) {
		this(e.getX(), e.getY(), e.getWhen());
	}
	
	/**
	 * New constructor that takes a UUID.
	 * 
	 * @param x
	 *            x value of the point.
	 * @param y
	 *            y value of the point.
	 * @param time
	 *            time stamp.
	 * @param id
	 *            point ID (.equals)
	 */
	public SrlPoint(double x, double y, long time, UUID id) {
		this(x, y, time);
		setId(id);
	}
	
	public SrlPoint(double x, double y, long time, UUID id, double tiltX, double tiltY, double pressure){
		this(x,y,time,id);
		setTilt(tiltX, tiltY);
		setPressure(pressure);
	}

	/**
	 * Construct a new point with the same elements
	 * @param p
	 */
	public SrlPoint(SrlPoint p){
		this(p.getX(), p.getY(), p.getTime(), p.getId(), p.getTiltX(), p.getTiltY(), p.getPressure());
		setName(p.getName());
	}
	
	
	
	/**
	 * Clone the point
	 * return an exact copy of this point
	 */
	@Override
	public SrlPoint clone() {
		ArrayList<Double> xlist = new ArrayList<Double>();
	    ArrayList<Double> ylist = new ArrayList<Double>();
	    for(int i = 0; i < m_xList.size(); i++){
	      xlist.add((double)m_xList.get(i));
	      ylist.add((double)m_yList.get(i));
	    }
	    SrlPoint p = new SrlPoint(getX(), getY());
	    super.copyInto(p);
	    p.setTime(getTime());
	    p.setPressure(getPressure());
	    p.setTilt(getTiltX(), getTiltY());
	    p.setName(getName());
	    p.m_xList = xlist;
	    p.m_yList = ylist;
	    p.m_currentElement = m_currentElement;
	    return p;
	  }

	/**
	   * Return the distance from the point specified by (x,y) to this point
	   * @param x the x value of the other point
	   * @param y the y value of the other point
	   * @return the distance
	   */
	  public double distance(double x, double y) {
	    double xdiff = x - getX();
	    double ydiff = y - getY();
	    return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	  }

	/**
	   * Return the distance from point rp to this point.
	   * @param rp the other point
	   * @return the distance
	   */
	  public double distance(SrlPoint rp) {
	    return distance(rp.getX(), rp.getY());
	  }

	/**
	 * Return an object drawable by AWT
	 * return awt point
	 */
	public Point getAWT(){
		return new Point((int)getX(),(int)getY());
	}


	/**
	 * Get the x value for the first point in the history
	 * @return
	 */
	public double getInitialX(){
		if(m_xList.size() == 0){
			return Double.NaN;
		}
		return m_xList.get(0);
	}
	
	/**
	 * Get the y value for the first point in the history
	 * @return
	 */
	public double getInitialY(){
		if(m_yList.size() == 0){
			return Double.NaN;
		}
		return m_yList.get(0);
	}

	@Override
	/**
	 * Just returns the x value 
	 * return x value
	 */
	public double getMaxX() {
		return getX();
	}
	
	 @Override
	/**
	 * Just returns the y value
	 * return y value
	 */
	public double getMaxY() {
		return getY();
	}
	  
	  @Override
	/**
	 * Just returns the x value 
	 * return x value
	 */
	public double getMinX() {
		return getX();
	}

	  @Override
	/**
	 * Just returns the y value 
	 * return y value
	 */
	public double getMinY() {
		return getY();
	}
  
	  
	/**
	 * Points can have pressure depending on the input device
	 * @return the pressure of the point
	 */
	public Double getPressure() {
		return m_pressure;
	}
	
	/**
	 * Get the tilt in the X direction.
	 * 
	 * @return tilt in the X direction.
	 */
	public Double getTiltX() {
		return m_tiltX;
	}
	
	/**
	 * Get the tilt in the Y direction.
	 * 
	 * @return tilt in the Y direction.
	 */
	public Double getTiltY() {
		return m_tiltY;
	}
	
	/**
	 * Get the current x value of the point
	 * @return current x value of the point
	 */
	public double getX(){
		return m_xList.get(m_currentElement);
	}
	
	/**
	 * Get the current y value of the point
	 * @return current y value of the point
	 */
	public double getY(){
		return m_yList.get(m_currentElement);
	}
	
	/**
	 * Get the original value of the point
	 * @return a point where getx and gety return the first values that were added to the history
	 */
	public SrlPoint goBackToInitial(){
		if(m_currentElement >= 0){
			m_currentElement = 0;
		}
		return this;
	}

	public void main(){
		SrlPoint p = new SrlPoint(3,4,1);
		System.out.println(p.toString());
	}
	  
	/** 
	 * Scales the point by the amount x and y. 
	 * Having x and y specified allows one to change the 
	 * relative dimensions to match another shape if wanted.
	 * Usually, these values will be the same to keep the 
	 * relative dimensions equal
	 * @param x the amount to scale in the x direction
	 * @param y the amount to scale in the y direction
	 */
	public void scale(double x, double y) {
	    m_xList.add(x * getX());
	    m_yList.add(y * getY());
	    m_currentElement = m_xList.size() - 1;
	}
	
	/**
	 * Delete the entire point history and 
	 * use these values as the starting point
	 * @param x new initial x location
	 * @param y new initial y location 
	 */
	public void setOrigP(double x, double y) {
	  m_xList = new ArrayList<Double>();
	  m_yList = new ArrayList<Double>();
	  setP(x, y);
	}

	/**
	 * Updates the location of the point
	 * Also add this point to the history of the points 
	 * so this can be undone.
	 * @param x the new x location for the point
	 * @param y the new y location for the point
	 */
	  public void setP(double x, double y) {
	    m_xList.add(x);
	    m_yList.add(y);
	    m_currentElement = m_xList.size() - 1;
	  }

	/**
	 * Set the pressure of the point.
	 * 
	 * @param pressure
	 *            pressure of the point.
	 */
	public void setPressure(double pressure) {
		m_pressure = pressure;
	}

	/**
	 * Set the tilt of the point.
	 * 
	 * @param tiltX
	 *            tilt in the X direction.
	 * @param tiltY
	 *            tilt in the Y direction.
	 */
	public void setTilt(double tiltX, double tiltY) {
		setTiltX(tiltX);
		setTiltY(tiltY);
	}

	/**
	 * Set the tilt in the X direction.
	 * 
	 * @param tiltX
	 *            tilt in the X direction.
	 */
	public void setTiltX(double tiltX) {
		m_tiltX = tiltX;
	}
	
	/**
	 * Set the tilt in the Y direction.
	 * 
	 * @param tiltY
	 *            tilt in the Y direction.
	 */
	public void setTiltY(double tiltY) {
		m_tiltY = tiltY;
	}
	
	/**
	 * Returns a string of [x,y,time]
	 * @return a string of [x,t,time]
	 */
	public String toString(){
		return "<"+ getX() + "," + getY() + "," + getTime() + ">";
	}

	/**
	 * Translate the point in the amount x,y
	 * Saves the point it was before in case we need to undo the translation
	 * @param x amount to move in the x direction
	 * @param y amount to move in the y direction
	 */
	public void translate(double x, double y) {
	    m_xList.add(x + getX());
	    m_yList.add(y + getY());
	    m_currentElement = m_xList.size() - 1;
	}
	
	/**
	 * Remove last point update
	 * If there is only one x,y value in the history,
	 * then it does nothing
	 * Returns the updated shape (this)
	 */
	public SrlPoint undoLastChange() {
	  if (m_xList.size() < 2) { return this; }
	  if (m_yList.size() < 2) { return this; }
	  m_xList.remove(m_xList.size() - 1);
	  m_yList.remove(m_yList.size() - 1);
	  m_currentElement -= 1;
	  return this;
	}

	@Override
	public boolean equalsXYTime(SrlPoint p) {
		return (p.getX() == getX() && p.getY() == getY() && p.getTime() == getTime());
	}
}
