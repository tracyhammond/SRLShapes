/**
 * 
 */
package edu.tamu.srl.object.shape;

import java.util.ArrayList;

/**
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class DomainShape extends SrlInterpretedShape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DomainShape(ArrayList<SrlInterpretedShape> shapes){
		for(SrlInterpretedShape o : shapes){
			addSubShape(o);
		}
	}


}
