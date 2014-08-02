package edu.tamu.srl.object.shape.primitive;

import java.util.UUID;

public interface ISrlPoint {
	/**
	 * Get the X value of the point.
	 * 
	 * @return x value of the point.
	 */
	public double getX();

	/**
	 * Get the Y value of the point.
	 * 
	 * @return y value of the point.
	 */
	public double getY();

	/**
	 * Get the Time value of the point.
	 * 
	 * @return time value of the point.
	 */
	public long getTime();

	/**
	 * Get the UUID of the IPoint. This is the unique identifier that identifies
	 * this specific point.
	 * 
	 * @return UUID of the IPoint.
	 */
	public UUID getId();

	/**
	 * Determine if two points are equal (same X, Y, and Time values)
	 * 
	 * @param p
	 *            point to compare against.
	 * @return true if points have the same x, y, time; else false.
	 */
	public boolean equalsXYTime(SrlPoint p);

	/**
	 * Get the Euclidean distance between two points.
	 * 
	 * @param p
	 *            second point.
	 * @return Euclidean distance between two points.
	 */
	public double distance(SrlPoint p);

	/**
	 * Get the Euclidean distance between two points.
	 * 
	 * @param x
	 *            x coordinate of second point.
	 * @param y
	 *            y coordinate of second point.
	 * @return Euclidean distance between two points.
	 */
	public double distance(double x, double y);

	/**
	 * Clone an IPoint.
	 * 
	 * @return a new, deep copied IPoint.
	 */
	public Object clone();

	//public void translate(double x, double y);

	//public void scale(double x, double y);
}
