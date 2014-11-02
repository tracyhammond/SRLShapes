package edu.tamu.srl.object.shape;

import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import edu.tamu.srl.settings.SrlInitialSettings;

/**
 * Stroke data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlStroke extends SrlInterpretedShape implements Serializable{
	
	/**
	 * This is for serialization of the stroke. 
	 * That said if you add member variables, this is no longer 
	 * serializable
	 */
	private static final long serialVersionUID = 1L;
	
	/** 
	 * This variable is simply to save the multiple recomputing of things over and over
	 * It is reset when the points change
	 */
	private transient ArrayList<SrlPoint> m_points = null;
	
	/**
	 * General constructor added because it is needed by superclasses
	 */
	public SrlStroke(){
		super();
	}
	
	/**
	 * @param resampled
	 */
	public SrlStroke(ArrayList<SrlPoint> resampled) {
		super();
		for(SrlPoint p : resampled){
			addPoint(p);
		}
	}
	
	/**
	 * Constructor setting the initial point in the stroke
	 * @param startPoint
	 */
	public SrlStroke(SrlPoint startPoint){
		add(startPoint);
		startPoint.setName("p" + getPoints().size());
	}
	
	/**
	 * This is a constructor to be used in place of the clone method
	 * @param s
	 */
	public SrlStroke(SrlStroke s){
		super(s);
	}

	/**
	 * Adding another point to the stroke
	 * @param point
	 */
	public void addPoint(SrlPoint point){
		flagExternalUpdate();
		add(point);
		point.setName("p" + getPoints().size());
	}
	
	/**
	 * Adds all of the points to the stroke
	 * @param points points to add to the stroke
	 */
	public void addPoints(ArrayList<SrlPoint> points) {
		getPoints().addAll(points);
		
	}	
	
	@Override
	protected void applyTransform(AffineTransform xform, Set<SrlObject> xformed) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean equalsByContent(SrlObject other) {
		SrlStroke otherstroke = (SrlStroke)other;
		int i = 0;
		for(SrlPoint p : getPoints()){			
			if(!p.equalsByContent(otherstroke.getPoint(i))){return false;}
			i++;
		}
		return true;
	}
	
	/**
	 * Flags an external update.
	 * 
	 */
	public void flagExternalUpdate() {
		super.flagExternalUpdate();
		m_points = null;
	}
	
	/**
	 * Gets all the points in the substrokes as well as this stroke
	 * @return
	 */
	public ArrayList<SrlPoint> getAllPoints(){
		return super.getPoints();
	}


	/**
	 * Returns the first point in the stroke.
	 * if the stroke has no points, it returns null.
	 * @return first point in the stroke
	 */
	public SrlPoint getFirstPoint(){
		if (getPoints().size() == 0){
			return null;
		}
		return getPoints().get(0);
	}

	/**
	 * Returns the last point in the stroke
	 * If the stroke has no points, it returns null.
	 * @return last point in the stroke.
	 */
	public SrlPoint getLastPoint(){
		if (getPoints().size() == 0){
			return null;
		}
		return getPoints().get(getPoints().size()-1);
	}


		
	/**
	 * Gets the number of points in the stroke
	 * @return number of points in the stroke
	 */
	public int getNumPoints(){
		return getPoints().size();
	}

	/**
	 * Get the i'th point in the stroke 
	 * The first point has index i = 0
	 * @param i the index of the stroke
	 * @return the point at index i
	 */
	public SrlPoint getPoint(int i){
		if(i >= getPoints().size()){
			return null;
		}
		return getPoints().get(i);
	}

	/**
	 * Gets the immediate points in the stroke since this may also 
	 * contain substrokes, we don't want those points to be included in 
	 * this. (getAllPoints() also gets the sub points in any substrokes)
	 */
	@Override
	public ArrayList<SrlPoint> getPoints(){
		if(m_points != null){return m_points;}
		ArrayList<SrlPoint> allPoints = new ArrayList<SrlPoint>();
		for(SrlObject o: getSubShapes()){
			if(o instanceof SrlPoint){
				allPoints.add((SrlPoint)o);
			}
		}
		m_points = allPoints;
		return allPoints;
	}

	/**
	 * Paints the lines with the thickness based on the initial pressure
	 */
	@Override
	public void paint(Graphics2D g) {
		for(int i = 0; i < getPoints().size() - 1; i++){
			SrlPoint p1 = getPoints().get(i);
			SrlPoint p2 = getPoints().get(i+1);
			g.setStroke(new BasicStroke(p1.getPressure().intValue()));
			g.draw(new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
			if(SrlInitialSettings.InitialDrawPoints){
				p1.paint(g);
				p2.paint(g);
			}
		}
	}
}
