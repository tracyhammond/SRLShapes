/**
 * 
 */
package edu.tamu.srl.object.shape.primitive;

import edu.tamu.srl.object.SrlObject;
import edu.tamu.srl.object.shape.SrlShape;

/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlCircle extends SrlShape {

	private SrlPoint m_center;
	private double m_radius;

	
	/**
	 * Get the center point of the circls
	 * @return the center
	 */
	public SrlPoint getCenter() {
		return m_center;
	}

	/**
	 * set the center point of the circle
	 * @param center the center to set
	 */
	public void setCenter(SrlPoint center) {
		m_center = center;
	}

	/**
	 * Get the radius of the circle
	 * @return the radius
	 */
	public double getRadius() {
		return m_radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		m_radius = radius;
	}

	
	/**
	 * @param center
	 * @param radius
	 */
	public SrlCircle(SrlPoint center, double radius) {
		m_center = center;
		m_radius = radius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
	 */
	@Override
	public double getMinX() {
		return m_center.getX() - m_radius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinY()
	 */
	@Override
	public double getMinY() {
		return m_center.getY() - m_radius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxX()
	 */
	@Override
	public double getMaxX() {
		return m_center.getX() + m_radius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxY()
	 */
	@Override
	public double getMaxY() {
		return m_center.getY() + m_radius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.SRL_Object#clone()
	 */
	@Override
	public SrlObject clone() {
		SrlCircle cloned = new SrlCircle(m_center, m_radius);
		super.clone(cloned);
	    return cloned;
	}
		
	/** 
	 * Translates the center and the subshapes
	 * @param x x amount to translate
	 * @param y y amount to translate
	 */
	public void translate(double x, double y){
		m_center.translate(x, y);
		translateSubShapes(x,y);
	}
	
}
