package edu.tamu.srl.processing;

import edu.tamu.srl.object.shape.SrlPoint;

public class LineProcessing {

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
	 * Returns the slope of the line. Note that 
	 * if this line is vertical, this will cause an error.
	 * This is the m in the line equation in y = mx + b.
	 * @return slope of the line
	 */
	public static double getSlope(double x1, double y1, double x2, double y2){
	    return (y2 - y1)/ (x2 - x1);
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
	  
}
