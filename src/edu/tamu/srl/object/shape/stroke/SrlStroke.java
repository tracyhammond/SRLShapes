package edu.tamu.srl.object.shape.stroke;

import java.util.ArrayList;

import edu.tamu.srl.object.SrlInterpretation;
import edu.tamu.srl.object.shape.SrlShape;
import edu.tamu.srl.object.shape.primitive.SrlLine;
import edu.tamu.srl.object.shape.primitive.SrlPoint;

/**
 * Stroke data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlStroke extends SrlShape{
	
	/**
	 * List of points in the stroke
	 */
	private ArrayList<SrlPoint> m_points= new ArrayList<SrlPoint>();

	public SrlStroke clone(){
		SrlStroke cloned = new SrlStroke();
		super.clone(cloned);
		for(SrlPoint p : m_points){
			cloned.m_points.add(p);
		}
		return cloned;
	}
	
	/**
	 * Constructor creating an empty stroke
	 */
	public SrlStroke(){
		addInterpretation("Stroke", 1, 1);		
	}
	
	/**
	 * Constructor setting the initial point in the stroke
	 * @param startPoint
	 */
	public SrlStroke(SrlPoint startPoint){
		m_points.add(startPoint);
		addInterpretation("stroke", 1, 1);
	}
	
	/**
	 * Gets the center point of the stroke
	 * @return the center of the bounding box
	 */
	public SrlPoint getCenter(){
		return new SrlPoint(getCenterX(), getCenterY());
	}
	
	/**
	 * Adding another point to the stroke
	 * @param point
	 */
	public void addPoint(SrlPoint point){
		m_points.add(point);
	}
	
	/**
	 * Gets the complete list of points in the stroke
	 * @return list of points in the stroke
	 */
	public ArrayList<SrlPoint> getPoints(){
		return m_points;
	}
	
	/**
	 * Get the i'th point in the stroke 
	 * The first point has index i = 0
	 * @param i the index of the stroke
	 * @return the point at index i
	 */
	public SrlPoint getPoint(int i){
		if(i >= m_points.size()){
			return null;
		}
		return m_points.get(i);
	}
	
	/**
	 * Gets the number of points in the stroke
	 * @return number of points in the stroke
	 */
	public int getNumPoints(){
		return m_points.size();
	}
	
	/**
	 * Returns the first point in the stroke.
	 * if the stroke has no points, it returns null.
	 * @return first point in the stroke
	 */
	public SrlPoint getFirstPoint(){
		if (m_points.size() == 0){
			return null;
		}
		return m_points.get(0);
	}
	
	/**
	 * Returns the last point in the stroke
	 * If the stroke has no points, it returns null.
	 * @return last point in the stroke.
	 */
	public SrlPoint getLastPoint(){
		if (m_points.size() == 0){
			return null;
		}
		return m_points.get(m_points.size()-1);
	}
	

	@Override
	/** returns the minimum x value in a stroke
	 * return minimum x value in a stroke
	 */
	public double getMinX() {
		double minx = getFirstPoint().getX();
		for(SrlPoint p : m_points){
			if(p.getX() < minx){
				minx = p.getX();
			}
		}
		return minx;
	}

	@Override
	/** returns the minimum y value in a stroke
	 * return minimum y value in a stroke
	 */
	public double getMinY() {
		double miny = getFirstPoint().getY();
		for(SrlPoint p : m_points){
			if(p.getY() < miny){
				miny = p.getY();
			}
		}
		return miny;
	}

	@Override
	/** returns the maximum x value in a stroke
	 * return maximum x value in a stroke
	 */
	public double getMaxX() {
		double maxx = getFirstPoint().getX();
		for(SrlPoint p : m_points){
			if(p.getX() > maxx){
				maxx = p.getX();
			}
		}
		return maxx;
	}

	@Override
	/** returns the maximum x value in a stroke
	 * return maximum x value in a stroke
	 */
	public double getMaxY() {
		double maxy = Double.NEGATIVE_INFINITY;
		for(SrlPoint p : m_points){
			if(p.getY() > maxy){
				maxy = p.getY();
			}
		}
		return maxy;
	}
	
	/**
	 * Return the cosine of the starting angle of the stroke
	 * This takes the angle between the initial point and the point specified as the secondPoint
	 * If there are fewer than that many points, it uses the last point.
	 * If there are only 0 or 1 points, this returns NaN.
	 * Note that this is also feature 1 of Rubine.
	 * @param secondPoint which number point should be used for the second point
	 * @return cosine of the starting angle of the stroke
	 */
	public double getStartAngleCosine(int secondPoint) {
		if(getNumPoints() <= 1) return Double.NaN;
		if(getNumPoints() <= secondPoint){
			secondPoint = getNumPoints() - 1;
		}
		
		double xStart, xEnd, yStart, yEnd;		
		xStart = getPoint(0).getX();
		yStart = getPoint(0).getY();
		xEnd = getPoint(secondPoint).getX();
		yEnd = getPoint(secondPoint).getY();
		
		while(xStart == xEnd && yStart == yEnd){
			if(getNumPoints() <= secondPoint){
				return 0; //TODO this is a hack for really small points
			}
			xEnd = getPoint(++secondPoint).getX();
			yEnd = getPoint(secondPoint).getY();
		}
		if(xStart == xEnd && yStart == yEnd){
			return Double.NaN;
		}
			
		double sectionWidth = xEnd - xStart;
		double sectionHeight = yEnd - yStart;
		double hypotenuse = Math.sqrt(sectionWidth * sectionWidth + sectionHeight * sectionHeight);
		return sectionWidth / hypotenuse;
	}
	
	/**
	 * Return the sine of the starting angle of the stroke
	 * This takes the angle between the initial point and the point specified as the secondPoint
	 * If there are fewer than that many points, it uses the last point.
	 * If there are only 0 or 1 points, this returns NaN.
	 * Note that this is also feature 1 of Rubine.
	 * @param secondPoint which number point should be used for the second point
	 * @return cosine of the starting angle of the stroke
	 */
	public double getStartAngleSine(int secondPoint) {
		if(getNumPoints() <= 1) return Double.NaN;
		if(getNumPoints() <= secondPoint){
			secondPoint = getNumPoints() - 1;
		}
		
		double xStart, xEnd, yStart, yEnd;		
		xStart = getPoint(0).getX();
		yStart = getPoint(0).getY();
		xEnd = getPoint(secondPoint).getX();
		yEnd = getPoint(secondPoint).getY();
		
		if(xStart == xEnd && yStart == yEnd){
			xEnd = getPoint(++secondPoint).getX();
			yEnd = getPoint(secondPoint).getY();
		}
		
		if(xStart == xEnd && yStart == yEnd){
			return Double.NaN;
		}
			
		double sectionWidth = xEnd - xStart;
		double sectionHeight = yEnd - yStart;
		double hypotenuse = Math.sqrt(sectionWidth * sectionWidth + sectionHeight * sectionHeight);
		return sectionHeight / hypotenuse;
	}
	
	
	/**
	 * Return the cosine of the angle between the start and end point
	 * @return cosine of the ending angle
	 */
	public double getEndAngleCosine() {
		if(getNumPoints() <= 1) return Double.NaN;
		if (getEuclideanDistance()==0)
			return Double.NaN;
		double xDistance = getLastPoint().getX() - getFirstPoint().getX();
		return xDistance/getEuclideanDistance();
	}

	
	/**
	 * Return the cosine of the angle between the start and end point
	 * @return cosine of the ending angle
	 */
	public double getEndAngleSine() {
		if(getNumPoints() <= 1) return Double.NaN;
		if (getEuclideanDistance()==0)
			return Double.NaN;
		double yDistance = getLastPoint().getY() - getFirstPoint().getY();
		return yDistance/getEuclideanDistance();
	}

	/**
	 * Returns the length of the stroke, 
	 * complete with all of its turns
	 * @return length of the stroke
	 */
	public double getStrokeLength() {
		double sum = 0;
		double deltaX, deltaY;
		for (int i = 0; i < getPoints().size()-1; i++) {
			deltaX = getPoint(i+1).getX()-getPoint(i).getX();
			deltaY = getPoint(i+1).getY()-getPoint(i).getY();
			sum += Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
		}
		return sum;
	}
	
	/**
	 * Return the Euclidean distance from the starting point 
	 * to the ending point of the stroke
	 * @return the distance between the starting and ending points of the stroke
	 */
	public double getEuclideanDistance() {
		double x0, xn, y0, yn;
		if (getPoints().size() == 0)
			return 0;
		x0 = getFirstPoint().getX();
		y0 = getFirstPoint().getY();
		xn = getLastPoint().getX();
		yn = getLastPoint().getY();
		return Math.sqrt(Math.pow(xn-x0,2)+Math.pow(yn-y0,2));
	}

	/**
	 * Return the total stroke time
	 * @return total time of the stroke
	 */
	public double getTotalTime() {
		if (getPoints().size() == 0)
			return Double.NaN;
		return getLastPoint().getTime()-getFirstPoint().getTime();
	}
	
	/**
	 * Return the maximum stroke speed reached
	 * @return maximum stroke speed reached
	 */
	public double getMaximumSpeed() {
		if (getPoints().size() == 0)
			return Double.NaN;
		double max = 0;
		double deltaX, deltaY;
		long deltaT;
		ArrayList<SrlPoint> p = removeTimeDuplicates();
		for (int i = 0; i < p.size()-1; i++) {
			deltaX = p.get(i+1).getX()-p.get(i).getX();
			deltaY = p.get(i+1).getY()-p.get(i).getY();
			deltaT = p.get(i+1).getTime()-p.get(i).getTime();
			double speed = (Math.pow(deltaX,2)+Math.pow(deltaY,2))/Math.pow(deltaT,2);
			if (speed > max)
				max = speed;
		}
		return max;
	}
	
	/**
	 * Auxiliary method used to return a list containing all points
	 * but with duplicated (based on time) removed
	 * @return list of points with duplicates (based on time) removed
	 */
	public ArrayList<SrlPoint> removeTimeDuplicates() {
		ArrayList<SrlPoint> points = new ArrayList<SrlPoint>();
		for (SrlPoint p: m_points) {
			if(points.size() > 0){
				if(points.get(points.size()-1).getTime() == p.getTime()){
					continue;
				}
			}
			points.add(p);
		}
		return points;
	}

	/**
	 * Auxiliary method used to return a list containing all points
	 * but with duplicated (based on X,Y coordinates) removed
	 * @return list of points with duplicates (based on coordinates) removed
	 */
	public ArrayList<SrlPoint> removeCoordinateDuplicates() {
		ArrayList<SrlPoint> p = new ArrayList<SrlPoint>();
		double x1, x2, y1, y2;
		p.add(getPoint(0));
		for (int i = 0; i < getPoints().size()-1; i++) {
			x1 = getPoint(i).getX();
			y1 = getPoint(i).getY();
			x2 = getPoint(i+1).getX();
			y2 = getPoint(i+1).getY();
			if (x1 != x2 || y1 != y2)
				p.add(getPoint(i+1));
		}
		return p;
	}
	
	/**
	 * Calculates the rotation from point startP to two points further.
	 * Calculates the line between startP and the next point,
	 * and then the next two points,
	 * and then returns the angle between the two.
	 * @param points
	 * @param startP
	 * @return
	 */
	private double rotationAtAPoint(ArrayList<SrlPoint> points, 
			int startP){
		if(points.size() < startP+2){
			return Double.NaN;
		}
		double mx = points.get(startP+1).getX()-points.get(startP).getX();
		double my = points.get(startP+1).getY()-points.get(startP).getY();
	    return Math.atan2(my, mx);
	}
	
	/**
	 * Return the total rotation of the stroke from start to end points
	 * @return total rotation of the stroke
	 */
	public double getRotationSum() {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates();
		double sum = 0;
		double lastrot = Double.NaN;
		for (int i = 1; i < p.size()-2; i++) {
			double rot = rotationAtAPoint(p, i);
			if(lastrot == Double.NaN) lastrot = rot;
			while((i > 0) && (rot - lastrot > Math.PI)){
				rot = rot - 2*Math.PI;
			}
		    while((i > 0) && (lastrot - rot > Math.PI)){
		    	rot = rot + 2*Math.PI;
			}  
		    sum += rot;
		}
		return sum;
	}

	/**
	 * Return the absolute rotation of the stroke from start to end points
	 * @return total absolute rotation of the stroke
	 */
	public double getRotationAbsolute() {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates();
		double sum = 0;
		double lastrot = Double.NaN;
		for (int i = 1; i < p.size()-2; i++) {
			double rot = rotationAtAPoint(p, i);
			if(lastrot == Double.NaN) lastrot = rot;
			while((i > 0) && (rot - lastrot > Math.PI)){
				rot = rot - 2*Math.PI;
			}
		    while((i > 0) && (lastrot - rot > Math.PI)){
		    	rot = rot + 2*Math.PI;
			}  
		    sum += Math.abs(rot);
		}
		return sum;
	}
	

	/**
	 * Return the squared rotation of the stroke from start to end points
	 * @return total squared rotation of the stroke
	 */
	public double getRotationSquared() {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates();
		double sum = 0;
		double lastrot = Double.NaN;
		for (int i = 1; i < p.size()-2; i++) {
			double rot = rotationAtAPoint(p, i);
			if(lastrot == Double.NaN) lastrot = rot;
			while((i > 0) && (rot - lastrot > Math.PI)){
				rot = rot - 2*Math.PI;
			}
		    while((i > 0) && (lastrot - rot > Math.PI)){
		    	rot = rot + 2*Math.PI;
			}  
		    sum += rot * rot;
		}
		return sum;
	}

	
	/**
	 * Return the squared rotation of the stroke from start to end points
	 * @return total squared rotation of the stroke
	 */
	public double getCurviness() {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates();
		double sum = 0;
		double lastrot = Double.NaN;
		for (int i = 1; i < p.size()-2; i++) {
			double rot = rotationAtAPoint(p, i);
			if(lastrot == Double.NaN) lastrot = rot;
			while((i > 0) && (rot - lastrot > Math.PI)){
				rot = rot - 2*Math.PI;
			}
		    while((i > 0) && (lastrot - rot > Math.PI)){
		    	rot = rot + 2*Math.PI;
			}  
		    if(rot < .331)
		    	sum += rot;
		}
		return sum;
	}

	/**
	 * Adds all of the points to the stroke
	 * @param points points to add to the stroke
	 */
	public void addPoints(ArrayList<SrlPoint> points) {
		m_points.addAll(points);
		
	}

	/**
	 * @param resampled
	 */
	public void setPoints(ArrayList<SrlPoint> resampled) {
		m_points = resampled;
		
	}
	
	/**
	 * Resample the strokes. Note that this does not alter the original input stroke. 
	 * @param stroke the stroke to be resampled
	 * @param diagonalThreshold the number of points along the diagonal to define the resample size
	 * @return resampled set of strokes
	 */
	public static SrlStroke resample(SrlStroke stroke, int diagonalThreshold){
		SrlStroke copy = stroke.clone();
		double diagonal = copy.getLengthOfDiagonal();
		double pointDistance = diagonal/diagonalThreshold;
		double currentDistance = 0;
	//	System.out.println("pointDistance = " + pointDistance);
		ArrayList<SrlPoint> resampled = new ArrayList<SrlPoint>();
		SrlPoint currentPoint = stroke.getFirstPoint();
		resampled.add(stroke.getFirstPoint());
		for(SrlPoint p : stroke.getPoints()){
			currentDistance += currentPoint.distance(p);
			while(currentDistance > pointDistance){
				double x = currentPoint.getX() + (pointDistance/currentDistance)*(p.getX() - currentPoint.getX());
				double y = currentPoint.getY() + (pointDistance/currentDistance)*(p.getY() - currentPoint.getY());
				long t = currentPoint.getTime() + (long) (pointDistance/currentDistance)*(p.getTime() - currentPoint.getTime());
				SrlPoint q = new SrlPoint(x,y,t);
				resampled.add(q);
	//			System.out.println("adding : " + q + ", d = " + currentDistance);
				currentDistance -= pointDistance;
			}
		//	System.out.println("passing : " + p + ", d = " + currentDistance);
			currentPoint = p;
		}
		resampled.add(stroke.getLastPoint());
		copy.setPoints(resampled);
		return copy;
	}
	
	/**
	 * This returns a resampled set of strokes with a default size between the points
	 * of diagonal/40. The initial stroke is not changed.
	 * @param stroke the stroke to be resampled
	 * @return a new stroke with the points resampled.
	 */
	public static SrlStroke resample(SrlStroke stroke){
		return resample(stroke, 20);
	}

	/**
	 * Checks if euclidean distance / stroke length is > .95
	 * @return returns true if so
	 */
	public boolean isLine(){
		if(getLineConfidence() > .95){
			return true;
		}
		return false;
	}
	
	/**
	 * returns the distance from start to end divided by stroke length
	 * @return returns the confidence, > .95 is a line.
	 */
	public double getLineConfidence(){
		return getEuclideanDistance()/getStrokeLength();
	}
	
	/**
	 * Returns a line from the first point to the last point
	 * @return a line from the first point to the last point
	 */
	public SrlLine getEndpointLine(){
		SrlPoint p1 = getFirstPoint();
		SrlPoint p2 = getLastPoint();
		SrlLine line = new SrlLine(p1, p2);
		SrlInterpretation interpretation = line.getInterpretation("line");
		interpretation.setConfidence(getEuclideanDistance()/getStrokeLength());
		interpretation.setComplexity(2);
		return line;
	}
	
	
	/**
	 * gets a substroke with the points from startIndex up to and including the endindex
	 * @param startIndex the index of first point
	 * @param endIndex the index of the last point
	 * @return
	 */
	public SrlStroke getSubstroke(int startIndex, int endIndex){
		System.out.println("creating substroke from " + startIndex + " to " + endIndex);
		SrlStroke substroke = new SrlStroke();
		for(int i = startIndex; i <= endIndex; i++){
			substroke.addPoint(getPoint(i));
		}
		return substroke;
	}

	/**
	 * Translates each of the points in the stroke
	 * Also translates each of the subshapes in case this stroke is made up of other strokes
	 */
	public void translate(double x, double y){
		for(SrlPoint p : m_points){
			p.translate(x, y);
		}
		translateSubShapes(x,y);
	}
	
}
