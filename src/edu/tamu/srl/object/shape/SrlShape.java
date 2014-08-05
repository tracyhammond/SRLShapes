package edu.tamu.srl.object.shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import edu.tamu.srl.object.SrlInterpretation;
import edu.tamu.srl.object.SrlObject;
import edu.tamu.srl.object.shape.primitive.SrlPoint;
import edu.tamu.srl.object.shape.stroke.SrlStroke;


/**
 * Shape data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class SrlShape extends SrlObject implements Iterable<SrlShape>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Clones all of the information to the object sent in
	 * @param cloned the new clone object
	 * @return the same cloned object (superfluous return)
	 */
	protected SrlShape clone(SrlShape cloned){
		cloned.setId(getId());
		cloned.setUserCreated(isUserCreated());
		cloned.setName(getName());
		cloned.setTime(getTime());
		for(SrlInterpretation i : m_interpretations){
			cloned.m_interpretations.add(i.clone());
		}
		for(SrlShape o : m_subShapes){
			cloned.m_subShapes.add((SrlShape) o.clone());
		}
		return cloned;
	}
	
	public SrlShape(){super();}
	
	private ArrayList<SrlInterpretation> m_interpretations = new ArrayList<SrlInterpretation>();

	/**
	 * @return the interpretations
	 */
	public ArrayList<SrlInterpretation> getInterpretations() {
		return m_interpretations;
	}

	/**
	 * @param interpretations the interpretations to set
	 */
	public void setInterpretations(ArrayList<SrlInterpretation> interpretations) {
		m_interpretations = interpretations;
	}

	/**
	 * Was this object made up from a collection of subObjects? 
	 * If so they are in this list.
	 * This list usually gets filled in through recognition.
	 * This list can be examined hierarchically.
	 * e.g., an arrow might have three lines inside, and each line might have a stroke.
	 */
	private ArrayList<SrlShape> m_subShapes = new ArrayList<SrlShape>();
	
	
	/**
	 * Adds a subshape to this object. 
	 * This usually happens during recognition, when a new object
	 * is made up from one or more objects
	 * @param subshape
	 */
	public void addSubShape(SrlShape subShape){
		m_subShapes.add(subShape);
	}
	
	/**
	 * Removes a subcomponent from this container.
	 * 
	 * @param subcomponent subcomponent to remove
	 * @return true if something was removed
	 */
	public boolean remove(SrlObject subcomponent) {
		flagExternalUpdate();
		return m_subShapes.remove(subcomponent);
	}
	
	/**
	 * Gets the list of subshapes
	 * @return list of objects that make up this object
	 */
	public ArrayList<SrlShape> getSubShapes(){
		return m_subShapes;
	}
	
	/**
	 * Gets the list of subshapes
	 * @param type the interpretation of the object
	 * @return list of objects that make up this object
	 */
	public ArrayList<SrlShape> getSubShapes(String type){
		ArrayList<SrlShape> shapes = new ArrayList<SrlShape>();
		for(SrlShape o : getSubShapes()){
			if(o.hasInterpretation(type)){
				shapes.add((SrlShape)o);
			}
		}
		return shapes;
	}
	
	
	/**
	 * adds a list of subobjects to this object
	 * @param subobjects list of subobjects to add
	 */
	public void addSubShapes(ArrayList<SrlShape> subshapes) {
		m_subShapes.addAll(subshapes);
		flagExternalUpdate();
	}
	
	/**
	 * Gets a list of all of the objects that make up this object.
	 * This is a recursive search through all of the subobjects.
	 * This objects is also included on the list.
	 * @return
	 */
	public ArrayList<SrlShape> getRecursiveSubShapeList(){
		ArrayList<SrlShape> completeList = new ArrayList<SrlShape>();
		completeList.add(this);
		for(SrlShape o : m_subShapes){
			completeList.addAll(o.getRecursiveSubShapeList());
		}
		return completeList;
	}
	
	
	/**
	 * add an interpretation for an object
	 * @param interpretation a string name representing the interpretation
	 * @param confidence a double representing the confidence
	 * @param complexity a double representing the complexity
	 */
	public void addInterpretation(String interpretation, double confidence, int complexity){
		m_interpretations.add(new SrlInterpretation(interpretation, confidence, complexity));
	}
	
	/**
	 * Checks if it contains an interpretation of a certain type
	 * @param interpretation the string name of the interpretation
	 * @return a boolean if it has such an interpretation or not
	 */
	public boolean hasInterpretation(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().toLowerCase().equals(interpretation.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the interpretation with the highest complexity
	 * and among those the one with the highest confidence
	 * @return best interpretation
	 */
	public SrlInterpretation getBestInterpretation(){
		Collections.sort(m_interpretations);
		return m_interpretations.get(0);
/**		if(m_interpretations.size() == 0) return null;
		SRL_Interpretation bestInterpretation = m_interpretations.get(0);
		for(SRL_Interpretation i : m_interpretations){
			if(i.compareTo(bestInterpretation) > 0){
				bestInterpretation = i;
			}
		}
		return bestInterpretation;
		**/
	}
	
	/**
	 * Checks if it has a particular interpretation, if so, 
	 * it returns it, else it returns null.
	 * @param interpretation the string name of the interpretation
	 * @return the interpretation requested (complete with confidence and complexity)
	 */
	public SrlInterpretation getInterpretation(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().equals(interpretation)){
				return i;
			}
		}
		return null;
	}
	
	/**
	 * Returns the confidence of a particular interpretation.
	 * If the interpretation does not exist, it return -1.
	 * @param interpretation the string name of the interpretation
	 * @return the confidence of that interpretation
	 */
	public double getInterpretationConfidence(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().equals(interpretation)){
				return i.getConfidence();
			}
		}
		return -1;
	}
	
	/**
	 * Returns the complexity of a particular interpretation.
	 * If the interpretation does not exist, it return -1.
	 * @param interpretation the string name of the interpretation
	 * @return the complexity of that interpretation
	 */
	public double getInterpretationComplexity(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().equals(interpretation)){
				return i.getComplexity();
			}
		}
		return -1;
	}
	
	/**
	 * Gets a list of all of the interpretations available for the object
	 * @return the list of interpretations
	 */
	public ArrayList<SrlInterpretation> getAllInterpretations(){
		Collections.sort(m_interpretations);
		return m_interpretations;
	}
	
	/**
	 * Gets a list of all of the strokes that make up this object.
	 * It searches recursively to get all of the strokes of this object.
	 * If it does not have any strokes, the list will be empty.
	 * @return
	 */
	public ArrayList<SrlStroke> getStrokes(){
		ArrayList<SrlStroke> completeList = new ArrayList<SrlStroke>();
		for(SrlShape o : getRecursiveSubShapeList()){
			try {
				if(o.getClass() == Class.forName("edu.tamu.srl.object.shape.stroke.SRL_Stroke")){
					completeList.add((SrlStroke)o);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return completeList;
	}
	
	/**
	 * Gets a list of all of the points that make up this object.
	 * It searches recursively to get all of the strokes of this object.
	 * If it does not have any strokes, and then returns the points in those strokes.
	 * @return
	 */
	public ArrayList<SrlPoint> getPoints(){
		ArrayList<SrlStroke> strokes = getStrokes();
		ArrayList<SrlPoint> allPoints = new ArrayList<SrlPoint>();
		for(SrlStroke stroke : strokes){
			allPoints.addAll(stroke.getPoints());
		}
		return allPoints;
	}
	
	/**
	 * To be called by the classes that extend this 
	 * @param x x amount to translate by
	 * @param y y amount to translate by
	 */
	protected void translateSubShapes(double x, double y){
		for(SrlShape s : getSubShapes()){
			s.translate(x,y);
		}
	}
	
	/**
	 * Adds a subshape to this object at the specified index.
	 * 
	 * @param index point to add the content
	 * @param subcomponent the subshape
	 */
	public void addSubShape(int index, SrlShape subcomponent) {
		m_subShapes.add(index, subcomponent);
		flagExternalUpdate();
	}

	/**
	 * Removes the ith Component from this SContainer
	 * 
	 * @param i
	 *            the ith component of the container
	 * @return the value removed
	 */
	public SrlObject remove(int i) {

		flagExternalUpdate();
		return m_subShapes.remove(i);
	}
	
	/**
	 * Removes a bunch of subcomponents from this SContainer.
	 * 
	 * @param subcomponents
	 *            the collection of subcomponents
	 * @return true if all target subcomponents were removed from myContents,
	 *         false otherwise
	 */
	public boolean removeAll(Collection<? extends SrlObject> subcomponents) {

		flagExternalUpdate();
		return m_subShapes.removeAll(subcomponents);
	}
	

	/**
	 * Gets the size of the myContents.
	 * 
	 * @return the size of the myContents
	 */
	public int size() {

		return m_subShapes.size();
	}

	/**
	 * Clears the container.
	 */
	public void clear() {

		m_subShapes.clear();
		flagExternalUpdate();
	}
	
	public Iterator<SrlShape> iterator() {

		return m_subShapes.iterator();
	}
	
	/**
	 * Checks if this container contains the target component.
	 * 
	 * @param component
	 *            the target component
	 * @return true if this container contains the target component, false
	 *         otherwise
	 */
	public boolean contains(SrlObject component) {

		for (SrlObject sub : m_subShapes)
			if (sub.equals(component))
				return true;

		return false;
	}

	
}
