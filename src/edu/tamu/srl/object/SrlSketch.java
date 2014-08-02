/**
 * 
 */
package edu.tamu.srl.object;


/**
 * Collection of shapes (and possibly speech, edit, objects et al.?) 
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlSketch extends SrlObject{

	/**
	 * List of objects in the sketch
	 */
//	private ArrayList<SRL_Object> m_objects = new ArrayList<SRL_Object>();

	/**
	 * Gets the list of objects
	 * @return list of objects
	 */
//	public ArrayList<SRL_Object> getObjects() {
//		return m_objects;
//	}

	/**
	 * Sets the list of objects
	 * @param objects list of objects
	 */
//	public void setObjects(ArrayList<SRL_Object> objects) {
//		m_objects = objects;
//	}
	
	/**
	 * Adds an object
	 * @param object
	 */
//	public void addObject(SRL_Object object){
//		m_objects.add(object);
//	}



	@Override
	public double getMinX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMinY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SrlObject clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
