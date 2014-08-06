package edu.tamu.srl.object.shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;


/**
 * Shape data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class SrlShape extends SrlObject implements Iterable<SrlObject>{

	/**
	 * This is for serializable. Note that if the code is recompiled with different 
	 * member methods, the serializable will not be able to load in previously saved 
	 * information
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Was this object made up from a collection of subObjects? 
	 * If so they are in this list.
	 * This list usually gets filled in through recognition.
	 * This list can be examined hierarchically.
	 * e.g., an arrow might have three lines inside, and each line might have a stroke.
	 */
	private ArrayList<SrlObject> m_subShapes = new ArrayList<SrlObject>();
	

	public Iterator<SrlObject> iterator() {
		return getSubShapes().iterator();
	}
	
	/**
	 * Constructor specifically to replace the clone() method
	 * @param s
	 */
	public SrlShape(SrlShape s){
		super(s);
		for(SrlObject sub : s.getSubShapes()){
			if(sub instanceof SrlPoint){
				this.add(new SrlPoint((SrlPoint)sub));
			} else if (sub instanceof SrlStroke){
				this.add(new SrlStroke((SrlStroke)sub));
			}
		}
	}
	
	/**
	 * Empty super constructor must exist for later
	 */
	public SrlShape(){
		super();
	}
	
	/**
	 * Adds a subshape to this object at the specified index.
	 * 
	 * @param index point to add the content
	 * @param subcomponent the subshape
	 */
	public void addSubShape(int index, SrlObject subcomponent) {
		m_subShapes.add(index, subcomponent);
		flagExternalUpdate();
	}

	/**
	 * Adds a subshape to this object. 
	 * This usually happens during recognition, when a new object
	 * is made up from one or more objects
	 * @param subshape
	 */
	public void addSubShape(SrlObject subShape){
		m_subShapes.add(subShape);
	}
	
	/**
	 * Adds a subshape to this object. 
	 * This usually happens during recognition, when a new object
	 * is made up from one or more objects
	 * @param subshape
	 */
	public void add(SrlObject subShape){
		flagExternalUpdate();
		m_subShapes.add(subShape);
	}
	
	
	/**
	 * adds a list of subobjects to this object
	 * @param subobjects list of subobjects to add
	 */
	public void addSubShapes(ArrayList<SrlObject> subshapes) {
		m_subShapes.addAll(subshapes);
		flagExternalUpdate();
	}
	
	/**
	 * adds a list of subobjects to this object
	 * @param subobjects list of subobjects to add
	 */
	public void addAll(ArrayList<SrlObject> subshapes) {
		m_subShapes.addAll(subshapes);
		flagExternalUpdate();
	}
	/**
	 * Clears the container.
	 */
	public void clear() {
		m_subShapes.clear();
		flagExternalUpdate();
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
			if (sub.equals(component)){
				return true;
			} else if(sub instanceof SrlShape){
				if(((SrlShape)sub).contains(component)){return true;}
			}
		return false;
	}
	
	
	/**
	 * Gets the ith component.
	 * 
	 * @param i
	 *            the index
	 * @return the ith component
	 */
	public SrlObject get(int i) {

		return m_subShapes.get(i);
	}

	/**
	 * Gets the number of components.
	 * 
	 * @return the number of components
	 */
	public int getComponentCount() {

		return m_subShapes.size();
	}

	/**
	 * Gets a list of all of the points that make up this object.
	 * It searches recursively to get all of the strokes of this object.
	 * If it does not have any strokes, and then returns the points in those strokes.
	 * @return
	 */
	public ArrayList<SrlPoint> getPoints(){
		ArrayList<SrlPoint> allPoints = new ArrayList<SrlPoint>();
		for(SrlObject o: m_subShapes){
			if(o instanceof SrlPoint){
				allPoints.add((SrlPoint)o);
			} else if (o instanceof SrlShape){
				allPoints.addAll((ArrayList<SrlPoint>)((SrlShape)o).getPoints());
			}
		}
		return allPoints;
	}
	
	/**
	 * Gets a list of all of the objects that make up this object.
	 * This is a recursive search through all of the subobjects.
	 * This objects is also included on the list.
	 * @return
	 */
	public ArrayList<SrlInterpretedShape> getRecursiveSubShapeList(){
		ArrayList<SrlInterpretedShape> completeList = new ArrayList<SrlInterpretedShape>();
		completeList.add((SrlInterpretedShape)this);
		for(SrlObject o : m_subShapes){
			if(o instanceof SrlInterpretedShape){
				SrlInterpretedShape sis = (SrlInterpretedShape) o;				
				completeList.addAll(sis.getRecursiveSubShapeList());
			}
		}
		return completeList;
	}
	
	/**
	 * Find the stroke by ID.
	 * 
	 * @param id
	 *            the ID
	 * @return the stroke with the given ID
	 */
	public SrlStroke getStroke(UUID id) {
		for (SrlObject comp : getRecursiveSubShapeList())
			if (comp instanceof SrlStroke && comp.getId().equals(id))
				return (SrlStroke) comp;
		return null;
	}
	
	/**
	 * Gets a list of all of the strokes that make up this object.
	 * It searches recursively to get all of the strokes of this object.
	 * If it does not have any strokes, the list will be empty.
	 * @return
	 */
	public ArrayList<SrlStroke> getStrokes(){
		ArrayList<SrlStroke> completeList = new ArrayList<SrlStroke>();
		for(SrlObject o : m_subShapes){
			if(o instanceof SrlStroke){
				completeList.add((SrlStroke)o);
			} else if(o instanceof SrlShape){
				completeList.addAll(((SrlShape)o).getStrokes());
			}
		}
		return completeList;
	}
	
	/**
	 * Gets the list of subshapes
	 * @return list of objects that make up this object
	 */
	public ArrayList<SrlObject> getSubShapes(){
		return m_subShapes;
	}
	
	/**
	 * Returns the most recent time of any of the subcomponents
	 */
	public long getTime() {

		if (getTime() == -1L) {
			for (SrlObject comp : m_subShapes)
				setTime(Math.max(getTime(), comp.getTime()));
		}
		return getTime();
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
	 * To be called by the classes that extend this 
	 * @param x x amount to translate by
	 * @param y y amount to translate by
	 */
	protected void translateSubShapes(double x, double y){
		for(SrlObject s : getSubShapes()){
			s.translate(x,y);
		}
	}

	/**
	 * Calculates the bounding box of the shape
	 */
	protected void calculateBBox() {
		setBoundingBox(new SrlRectangle(new SrlPoint(getMinX(), getMinY()), 
				new SrlPoint(getMaxX(), getMaxY())));
	}

	/**
	 * Checks if this container contains any of the given components.
	 * 
	 * @param components
	 *            the target components
	 * @return true if this container contains any of the target components,
	 *         false otherwise
	 */
	public boolean containsAny(Collection<? extends SrlObject> components) {

		for (SrlObject component : components)
			if (contains(component))
				return true;

		return false;
	}
	
	
	
	/* (non-Javadoc)
	 * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
	 */
	@Override
	public double getMinX() {
		double min = Double.POSITIVE_INFINITY;
		for(SrlObject shape : getSubShapes()){
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
		for(SrlObject shape : getSubShapes()){
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
		for(SrlObject shape : getSubShapes()){
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
		for(SrlObject shape : getSubShapes()){
			max = Math.max(max, shape.getMaxY());
		}
		return max;
	}

	/**
	 * Translate the object by the amount x,y
	 * @param x
	 * @param y
	 */
	public void translate(double x, double y){
		for(SrlObject o : getSubShapes()){
			o.translate(x,y);
		}
	}
	
}
