package edu.tamu.srl.object.shape;

import edu.tamu.srl.settings.SrlInitialSettings;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Point data class.
 *
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlPoint extends SrlObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * A counter that keeps track of where you are in the history of points
     */
    private int mCurrentElement = -1;

    /**
     * Points can have pressure depending on the input device
     */
    private Double mPressure = null;

    /**
     * Tilt in the X direction when the point was created.
     */
    private Double mTiltX = null;

    /**
     * Tilt in the Y direction when the point was created.
     */
    private Double mTiltY = null;

    /**
     * Holds an history list of the x points
     * Purpose is so that we can redo and undo and go back to the original points
     */
    private ArrayList<Double> mXList = new ArrayList<Double>();

    /**
     * Holds a history list of the y points
     * Purpose is so that we can redo and undo and go back to the original points
     * Note that this means that we cannot just set one value, and not the other.
     */
    private ArrayList<Double> mYList = new ArrayList<Double>();

    /**
     * Because it is serializable, that means we can save to a file, but if we change this class
     * it might break the ability to read the file back in.
     */

    /**
     * Creates a point with the initial points at x,y
     *
     * @param x the initial x point
     * @param y the initial y point
     */
    public SrlPoint(final double x, final double y) {
        setPointDataFromConstructor(x, y);
    }

    /**
     * Creates a point with the initial points at x,y
     *
     * @param x    the initial x point
     * @param y    the initial y point
     * @param time the time the point was made
     */
    public SrlPoint(final double x, final double y, final long time) {
        super(time);
        setPointDataFromConstructor(x, y);
    }

    /**
     * New constructor that takes a UUID.
     *
     * @param x    x value of the point.
     * @param y    y value of the point.
     * @param time time stamp.
     * @param id   point ID (.equals)
     */
    public SrlPoint(final double x, final double y, final long time, final UUID id) {
        super(time, id);
        setPointDataFromConstructor(x, y);
    }

    /**
     * Creates a new point with the specified values
     *
     * @param x
     * @param y
     * @param time
     * @param id
     * @param tiltX
     * @param tiltY
     * @param pressure
     */
    public SrlPoint(final double x, final double y, final long time, final UUID id, final double tiltX,
            final double tiltY, final double pressure) {
        this(x, y, time, id);
        setTilt(tiltX, tiltY);
        setPressure(pressure);
    }

    /**
     * Construct a new point with the same elements.
     *
     * @param p the point that is being copied.
     */
    public SrlPoint(final SrlPoint p) {
        super(p);
        this.mCurrentElement = p.mCurrentElement;
        this.mPressure = p.mPressure;
        this.mTiltX = p.mTiltX;
        this.mTiltY = p.mTiltY;
        for (int i = 0; i < p.mXList.size(); i++) {
            mXList.add((double) p.mXList.get(i));
            mYList.add((double) p.mYList.get(i));
        }
    }

    /**
     * Takes
     * @param x The location in the x direction of the point.
     * @param y The location in the y direction of the point.
     */
    private void setPointDataFromConstructor(final double x, final double y) {
        setP(x, y);
        setDescription("Initial Points: " + x + "," + y);
        setType("Point");
        setName("p");
    }

    /**
     * Return the distance from the point specified by (x1, y1 ) to the specified by (x2, y2)
     *
     * @param x2 the x value of the other point
     * @param y2 the y value of the other point
     * @return the distance
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        double xdiff = x1 - x2;
        double ydiff = y1 - y2;
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }

    @Override
    protected void applyTransform(AffineTransform xform, Set<SrlObject> xformed) {
        if (xformed.contains(this)) {
            return;
        }
        xformed.add(this);
        Point2D point = xform.transform(new Point2D.Double(getX(), getY()),
                new Point2D.Double());
        setP(point.getX(), point.getY());
    }

    /**
     * Calculated the bounding box of the shape
     */
    protected void calculateBBox() {
        mBoundingBox = new SrlRectangle(this, this);
    }

    /**
     * Called to calculate the convex hull of the object.
     */
    @Override protected void calculateConvexHull() {

    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.object.shape.SrlObject}.
     * This cloned object is only a shallow copy.
     */
    @Override public Object clone() {
        return new SrlPoint(this);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @Override public SrlObject deepClone() {
        return new SrlPoint(this);
    }

    /**
     * Compare this point to another point based on time.
     *
     * @param p point to compare to.
     * @return time difference between points.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public int compareTo(SrlPoint p) {
        final int timeDiff = (int) (this.getTime() - p.getTime());
        if (timeDiff != 0) {
            return timeDiff;
        }

        final int xDiff = (int) (this.getX() - p.getX());
        if (xDiff != 0) {
            return xDiff;
        }

        final int yDiff = (int) (this.getY() - p.getY());
        if (yDiff != 0) {
            return yDiff;
        }

        final int idDiff = this.getId().compareTo(p.getId());
        // if(idDiff!=0)
        return idDiff;

    }

    /**
     * In this case the same as equalsBy Content
     */
    public boolean equals(SrlObject o) {
        return equalsByContent(o);
    }

    @Override
    public boolean equalsByContent(SrlObject other) {
        if (!(other instanceof SrlPoint)) {
            return false;
        }
        SrlPoint otherpoint = (SrlPoint) other;
        if (getPressure() != otherpoint.getPressure()) {
            return false;
        }
        if (getTiltX() != otherpoint.getTiltX()) {
            return false;
        }
        if (getTiltY() != otherpoint.getTiltY()) {
            return false;
        }
        if (getX() != otherpoint.getX()) {
            return false;
        }
        if (getY() != otherpoint.getY()) {
            return false;
        }
        if (getTime() != otherpoint.getTime()) {
            return false;
        }
        return true;
    }

    /**
     * Check if the x y t values match (probably the same initial point)
     *
     * @param p
     * @return
     */
    public boolean equalsXYTime(SrlPoint p) {
        return (p.getX() == getX() && p.getY() == getY() && p.getTime() == getTime());
    }

    /**
     * Return an object drawable by AWT
     * return awt point
     */
    public Point getAWT() {
        return new Point((int) getX(), (int) getY());
    }

    /**
     * Get the x value for the first point in the history
     *
     * @return
     */
    public double getInitialX() {
        if (mXList.size() == 0) {
            return Double.NaN;
        }
        return mXList.get(0);
    }

    /**
     * Get the y value for the first point in the history
     *
     * @return
     */
    public double getInitialY() {
        if (mYList.size() == 0) {
            return Double.NaN;
        }
        return mYList.get(0);
    }

    @Override
    /**
     * Just returns the x value
     * return x value
     */
    public double getMaxX() {
        return getX();
    }

    @Override
    /**
     * Just returns the y value
     * return y value
     */
    public double getMaxY() {
        return getY();
    }

    @Override
    /**
     * Just returns the x value
     * return x value
     */
    public double getMinX() {
        return getX();
    }

    @Override
    /**
     * Just returns the y value
     * return y value
     */
    public double getMinY() {
        return getY();
    }

    public double getOrigX() {
        return mXList.get(0);
    }

    public double getOrigY() {
        return mYList.get(0);
    }

    /**
     * Points can have pressure depending on the input device
     *
     * @return the pressure of the point
     */
    public Double getPressure() {
        return mPressure;
    }

    /**
     * Sets the pressure of the point
     *
     * @param pressure
     */
    public void setPressure(Double pressure) {
        mPressure = pressure;
    }

    /**
     * Get the tilt in the X direction.
     *
     * @return tilt in the X direction.
     */
    public Double getTiltX() {
        return mTiltX;
    }

    /**
     * Set the tilt in the X direction.
     *
     * @param tiltX tilt in the X direction.
     */
    public void setTiltX(double tiltX) {
        mTiltX = tiltX;
    }

    /**
     * Get the tilt in the Y direction.
     *
     * @return tilt in the Y direction.
     */
    public Double getTiltY() {
        return mTiltY;
    }

    /**
     * Set the tilt in the Y direction.
     *
     * @param tiltY tilt in the Y direction.
     */
    public void setTiltY(double tiltY) {
        mTiltY = tiltY;
    }

    /**
     * Get the current x value of the point
     *
     * @return current x value of the point
     */
    public double getX() {
        return mXList.get(mCurrentElement);
    }

    /**
     * We keep a history of the x values as the point is transformed
     *
     * @return the history of the x values
     */
    public ArrayList<Double> getxList() {
        return mXList;
    }

    /**
     * Get the current y value of the point
     *
     * @return current y value of the point
     */
    public double getY() {
        return mYList.get(mCurrentElement);
    }

    /**
     * We keep a history of the y values as the point is transformed
     *
     * @return the history of the y values
     */
    public ArrayList<Double> getyList() {
        return mYList;
    }

    /**
     * Get the original value of the point
     *
     * @return a point where getx and gety return the first values that were added to the history
     */
    public SrlPoint goBackToInitial() {
        if (mCurrentElement >= 0) {
            mCurrentElement = 0;
        }
        return this;
    }

    @Override
    public int hashCode() {

        return (int) getX() + (int) getY() + (int) getTime();
    }

    /**
     * Scales the point by the amount x and y.
     * Having x and y specified allows one to change the
     * relative dimensions to match another shape if wanted.
     * Usually, these values will be the same to keep the
     * relative dimensions equal
     *
     * @param x the amount to scale in the x direction
     * @param y the amount to scale in the y direction
     */
    public void scale(double x, double y) {
        mXList.add(x * getX());
        mYList.add(y * getY());
        mCurrentElement = mXList.size() - 1;
    }

    public void set(double x, double y, long time) {
        setP(x, y);
        setTime(time);
    }

    /**
     * Delete the entire point history and
     * use these values as the starting point
     *
     * @param x new initial x location
     * @param y new initial y location
     */
    public void setOrigP(double x, double y) {
        mXList = new ArrayList<Double>();
        mYList = new ArrayList<Double>();
        setP(x, y);
    }

    /**
     * Updates the location of the point
     * Also add this point to the history of the points
     * so this can be undone.
     *
     * @param x the new x location for the point
     * @param y the new y location for the point
     */
    public void setP(double x, double y) {
        mXList.add(x);
        mYList.add(y);
        mCurrentElement = mXList.size() - 1;
    }

    /**
     * Set the pressure of the point.
     *
     * @param pressure pressure of the point.
     */
    public void setPressure(double pressure) {
        mPressure = pressure;
    }

    /**
     * Set the tilt of the point.
     *
     * @param tiltX tilt in the X direction.
     * @param tiltY tilt in the Y direction.
     */
    public void setTilt(double tiltX, double tiltY) {
        setTiltX(tiltX);
        setTiltY(tiltY);
    }

    /**
     * Converts the Core Sketch Point object to the equivalent AWT Point .
     *
     * @return the Core Sketch-to-AWT converted point
     */
    public java.awt.Point toAWTpoint() {

        java.awt.Point awtPoint = new java.awt.Point(new Double(getX()).intValue(),
                new Double(getY()).intValue());

        return awtPoint;
    }

    /**
     * Returns a string of [x,y,time]
     *
     * @return a string of [x,t,time]
     */
    public String toString() {
        return "<" + getX() + "," + getY() + "," + getTime() + ">";
    }

    /**
     * Translate the point in the amount x,y
     * Saves the point it was before in case we need to undo the translation
     *
     * @param x amount to move in the x direction
     * @param y amount to move in the y direction
     */
    public void translate(double x, double y) {
        mXList.add(x + getX());
        mYList.add(y + getY());
        mCurrentElement = mXList.size() - 1;
    }

    /**
     * Remove last point update
     * If there is only one x,y value in the history,
     * then it does nothing
     * Returns the updated shape (this)
     */
    public SrlPoint undoLastChange() {
        if (mXList.size() < 2) {
            return this;
        }
        if (mYList.size() < 2) {
            return this;
        }
        mXList.remove(mXList.size() - 1);
        mYList.remove(mYList.size() - 1);
        mCurrentElement -= 1;
        return this;
    }

}
