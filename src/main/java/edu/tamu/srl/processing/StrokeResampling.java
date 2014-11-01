package edu.tamu.srl.processing;

import java.util.ArrayList;

import edu.tamu.srl.object.SrlInterpretation;
import edu.tamu.srl.object.shape.SrlLine;
import edu.tamu.srl.object.shape.SrlPoint;
import edu.tamu.srl.object.shape.SrlStroke;

public class StrokeResampling {

	/**
	 * Resample the strokes. Note that this does not alter the original input stroke. 
	 * @param stroke the stroke to be resampled
	 * @param diagonalThreshold the number of points along the diagonal to define the resample size
	 * @return resampled set of strokes
	 */
	public static SrlStroke resample(SrlStroke stroke, int diagonalThreshold){
		double diagonal = stroke.getLengthOfDiagonal();
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
		SrlStroke copy = new SrlStroke(resampled);
		copy.setName(stroke.getName());
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
	 * Returns a line from the first point to the last point
	 * @return a line from the first point to the last point
	 */
	public SrlLine getEndpointLine(SrlStroke s){
		SrlPoint p1 = s.getFirstPoint();
		SrlPoint p2 = s.getLastPoint();
		SrlLine line = new SrlLine(p1, p2);
		SrlInterpretation interpretation = line.getInterpretation("line");
		interpretation.setConfidence(getEuclideanDistance(s)/getStrokeLength(s));
		interpretation.setComplexity(2);
		return line;
	}
	
	/**
	 * gets a substroke with the points from startIndex up to and including the endindex
	 * @param startIndex the index of first point
	 * @param endIndex the index of the last point
	 * @return
	 */
	public SrlStroke getSubstroke(SrlStroke s, int startIndex, int endIndex){
		System.out.println("creating substroke from " + startIndex + " to " + endIndex);
		SrlStroke substroke = new SrlStroke();
		for(int i = startIndex; i <= endIndex; i++){
			substroke.addPoint(s.getPoint(i));
		}
		return substroke;
	}
	
	/**
	 * Checks if euclidean distance / stroke length is > .95
	 * @return returns true if so
	 */
	public boolean isLine(SrlStroke s){
		if(getLineConfidence(s) > .95){
			return true;
		}
		return false;
	}
	
	/**
	 * returns the distance from start to end divided by stroke length
	 * @return returns the confidence, > .95 is a line.
	 */
	public double getLineConfidence(SrlStroke s){
		return getEuclideanDistance(s)/getStrokeLength(s);
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
	public double getStartAngleCosine(SrlStroke s, int secondPoint) {
		if(s.getNumPoints() <= 1) return Double.NaN;
		if(s.getNumPoints() <= secondPoint){
			secondPoint = s.getNumPoints() - 1;
		}
		
		double xStart, xEnd, yStart, yEnd;		
		xStart = s.getPoint(0).getX();
		yStart = s.getPoint(0).getY();
		xEnd = s.getPoint(secondPoint).getX();
		yEnd = s.getPoint(secondPoint).getY();
		
		while(xStart == xEnd && yStart == yEnd){
			if(s.getNumPoints() <= secondPoint){
				return 0; //TODO this is a hack for really small points
			}
			xEnd = s.getPoint(++secondPoint).getX();
			yEnd = s.getPoint(secondPoint).getY();
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
	 * Auxiliary method used to return a list containing all points
	 * but with duplicated (based on X,Y coordinates) removed
	 * @return list of points with duplicates (based on coordinates) removed
	 */
	public ArrayList<SrlPoint> removeCoordinateDuplicates(SrlStroke s) {
		ArrayList<SrlPoint> p = new ArrayList<SrlPoint>();
		double x1, x2, y1, y2;
		p.add(s.getPoint(0));
		for (int i = 0; i < s.getPoints().size()-1; i++) {
			x1 = s.getPoint(i).getX();
			y1 = s.getPoint(i).getY();
			x2 = s.getPoint(i+1).getX();
			y2 = s.getPoint(i+1).getY();
			if (x1 != x2 || y1 != y2)
				p.add(s.getPoint(i+1));
		}
		return p;
	}

	
	/**
	 * Return the total rotation of the stroke from start to end points
	 * @return total rotation of the stroke
	 */
	public double getRotationSum(SrlStroke s) {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates(s);
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
	public double getRotationAbsolute(SrlStroke s) {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates(s);
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
	 * Return the sine of the starting angle of the stroke
	 * This takes the angle between the initial point and the point specified as the secondPoint
	 * If there are fewer than that many points, it uses the last point.
	 * If there are only 0 or 1 points, this returns NaN.
	 * Note that this is also feature 1 of Rubine.
	 * @param secondPoint which number point should be used for the second point
	 * @return cosine of the starting angle of the stroke
	 */
	public double getStartAngleSine(SrlStroke s, int secondPoint) {
		if(s.getNumPoints() <= 1) return Double.NaN;
		if(s.getNumPoints() <= secondPoint){
			secondPoint = s.getNumPoints() - 1;
		}
		
		double xStart, xEnd, yStart, yEnd;		
		xStart = s.getPoint(0).getX();
		yStart = s.getPoint(0).getY();
		xEnd = s.getPoint(secondPoint).getX();
		yEnd = s.getPoint(secondPoint).getY();
		
		if(xStart == xEnd && yStart == yEnd){
			xEnd = s.getPoint(++secondPoint).getX();
			yEnd = s.getPoint(secondPoint).getY();
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
	 * Return the squared rotation of the stroke from start to end points
	 * @return total squared rotation of the stroke
	 */
	public double getRotationSquared(SrlStroke s) {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates(s);
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
	public double getCurviness(SrlStroke s) {
		ArrayList<SrlPoint> p = removeCoordinateDuplicates(s);
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
	 * Return the cosine of the angle between the start and end point
	 * @return cosine of the ending angle
	 */
	public double getEndAngleCosine(SrlStroke s) {
		if(s.getNumPoints() <= 1) return Double.NaN;
		if (getEuclideanDistance(s)==0)
			return Double.NaN;
		double xDistance =s.getLastPoint().getX() - s.getFirstPoint().getX();
		return xDistance/getEuclideanDistance(s);
	}

	
	/**
	 * Return the cosine of the angle between the start and end point
	 * @return cosine of the ending angle
	 */
	public double getEndAngleSine(SrlStroke s) {
		if(s.getNumPoints() <= 1) return Double.NaN;
		if (getEuclideanDistance(s)==0)
			return Double.NaN;
		double yDistance = s.getLastPoint().getY() - s.getFirstPoint().getY();
		return yDistance/getEuclideanDistance(s);
	}

	/**
	 * Returns the length of the stroke, 
	 * complete with all of its turns
	 * @return length of the stroke
	 */
	public double getStrokeLength(SrlStroke s) {
		double sum = 0;
		double deltaX, deltaY;
		for (int i = 0; i < s.getPoints().size()-1; i++) {
			deltaX = s.getPoint(i+1).getX()-s.getPoint(i).getX();
			deltaY = s.getPoint(i+1).getY()-s.getPoint(i).getY();
			sum += Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
		}
		return sum;
	}
	
	/**
	 * Return the Euclidean distance from the starting point 
	 * to the ending point of the stroke
	 * @return the distance between the starting and ending points of the stroke
	 */
	public double getEuclideanDistance(SrlStroke s) {
		double x0, xn, y0, yn;
		if (s.getPoints().size() == 0)
			return 0;
		x0 = s.getFirstPoint().getX();
		y0 = s.getFirstPoint().getY();
		xn = s.getLastPoint().getX();
		yn = s.getLastPoint().getY();
		return Math.sqrt(Math.pow(xn-x0,2)+Math.pow(yn-y0,2));
	}

	/**
	 * Return the total stroke time
	 * @return total time of the stroke
	 */
	public double getTotalTime(SrlStroke s) {
		if (s.getPoints().size() == 0)
			return Double.NaN;
		return s.getLastPoint().getTime()- s.getFirstPoint().getTime();
	}
	
	/**
	 * Return the maximum stroke speed reached
	 * @return maximum stroke speed reached
	 */
	public double getMaximumSpeed(SrlStroke s) {
		if (s.getPoints().size() == 0)
			return Double.NaN;
		double max = 0;
		double deltaX, deltaY;
		long deltaT;
		ArrayList<SrlPoint> p = removeTimeDuplicates(s);
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
	public ArrayList<SrlPoint> removeTimeDuplicates(SrlStroke s) {
		ArrayList<SrlPoint> points = new ArrayList<SrlPoint>();
		for (SrlPoint p: s.getPoints()) {
			if(points.size() > 0){
				if(points.get(points.size()-1).getTime() == p.getTime()){
					continue;
				}
			}
			points.add(p);
		}
		return points;
	}
	
	
}
