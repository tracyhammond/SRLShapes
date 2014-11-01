/*******************************************************************************
 *  Revision History:<br>
 *  SRL Member - File created
 *  SRL Member - Renamed to SRL Point
 *
 *  <p>
 *  <pre>
 *  This work is released under the BSD License:
 *  (C) 2011 Sketch Recognition Lab, Texas A&M University (hereafter SRL @ TAMU)
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Sketch Recognition Lab, Texas A&M University 
 *        nor the names of its contributors may be used to endorse or promote 
 *        products derived from this software without specific prior written 
 *        permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY SRL @ TAMU ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL SRL @ TAMU BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  </pre>
 *  
 *******************************************************************************/
package srl.sketch.core;

import java.util.Set;
import java.util.UUID;


public class SrlPoint extends AbstractSrlComponent implements ISrlTimeInstant, Comparable<ISrlTimeInstant> {
	public transient final int radius = 5;

	public double x, y;
	public Double pressure;
	public Double tiltX,tiltY;
	public long time;
	
	public UUID id;
	
	public SrlPoint() {
		this(0.0, 0.0);
	}

	public SrlPoint(double x, double y) {
		this(x, y, -1L);
	}

	public SrlPoint(double x, double y, double pressure) {
		this(x, y, -1L, pressure);
	}
	public SrlPoint(double x, double y, long time) {
		this.x = x;
		this.y = y;
		this.time = time;
		id = nextID();
	}

	public SrlPoint(double x, double y, long time, UUID id) {
		this(x, y, time);
		this.id = id;
	}

	public SrlPoint(double x, double y, long time, double pressure) {
		this(x, y, time);
		this.pressure = pressure;
	}
	
	public SrlPoint(SrlPoint copyFrom) {
		this(copyFrom.x, copyFrom.y, copyFrom.time);
		this.id = copyFrom.id;
		this.pressure = copyFrom.pressure;
	}

	public Point2D toPoint2D(){
		return new Point2D.Double(x,y);
	}
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void setTime(long time){
		this.time = time;
	}
	
	public Double getPressure(){
		return pressure;
	}
	public void setPressure(Double pressure){
		this.pressure = pressure;
	}

	/**
	 * @return the tiltX
	 */
	public Double getTiltX() {
		return tiltX;
	}

	/**
	 * @param tiltX the tiltX to set
	 */
	public void setTiltX(Double tiltX) {
		this.tiltX = tiltX;
	}

	/**
	 * @return the tiltY
	 */
	public Double getTiltY() {
		return tiltY;
	}

	/**
	 * @param tiltY the tiltY to set
	 */
	public void setTiltY(Double tiltY) {
		this.tiltY = tiltY;
	}

	@Override
	public void applyTransform(AffineTransform xform, Set<ISrlTransformable> xformed) {
		if (xformed.contains(this))
			return;

		xformed.add(this);

		Point2D point = xform.transform(new Point2D.Double(x, y),
				new Point2D.Double());
		x = point.getX();
		y = point.getY();
	}

	@Override
	public SrlPoint clone() {
		return new SrlPoint(this);
	}

	/**
	 * Distance between two points
	 *
	 * @param other
	 * @return
	 */
	public double distance(SrlPoint other) {
		return distance(other.x, other.y);

	}

	/**
	 * Distance between this point and (x,y)
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double distance(double x, double y) {
		double Xdist = this.x - x;
		double Ydist = this.y - y;

		return Math.sqrt(Xdist * Xdist + Ydist * Ydist);
	}
	
	public double distanceSquared(double x, double y){
		return ((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
	}
	
	public double distanceSquared(SrlPoint pt){
		return distanceSquared(pt.x,pt.y);
	}

	public boolean equalsByContent(AbstractSrlComponent o) {
		if (o instanceof SrlPoint) {
			SrlPoint other = (SrlPoint) o;
			return (Math.abs(other.x-x+other.y-y) < .00001) && time == other.time;
		}
		return false;
	}

	public String toString() {
		return super.toString() + " (" + x + "," + y + ")";
	}

	public long getTime() {
		return time;
	}

    public UUID getID() {
        return id;
    }

    public boolean equalsXYTime(SrlPoint p) {
        return false;
    }

    /**
     * Compares the difference in time.
     * This can be used if you want the points to be sorted by time.
     * @param other Another {@link ISrlTimeInstant}.
     * @return the difference in time.
     */
    @Override
	public int compareTo(ISrlTimeInstant other) {
		return (int)(this.getTime()-other.getTime());
	}

	@Override
	protected void calculateBBox() {
	}

    /**
     * Does different comparisons based on what type it is.
     *
     * If the point is an instance of ISrlTimeInstant then it does {@link #compareTo(ISrlTimeInstant)}
     * @param o
     * @return
     */
    public int compareTo(SrlPoint o) {
        if (o instanceof ISrlTimeInstant) {
            return compareTo((ISrlTimeInstant)o);
        }
        return 0;
    }
}
