/**
 * 
 */
package edu.tamu.srl.object.shape;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.Set;

import edu.tamu.srl.settings.SrlInitialSettings;


/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlRectangle extends SrlShape {

	private SrlPoint mTopLeftCorner = new SrlPoint(0,0);
	private SrlPoint mBottomRightCorner = new SrlPoint(0,0);

	/**
	 * Constructor takes two points, and constructs a horizontal rectangle from this.
	 * @param topLeftCorner
	 * @param bottomRightCorner
	 */
	public SrlRectangle(SrlPoint topLeftCorner, SrlPoint bottomRightCorner){
		setColor(SrlInitialSettings.IntialRectangleColor);
		setTopLeftCorner(topLeftCorner);
		setBottomRightCorner(bottomRightCorner);
	}

	/**
	 * Constructor 
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 */
	public SrlRectangle(double minX, double minY, double maxX, double maxY){
		this(new SrlPoint(minX, minY), new SrlPoint(maxX, maxY));
	}
	

	/**
	 * Gets the bottom right corner
	 * @return
	 */
	public SrlPoint getBottomRightCorner() {
		return mBottomRightCorner;
	}

	/**
	 * Sets the bottomright corner
	 * @param bottomRightCorner
	 */
	public void setBottomRightCorner(SrlPoint bottomRightCorner) {
		mBottomRightCorner = bottomRightCorner;
	}

	/* 
	 * Returns the minimum x. This works even if the topleft and bottomright are switched
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
	 */
	@Override
	public double getMinX() {
		return Math.min(mTopLeftCorner.getX(), mBottomRightCorner.getX());
	}

	/* 
	 * Returns the minimum y. This works even if the topleft and bottomright are switched
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinY()
	 */
	@Override
	public double getMinY() {
		return Math.min(mTopLeftCorner.getY(), mBottomRightCorner.getY());
	}

	/* 
	 * Returns the maximum x. This works even if the topleft and bottomright are switched
	 * (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxX()
	 */
	@Override
	public double getMaxX() {
		return Math.max(mTopLeftCorner.getX(), mBottomRightCorner.getX());
	}

	/* (non-Javadoc)
     * Returns the maximum y. This works even if the topleft and bottomright are switched
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxY()
	 */
	@Override
	public double getMaxY() {
		return Math.max(mTopLeftCorner.getY(), mBottomRightCorner.getY());
	}

	/**
	 * Gets the top left corner point
	 * @return
	 */
	public SrlPoint getTopLeftCorner() {
		return mTopLeftCorner;
	}

	/**
	 * Sets the top left corner
	 * @param topLeftCorner
	 */
	public void setTopLeftCorner(SrlPoint topLeftCorner) {
		mTopLeftCorner = topLeftCorner;
	}

	/**
	 * Translates the saved corner points and the subshapes
	 * @param x x amount to translate
	 * @param y y amount to translate
	 */
	public void translate(double x, double y){
		mTopLeftCorner.translate(x, y);
		mBottomRightCorner.translate(x, y);
		translateSubShapes(x,y);
	}

	@Override
	public boolean equalsByContent(SrlObject other) {
		if (!(other instanceof SrlRectangle)){return false;}
		SrlRectangle otherrectangle = (SrlRectangle)other;
		if(!getTopLeftCorner().equals(otherrectangle.getTopLeftCorner())){return false;}
		if(!getBottomRightCorner().equals(otherrectangle.getBottomRightCorner())){return false;}
		return true;
	}

	@Override
	protected void applyTransform(AffineTransform xform, Set<SrlObject> xformed) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method just returns itself
	 * Overwrites this method to prevent an infinite loop
	 */
	public SrlRectangle getBoundingBox(){
		return this;
	}
	
	


	/**
	 * Get the line segment along the top edge of this box.
	 * 
	 * @return the segment along the top edge of this box.
	 */
	public SrlLine getTopSegment() {
		return new SrlLine(getMinX(), getMinY(), getMaxX(), getMinY());
	}




	/**
	 * Get the line segment along the bottom edge of this box.
	 * 
	 * @return The segment along the bottom edge of this box.
	 */
	public SrlLine getBottomSegment() {
		return new SrlLine(getMinX(), getMaxY(), getMaxX(), getMaxY());
	}

	/**
	 * Get the line segment along the left edge of this box.
	 * 
	 * @return The segment along the left edge of this box.
	 */
	public SrlLine getLeftSegment() {
		return new SrlLine(getMinX(), getMinY(), getMinX(), getMaxY());
	}

	/**
	 * Get the line segment along the right edge of this box.
	 * 
	 * @return The segment along the right edge of this box.
	 */
	public SrlLine getRightSegment() {
		return new SrlLine(getMaxX(), getMinY(), getMaxX(), getMaxY());
	}
	
	/*
	 * This method doesn't do anything because it would cause an infinite loop
	 * (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SrlShape#calculateBBox()
	 */
	@Override
	protected void calculateBBox() {
	}

	private Color m_fillColor = SrlInitialSettings.InitialRectangleFillColor;
	private boolean m_fill = SrlInitialSettings.InitialRectangleFill;
	
}
