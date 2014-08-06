/**
 * 
 */
package edu.tamu.srl.object.shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Set;

import edu.tamu.srl.settings.SrlInitialSettings;


/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlRectangle extends SrlShape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private SrlPoint m_topLeftCorner = new SrlPoint(0,0);
	private SrlPoint m_bottomRightCorner = new SrlPoint(0,0);
	
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
		return m_bottomRightCorner;
	}

	/**
	 * Sets the bottomright corner
	 * @param bottomRightCorner
	 */
	public void setBottomRightCorner(SrlPoint bottomRightCorner) {
		m_bottomRightCorner = bottomRightCorner;
	}

	/* 
	 * Returns the minimum x. This works even if the topleft and bottomright are switched
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
	 */
	@Override
	public double getMinX() {
		return Math.min(m_topLeftCorner.getX(), m_bottomRightCorner.getX());
	}

	/* 
	 * Returns the minimum y. This works even if the topleft and bottomright are switched
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinY()
	 */
	@Override
	public double getMinY() {
		return Math.min(m_topLeftCorner.getY(), m_bottomRightCorner.getY());
	}

	/* 
	 * Returns the maximum x. This works even if the topleft and bottomright are switched
	 * (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxX()
	 */
	@Override
	public double getMaxX() {
		return Math.max(m_topLeftCorner.getX(), m_bottomRightCorner.getX());
	}

	/* (non-Javadoc)
     * Returns the maximum y. This works even if the topleft and bottomright are switched
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxY()
	 */
	@Override
	public double getMaxY() {
		return Math.max(m_topLeftCorner.getY(), m_bottomRightCorner.getY());
	}

	/**
	 * Gets the top left corner point
	 * @return
	 */
	public SrlPoint getTopLeftCorner() {
		return m_topLeftCorner;
	}

	/**
	 * Sets the top left corner
	 * @param topLeftCorner
	 */
	public void setTopLeftCorner(SrlPoint topLeftCorner) {
		m_topLeftCorner = topLeftCorner;
	}

	/**
	 * Translates the saved corner points and the subshapes
	 * @param x x amount to translate
	 * @param y y amount to translate
	 */
	public void translate(double x, double y){
		m_topLeftCorner.translate(x, y);
		m_bottomRightCorner.translate(x, y);
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

	@Override
	public void paint(Graphics2D g) {
		if (getColor() != null)
			g.setColor(getColor());
		g.setStroke(new BasicStroke(SrlInitialSettings.InitialRectangleBorderThickness));
		g.drawRect((int)getMinX(), (int)getMinY(), (int)getWidth(), (int)getHeight());
		if(m_fill){
			if (getColor() != null)
				g.setColor(m_fillColor);
			g.fillRect((int)getMinX(), (int)getMinY(), (int)getWidth(), (int)getHeight());
		}
	}

	private Color m_fillColor = SrlInitialSettings.InitialRectangleFillColor;
	private boolean m_fill = SrlInitialSettings.InitialRectangleFill;
	
}
