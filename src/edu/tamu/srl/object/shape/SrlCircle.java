/**
 * 
 */
package edu.tamu.srl.object.shape;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.Set;

/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlCircle extends SrlInterpretedShape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SrlPoint m_center;
	private double m_radius;

	
	/**
	 * Get the center point of the circle
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
		
	/** 
	 * Translates the center and the subshapes
	 * @param x x amount to translate
	 * @param y y amount to translate
	 */
	public void translate(double x, double y){
		m_center.translate(x, y);
		translateSubShapes(x,y);
	}

	@Override
	public boolean equalsByContent(SrlObject other) {
		if (!(other instanceof SrlCircle)){return false;}
		SrlCircle othercircle = (SrlCircle) other;
		if(!getCenter().equals(othercircle.getCenter())){return false;}
		if(getRadius() != othercircle.getRadius()){return false;}
		return true;
	}

	@Override
	protected void applyTransform(AffineTransform xform, Set<SrlObject> xformed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
