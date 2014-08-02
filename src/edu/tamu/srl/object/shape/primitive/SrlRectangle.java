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
public class SrlRectangle extends SrlShape {

	private SrlPoint m_topLeftCorner = new SrlPoint(0,0);
	private SrlPoint m_bottomRightCorner = new SrlPoint(0,0);
	
	/**
	 * Constructor takes two points, and constructs a horizontal rectangle from this.
	 * @param topLeftCorner
	 * @param bottomRightCorner
	 */
	public SrlRectangle(SrlPoint topLeftCorner, SrlPoint bottomRightCorner){
		setTopLeftCorner(topLeftCorner);
		setBottomRightCorner(bottomRightCorner);
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

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.SRL_Object#clone()
	 */
	@Override
	public SrlObject clone() {
		return new SrlRectangle(m_topLeftCorner.clone(), m_bottomRightCorner.clone());
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

}
