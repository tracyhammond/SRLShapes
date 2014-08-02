/**
 * 
 */
package edu.tamu.srl.object.shape;

import java.util.ArrayList;

import edu.tamu.srl.object.SrlObject;

/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class JapaneseKanji extends SrlShape {

	public JapaneseKanji(ArrayList<SrlShape> shapes){
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

	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.SRL_Object#clone()
	 */
	@Override
	public JapaneseKanji clone() {
		JapaneseKanji cloned = new JapaneseKanji(getSubShapes("shape"));
		super.clone(cloned);
		return cloned;
	}


}
