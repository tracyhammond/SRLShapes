package edu.tamu.srl.object.shape;


import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Set;

/**
 * Line data class
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlLine extends SrlInterpretedShape{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Starting value of the line
	 */
	private SrlPoint m_p1;
	/**
	 * Ending value of the line
	 */
	private SrlPoint m_p2;  

	/**
	 * Get the starting point of the line
	 * @return starting point of the line
	 */
	public SrlPoint getP1() {
		return m_p1;
	}

	/**
	 * Set the starting point of the line
	 * @param p1 starting point of the line
	 */
	public void setP1(SrlPoint p1) {
		m_p1 = p1;
	}

	/**
	 * Get the ending point of the line
	 * @return ending point of the line
	 */
	public SrlPoint getP2() {
		return m_p2;
	}

	/**
	 * Set the ending point of the line
	 * @param p2 ending point of the line
	 */
	public void setP2(SrlPoint p2) {
		m_p2 = p2;
	}
	
	/**
	 * Constructor that takes two srlpoints
	 * @param p1 start point
	 * @param p2 end point
	 */
	public SrlLine(SrlPoint p1, SrlPoint p2){
		m_p1 = p1;
		m_p2 = p2;
		addInterpretation("line", 1, 1);
	}

	/**
	 * Returns the slope of the line. Note that 
	 * if this line is vertical, this will cause an error.
	 * This is the m in the line equation in y = mx + b.
	 * @return slope of the line
	 */
	public double getSlope(){
		return (getP2().getY() - getP1().getY())/
	        (getP2().getX() - getP1().getX());
	}
	
	/**
	 * Returns the slope of the line. Note that 
	 * if this line is vertical, this will cause an error.
	 * This is the m in the line equation in y = mx + b.
	 * @return slope of the line
	 */
	public static double getSlope(double x1, double y1, double x2, double y2){
	    return (y2 - y1)/ (x2 - x1);
	}
	  
	
	/**
	 * Returns the y-intercept of the line.  (Where the 
	 * line crosses the y axis.) This is the b in 
	 * the equation for a line y = mx + b. Note that this will 
	 * cause an error if this line is vertical.
	 * @return the y-intercept
	 */
	public double getYIntercept(){
	    return getP1().getY() - getSlope() * getP1().getX();
	}
	  
	/**
	 * Returns the y-intercept of the line.  (Where the 
	 * line crosses the y axis.) This is the b in 
	 * the equation for a line y = mx + b. Note that this will 
	 * cause an error if this line is vertical.
	 * @return the y-intercept
	 */
	public static double getYIntercept(double x1, double y1, double x2, double y2){
	    return y1 - getSlope(x1, y1, x2, y2) * x1;
	}
	    
	  
	/**
	 * Returns a vector of doubles A,B,C representing the 
	 * line in the equations Ax + BY = C;
	 * @return the vector, [A,B,C]
	 */
	public static double[] getABCArray(double x1, double y1, double x2, double y2){
	    double A = 0;
	    double B = 0;
	    double C = 0;
	    if(Math.abs(x2-x1) < .001){
	      A = 1;
	      B = 0;
	      C = x1;
	    } else if (Math.abs(y2-y1) < .001){
	      A = 0;
	      B = 1;
	      C = y1;
	    } else {
	      A = - getSlope(x1, y1, x2, y2);
	      B = 1;
	      C = getYIntercept(x1, y1, x2, y2);
	    }
	    double[] array = new double[3];
	    array[0] = A;
	    array[1] = B;
	    array[2] = C;
	    //confirm
	    if((Math.abs(A*x1 +  B*y1 - C) > .001) || (Math.abs(A*x2 + B * y2 - C) > .001)){
	      System.err.println("getABCArray FAILED! A:" + A + ",B:" + B + "C:" + C + " (" + x1 + "," + y1 + "),(" + x2 + "," + y2 + ")");
	    }
	    return array;
	  }
	  
	 /**
	   * Returns the intersection point between this 
	   * line and the given line as if they are infinite.
	   * This function returns null if there is no intersection point
	   * (i.e., the lines are parallel).
	   * @param l the other line
	   * @return The intersection point between the two lines
	   */
	  public static double [] getIntersection(double l1_x1, double l1_y1, double l1_x2, double l1_y2, 
			  double l2_x1, double l2_y1, double l2_x2, double l2_y2){
	    
	    double[] array1 = getABCArray(l1_x1, l1_y1, l1_x2, l1_y2);
	    double[] array2 = getABCArray(l2_x1, l2_y1, l2_x2, l2_y2);
	  
	    double a1 = array1[0];
	    double b1 = array1[1];
	    double c1 = array1[2];
	    double a2 = array2[0];
	    double b2 = array2[1];
	    double c2 = array2[2];
	    double x = 0;
	    double y = 0;
	    boolean done = false;
	    while(!done){
	      done = true;
	      //fix problems from floating point errors
	      if(Math.abs(a1) < .001){a1 = 0;}
	      if(Math.abs(a2) < .001){a2 = 0;}
	      if(Math.abs(b1) < .001){b1 = 0;}
	      if(Math.abs(b2) < .001){b2 = 0;}
	      if(Math.abs(c1) < .001){c1 = 0;}
	      if(Math.abs(c2) < .001){c2 = 0;}
	    //  System.out.println("["+a1+","+b1+","+c1+"]["+a2+","+b2+","+c2+"]");
	      if(a1 == 0 && b1 == 0 && a2 == 0 && b2 == 0){done = true;}
	      else if(a1 == 0 && b1 == 0 && c1 != 0){done = true;}
	      else if(a2 == 0 && b2 == 0 && c2 != 0){done = true;}
	      else if(a2 == 0 && b2 == 0 && c2 == 0){
	        //can pick any point on other line
	        if(b1 == 0){
	          x = c1/a1;
	          y = 0;
	        } else {
	          y = c1/b1;
	          x = 0;        
	        }  
	        done = true;
	      } else if(a1 == 0 && b1 == 0 && c1 == 0){
	        //can pick any point on other line
	        if(b2 == 0){
	          x = c2/a2;
	          y = 0;
	        } else {
	          y = c2/b2;
	          x = 0;        
	        }  
	        done = true;
	      } else if (a1 == 0 && a2 == 0){        
	        y = c1/b1;
	        x = 0;
	        done = true;
	      } else if (a1 == 0 && b2 == 0){
	        y = c1/b1;
	        x = c2/a2;
	        done = true;
	      } else if (a1 == 0){
	        y = c1/b1;
	        x = (c2 - y*b2) / a2;
	        done = true;
	      } else if (b1 == 0 && b2 == 0){
	        x = c1/a1;
	        y = 0;
	        done = true;
	      } else if (b1 == 0 && a2 == 0){
	        x = c1/a1;
	        y = c2/b2;
	        done = true; 
	      } else if (b1 == 0){
	        x = c1/a1;
	        y = (c2 - x*a2)/b2;
	        done = true;
	      } else if (a2 == 0){
	        y = c2/b2;
	        x = (c1 - y*b1)/a1;
	        done = true;
	      } else if (b2 == 0){
	        x = c2/a2;
	        y = (c1 - x*a1)/b1;
	      } else if (b2 * a2 != 0){
	        double fraction = a1/a2;
	        a2 *= fraction;
	        b2 *= fraction;
	        c2 *= fraction;
	        a2 -= a1;
	        b2 -= b1;
	        c2 -= c1;
	        done = false;
	     }
	   }

	    if(Math.abs(a1 * x + b1 * y - c1) > .001 || 
	        Math.abs(a2 * x + b2 * y - c2) > .001){
	      System.err.println("["+a1+","+b1+","+c1+"]["+a2+","+b2+","+c2+"]");
	      System.err.println("Failed Intersection! " + x + "," + y);
	      System.err.println(Math.abs(a1 * x + b1 * y - c1));
	      System.err.println(Math.abs((a2 * x) + (b2 * y) - c2) + " " +  ((a2 * x) + (b2 * y) - c2) + " a2 * x = " + (a2 * x) + "  b2 * y = " + (b2 * y) + " c2 = " + c2 );
	      System.err.println("Failed Intersection! " + x + "," + y);
	      System.err.println("Initial values were: [(" + l1_x1 + "," + l1_y1 + "),(" + l1_x2 + "," + l1_y2 + ")] [(" + l2_x1 + "," + l2_y1 + "),(" + l2_x2 + "," + l2_y2 + ")]");
	      return null;
	    }
	    double [] iPoint = {x,y}; 
	    return iPoint;
	  }
	  
	  
	  public SrlPoint getIntersection(SrlLine l){
		  double[] iPoint = getIntersection(
		    		getP1().getX(), getP1().getY(), 
		    		getP2().getX(), getP2().getY(),
		    		l.getP1().getX(), l.getP1().getY(), 
		    		l.getP2().getX(), l.getP2().getY());
		  return new SrlPoint(iPoint[0], iPoint[1]);
	  }
	  
	  /**
	   * Compute the distance from this line to 
	   * the closest point on the line.
	   * @param p the point to compare
	   * @return the distance
	   */
	  public double distance(SrlPoint p){
	    SrlLine perp = getPerpendicularLine(p);
	    SrlPoint intersectionPoint = getIntersection(perp);
	    if(overBoundingBox(intersectionPoint)){return intersectionPoint.distance(p);}
	    return Math.min(p.distance(getP1()), p.distance(getP2()));
	  }
	 
	  /**
	   * Compute the distance from this line to 
	   * the closest point on the line.
	   * @param p the point to compare
	   * @return the distance
	   */
	  public static double distance(double px, double py, 
			  double l1_x1, double l1_y1, double l1_x2, double l1_y2){   
	    if(l1_x1 == l1_x2 && l1_y1 == l1_y2){return SrlPoint.distance(l1_x1, l1_y1, px, py);}
	    double[] perp = getPerpendicularLine(px, py, 10, l1_x1, l1_y1, l1_x2, l1_y2);
	    double[] iPoint = getIntersection(l1_x1, l1_y1, l1_x2, l1_y2,
	    		perp[0], perp[1], perp[2], perp[3]);
	    if(iPoint != null && overBoundingBox(iPoint[0], iPoint[1], l1_x1, l1_y1, l1_x2, l1_y2)){
	    	return SrlPoint.distance(px, py, iPoint[0], iPoint[1]);}
	    return Math.min(SrlPoint.distance(l1_x1, l1_y1, px, py),
	    		SrlPoint.distance(l1_x2, l1_y2, px, py));
	  }

	  public double distance(SrlLine l){
	    SrlPoint intersectionPoint;
	    if(isParallel(l, .001)){
	      intersectionPoint = l.getP1();
	    } else {
	      intersectionPoint = getIntersection(l);
	    }
	    if(intersectionPoint == null){
	      System.err.println("intersection point is null!");
	      return 0;
	    }
	    double di = Math.max(distance(intersectionPoint), l.distance(intersectionPoint)) ;
	    double d1 = distance(l.getP1());
	    double d2 = distance(l.getP2());    
	    return Math.min(di, Math.min(d1, d2));
	  }

	  public static double distance(double l1_x1, double l1_y1, double l1_x2, double l1_y2, 
			  double l2_x1, double l2_y1, double l2_x2, double l2_y2){
		    double[] iPoint = {l1_x1, l1_y1};
	      if(l1_x1 == l1_x2 && l1_y1 == l1_y2){return distance(l1_x1, l1_y1, l2_x1, l2_y1, l2_x2, l2_y2);}
	      if(l2_x1 == l2_x2 && l2_y1 == l2_y2){return distance(l2_x1, l2_y1, l1_x1, l1_y1, l1_x2, l1_y2);}
		    if(!isParallel(l1_x1, l1_y1, l1_x2, l1_y2, 
		    		  l2_x1, l2_y1, l2_x2, l2_y2, .001)){
		      iPoint = getIntersection(l1_x1, l1_y1, l1_x2, l1_y2, 
		    		  l2_x1, l2_y1,l2_x2, l2_y2);
		    }
		    if(iPoint == null){
		      System.err.println("intersection point is null!");
		      return 0;
		    }
		    double di = Math.max(distance(iPoint[0], iPoint[1], l2_x1, l2_y1, l2_x2, l2_y2), 
		    		distance(iPoint[0], iPoint[1], l1_x1, l1_y1, l1_x2, l1_y2)) ;
		    double d1 = distance(l2_x1, l2_y1, l1_x1, l1_y1, l1_x2, l1_y2);
		    double d2 = distance(l2_x2, l2_y2, l1_x1, l1_y1, l1_x2, l1_y2);    
		    return Math.min(di, Math.min(d1, d2));
		  }
	 
	  /**
	   * Is this point on the bounding box of the point
	   * @param p the point on the bounding box
	   * @return true if the point and line share the bounding box
	   */
	  public boolean overBoundingBox(SrlPoint p){
	    if(p.getX() > getP1().getX() && p.getX() > getP2().getX()){return false;}
	    if(p.getX() < getP1().getX() && p.getX() < getP2().getX()){return false;}
	    if(p.getY() > getP1().getY() && p.getY() > getP2().getY()){return false;}
	    if(p.getY() < getP1().getY() && p.getY() < getP2().getY()){return false;}
	    return true;
	  }
	  
	  
	  /**
	   * Is this point on the bounding box of the point
	   * @param p the point on the bounding box
	   * @return true if the point and line share the bounding box
	   */
	  public static boolean overBoundingBox(double px, double py, 
			  double lx1, double ly1, double lx2, double ly2){
	    if(px > lx1 && px > lx2){return false;}
	    if(px < lx1 && px < lx2){return false;}
	    if(py > ly1 && py > ly2){return false;}
	    if(py < ly1 && py < ly2){return false;}
	    return true;
	  }
	  
	  
	  /**
	   * Returns true if the lines are parallel
	   * within given threshold. If threshold is 0,
	   * the lines have to be perfectly parallel.
	   * If the threshold is 1, all lines are parallel
	   * If the threshold is .5, lines with a difference of  less than 45 
	   * degrees are parallel.
	   * @param line 
	   * @param percent_threshold
	   * @return true if parallel
	   */
	  public boolean isParallel(SrlLine line, double percent_threshold){
	    double threshold = percent_threshold * Math.PI/2;
	    double diff = getAngleInRadians() - line.getAngleInRadians();
	    while(diff < 0){ diff += Math.PI;}
	    while(diff > Math.PI){diff -= Math.PI;}
	    if(diff <= threshold){return true;}
	    if(diff >= Math.PI - threshold){return true;}
	    return false;
	  }
	 
	  /**
	   * Returns true if the lines are parallel
	   * within given threshold. If threshold is 0,
	   * the lines have to be perfectly parallel.
	   * If the threshold is 1, all lines are parallel
	   * If the threshold is .5, lines with a difference of  less than 45 
	   * degrees are parallel.
	   * @param line 
	   * @param percent_threshold
	   * @return true if parallel
	   */
	  public static boolean isParallel(double l1_x1, double l1_y1, double l1_x2, double l1_y2, 
			  double l2_x1, double l2_y1, double l2_x2, double l2_y2, double percent_threshold){
	    double threshold = percent_threshold * Math.PI/2;
	    double diff = getAngleInRadians(l1_x1, l1_y1, l1_x2, l1_y2) - getAngleInRadians(l2_x1, l2_y1, l2_x2, l2_y2);
	    while(diff < 0){ diff += Math.PI;}
	    while(diff > Math.PI){diff -= Math.PI;}
	    if(diff <= threshold){return true;}
	    if(diff >= Math.PI - threshold){return true;}
	    return false;
	  }
	  
	  /**
	   * Returns the angle of the line in degrees
	   * @return angle in degrees from 1-360
	   */
		public double getAngleInDegrees(){
			double angle = 360 - getAngleInRadians() * 180/Math.PI;
			while(angle < 0){angle += 360;}
			while(angle >=360){angle -=360;}
			return angle;
		}
	 
	 /**
	  * returns the angle in degrees within 180
	  * @return angle in degrees
	  */
	  public double getAngleInDegreesUndirected(){
	    double angle = 360 - getAngleInRadians() * 180/Math.PI;
	    while(angle < 0){angle += 180;}
	    while(angle >=180){angle -=180;}
	    return angle;
	  }
	  
	  /**
	   * returns angle in radians
	   * @return angle in radians
	   */
	  public double getAngleInRadians(){
	    return Math.atan2(getP2().getY() - getP1().getY(), 
	        getP2().getX() - getP1().getX());
	  }
		
	  /**
	   * returns angle in radians of two separate lines
	   * @param x1
	   * @param y1
	   * @param x2
	   * @param y2
	   * @return angle in radians of two separate lines
	   */
	  public static double getAngleInRadians(double x1, double y1, double x2, double y2){
		    return Math.atan2(y2 - y1, x2 - x1);
		  }
	  

	  
	  /**
	   * Do the two lines intersect?
	   * @param line second line that might intersect with this one
	   * @return yes if they intersect
	   */
	  public boolean intersects(SrlLine line){
	    //System.debug.println("distance between lines is " + distance(line));
	    if(distance(line) < .1){return true;}
	    return false;
	  }

	  
	  /**
	   * Create a new line from the end point values
	   * @param x1 x value of endpoint 1
	   * @param y1 y value of endpoint 1
	   * @param x2 x value of endpoint 2
	   * @param y2 y value of endpoint 2
	   */
	  public SrlLine(double x1, double y1, double x2, double y2){
	    this(new SrlPoint(x1, y1), new SrlPoint(x2,y2));
	  }
	  
	 /**
	  * This function creates a line segment of the same
	  * length as this line but is perpendicular to this line
	  * and has an endpoint at point p.
	  * @param p endpoint of perpendicular line
	  * @return the perpendicular line segment.
	  */
	 public SrlLine getPerpendicularLine(SrlPoint p){
	    double len = getLength();
	    double [] perpline = getPerpendicularLine(p.getX(), p.getY(), len, 
	    		getP1().getX(), getP1().getY(), getP2().getX(), getP2().getY());
	    return new SrlLine(perpline[0], perpline[1], perpline[2], perpline[3]);
	  }
	  
	  /**
	   * This function creates a line segment of the same
	   * length as this line but is perpendicular to this line
	   * and has an endpoint at point p.
	   * @param p endpoint of perpendicular line
	   * @return the perpendicular line segment.
	   */
	  public static double[] getPerpendicularLine(double newx, double newy, double newlength,
			  double oldx1, double oldy1, double oldx2, double oldy2){
		    double newangle = Math.atan2(oldy2 - oldy1, oldx2 - oldx1) + Math.PI/2;
		    double perpline[] = {newx,newy, newx + Math.cos(newangle) * newlength, 
		    		newy + Math.sin(newangle) * newlength};
		    return perpline;
		  }
	  
	  /**
	   * Returns the euclidean length of the line.  The area of the line
	   * is assumed to be the length times the width, where the width is 1.
	   * return area of line, which equals the euclidean length
	   */ 
	  public double getArea(){
	    return getLength();
	  }
	  
	  /**
	   * Returns the euclidean length of the line between the two endpoints
	   * @return the length
	   */
	  public double getLength(){
	    return m_p1.distance(m_p2);
	  }
	  
	  /**
	   * Returns a new line in the opposite direction.
	   * Note that the points inside the line are not cloned, 
	   * but rather the same original points.
	   * @return a line with the endpoints swapped.
	   */
	  public SrlLine getFlippedLine() {
	    return new SrlLine(getP1(), getP2());
	  }

	  /**
	   * Make this line to be permanently going in the other direction.
	   * Note that the timing of the points will not be changed.
	   * This just swaps the first and the last point.
	   */
	  public void flip() {
	    SrlPoint temp = m_p1;
	    m_p1 = m_p2;
	    m_p2 = temp;    
	  }


	@Override
	/**
	 * Returns the x value of the leftmost point.
	 * return min x of line
	 */
	public double getMinX() {
		return Math.min(getP1().getX(), getP2().getX());
	}


	@Override
	/**
	 * Returns the y value of the lower point 
	 * return min y of line
	 */
	public double getMinY() {
		return Math.min(getP1().getY(), getP2().getY());
	}


	@Override
	/**
	 * Returns the x value of the rightmost point
	 * return max x of line
	 */
	public double getMaxX() {
		return Math.max(getP1().getX(), getP2().getX());
	}


	@Override
	/**
	 * Returns the y value of the highest point
	 * return max y of line
	 */
	public double getMaxY() {
		return Math.max(getP1().getY(), getP2().getY());
	}
	   	
	/**
	 * Returns a string of [x1,y1,t1],[x2,y2,t2]
	 * @return a string of [x1,y1,t1],[x2,y2,t2]
	 */
	public String toString(){
		return getP1().toString() + "," + getP2().toString();
	}

	/**
	 * Translates end points and any subshapes
	 * @param x x amount to translate
	 * @param y y amount to translate
	 */
	public void translate(double x, double y){
		m_p1.translate(x, y);
		m_p2.translate(x, y);
		translateSubShapes(x,y);
	}


	@Override
	public boolean equalsByContent(SrlObject other) {
		if (!(other instanceof SrlLine)){return false;}
		SrlLine otherline = (SrlLine)other;
		if (!getP1().equalsByContent(otherline.getP1())){return false;}
		if (!getP2().equalsByContent(otherline.getP2())){return false;}
		return true;
	}


	@Override
	protected void applyTransform(AffineTransform xform, Set<SrlObject> xformed) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void calculateBBox() {
		// TODO Auto-generated method stub
		
	}
}
