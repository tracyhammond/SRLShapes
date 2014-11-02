/**
 * 
 */
package edu.tamu.srl.object.shape;

import java.awt.geom.AffineTransform;
import java.util.Set;

/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlCircle extends SrlInterpretedShape {

	private SrlPoint mCenter;
	private double mRadius;

	
	/**
	 * Get the center point of the circle
	 * @return the center
	 */
	public SrlPoint getCenter() {
		return mCenter;
	}

	/**
	 * set the center point of the circle
	 * @param center the center to set
	 */
	public void setCenter(SrlPoint center) {
		mCenter = center;
	}

	/**
	 * Get the radius of the circle
	 * @return the radius
	 */
	public double getRadius() {
		return mRadius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		mRadius = radius;
	}

	
	/**
	 * @param center
	 * @param radius
	 */
	public SrlCircle(SrlPoint center, double radius) {
		mCenter = center;
		mRadius = radius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
	 */
	@Override
	public double getMinX() {
		return mCenter.getX() - mRadius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinY()
	 */
	@Override
	public double getMinY() {
		return mCenter.getY() - mRadius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxX()
	 */
	@Override
	public double getMaxX() {
		return mCenter.getX() + mRadius;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxY()
	 */
	@Override
	public double getMaxY() {
		return mCenter.getY() + mRadius;
	}
		
	/** 
	 * Translates the center and the subshapes
	 * @param x x amount to translate
	 * @param y y amount to translate
	 */
	public void translate(double x, double y){
		mCenter.translate(x, y);
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

}
