/**
 * 
 */
package edu.tamu.srl.object.shape;

import java.util.ArrayList;

/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class DomainShape extends SrlShape {

	public DomainShape(ArrayList<SrlShape> shapes){
		for(SrlShape o : shapes){
			addSubShape(o);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
	 */
	@Override
	public double getMinX() {
		double min = Double.POSITIVE_INFINITY;
		for(SrlShape shape : getSubShapes("shape")){
			min = Math.min(min, shape.getMinX());
		}
		return min;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinY()
	 */
	@Override
	public double getMinY() {
		double min = Double.POSITIVE_INFINITY;
		for(SrlShape shape : getSubShapes("shape")){
			min = Math.min(min, shape.getMinY());
		}
		return min;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxX()
	 */
	@Override
	public double getMaxX() {
		double max = Double.NEGATIVE_INFINITY;
		for(SrlShape shape : getSubShapes("shape")){
			max = Math.max(max, shape.getMaxX());
		}
		return max;
	}

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxY()
	 */
	@Override
	public double getMaxY() {
		double max = Double.NEGATIVE_INFINITY;
		for(SrlShape shape : getSubShapes("shape")){
			max = Math.max(max, shape.getMaxY());
		}
		return max;
	}




}
