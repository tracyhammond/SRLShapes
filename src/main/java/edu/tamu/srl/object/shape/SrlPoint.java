package edu.tamu.srl.object.shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
     * A counter that keeps track of where you are in the history of points.
     */
    private int mCurrentElement = -1;

    /**
     * Points can have pressure depending on the input device.
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
     * Holds an history list of the x points.
     * Purpose is so that we can redo and undo and go back to the original points
     */
    private ArrayList<Double> mXList = new ArrayList<Double>();

    /**
     * Holds a history list of the y points.
     * Purpose is so that we can redo and undo and go back to the original points
     * Note that this means that we cannot just set one value, and not the other.
     */
    private ArrayList<Double> mYList = new ArrayList<Double>();

    /**
     * Creates a point with the initial points at x,y.
     *
     * @param x the initial x point
     * @param y the initial y point
     */
    public SrlPoint(final double x, final double y) {
        setPointDataFromConstructor(x, y);
    }

    /**
     * Creates a point with the initial points at x,y.
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
     * @param id   point ID
     */
    public SrlPoint(final double x, final double y, final long time, final UUID id) {
        super(time, id);
        setPointDataFromConstructor(x, y);
    }

    /**
     * Creates a new point with the specified values.
     *
     * @param x    x value of the point.
     * @param y    y value of the point.
     * @param time time stamp.
     * @param id   point ID
     * @param tiltX the pen tiltX of the point.
     * @param tiltY the pen tiltY of the point.
     * @param pressure the pressure at which the point was created.
     */
    public SrlPoint(final double x, final double y, final long time, final UUID id, final double tiltX,
            final double tiltY, final double pressure) {
        this(x, y, time, id);
        setTilt(tiltX, tiltY);
        setPressure(pressure);
    }

    /**
     * Construct a new point with the same elements.
     * This performs a deep copy.
     *
     * @param p the point that is being copied.
     */
    public SrlPoint(final SrlPoint p) {
        this(p, true);
    }

    /**
     * Construct a new point with the same elements.
     * This can perform a deep copy.
     * The shallow copy only copies the first point and the current point in the history of points.
     * @param deep True if a deep copy is wanted otherwise a shallow copy is performed.
     * @param p The point that is being copied.
     */
    public SrlPoint(final SrlPoint p, final boolean deep) {
        super(p);
        this.mCurrentElement = p.mCurrentElement;
        this.mPressure = p.mPressure;
        this.mTiltX = p.mTiltX;
        this.mTiltY = p.mTiltY;
        if (deep) {
            for (int i = 0; i < p.mXList.size(); i++) {
                mXList.add((double) p.mXList.get(i));
                mYList.add((double) p.mYList.get(i));
            }
        } else {
            // original point.
            mXList.add((double) p.mXList.get(0));
            mYList.add((double) p.mYList.get(0));

            // current point (if it exist)
            if (mXList.size() > 1 && mYList.size() > 1) {
                mXList.add((double) p.mXList.get(p.mXList.size() - 1));
                mYList.add((double) p.mYList.get(p.mYList.size() - 1));
            }
        }
    }

    /**
     * Takes the points and sets them (these values can be defined from multiple constructors.
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
     * Return the distance from the point specified by (x1, y1 ) to the specified by (x2, y2).
     *
     * @param x1 the x value of the first point.
     * @param y1 the y value of the first point.
     * @param x2 the x value of the other point.
     * @param y2 the y value of the other point.
     * @return the distance
     */
    public static double distance(final double x1, final double y1, final double x2, final double y2) {
        final double xdiff = x1 - x2;
        final double ydiff = y1 - y2;
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }

    @SuppressWarnings("checkstyle:designforextension")
    @Override
    protected void applyTransform(final AffineTransform xform, final Set<SrlObject> xformed) {
        if (xformed.contains(this)) {
            return;
        }
        xformed.add(this);
        final Point2D point = xform.transform(new Point2D.Double(getX(), getY()),
                new Point2D.Double());
        setP(point.getX(), point.getY());
    }

    /**
     * Calculated the bounding box of the shape.
     * For a point all edges are identical.
     */
    protected final void calculateBBox() {
       setBoundingBox(new SrlRectangle(this, this));
    }

    /**
     * Called to calculate the convex hull of the object.
     */
    @Override protected void calculateConvexHull() {

    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.object.shape.SrlObject}.
     * This cloned object is only a shallow copy.
     * This copies all values and only the original location and the current location.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlPoint(this, false);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public SrlObject deepClone() {
        return new SrlPoint(this, true);
    }

    /**
     * Compare this point to another point based on time.
     * unless they have the same time then it is compared based on location (starting with X).
     * @param p point to compare to.
     * @return time difference between points.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public int compareTo(final SrlPoint p) {
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
     * In this case the same as equalsBy Content.
     * @param o The object this is being compared to.
     * @return true if the objects are equal.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public boolean equals(final Object o) {
        return o instanceof SrlObject && equalsByContent((SrlObject) o);
    }

    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public boolean equalsByContent(final SrlObject other) {
        if (!(other instanceof SrlPoint)) {
            return false;
        }
        final SrlPoint otherpoint = (SrlPoint) other;
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
     * Check if the x y t values match (probably the same initial point).
     *
     * @param p the point that the time is being compared to.
     * @return true if the points are equal
     */
    public final boolean equalsXYTime(final SrlPoint p) {
        return (p.getX() == getX() && p.getY() == getY() && p.getTime() == getTime());
    }

    /**
     * @return The x value for the first point in the history.
     */
    public final double getInitialX() {
        if (mXList.size() == 0) {
            return Double.NaN;
        }
        return mXList.get(0);
    }

    /**
     * @return The y value for the first point in the history.
     */
    public final double getInitialY() {
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
    public final double getMaxX() {
        return getX();
    }

    @Override
    /**
     * Just returns the y value
     * return y value
     */
    public final double getMaxY() {
        return getY();
    }

    @Override
    /**
     * Just returns the x value
     * return x value
     */
    public final double getMinX() {
        return getX();
    }

    @Override
    /**
     * Just returns the y value
     * return y value
     */
    public final double getMinY() {
        return getY();
    }

    /**
     * @return The first x location of the point as defined by the constructor.
     */
    public final double getOrigX() {
        return mXList.get(0);
    }

    /**
     * @return The first y location of the point as defined by the constructor.
     */
    public final double getOrigY() {
        return mYList.get(0);
    }

    /**
     * Points can have pressure depending on the input device.
     *
     * @return the pressure of the point
     */
    public final Double getPressure() {
        return mPressure;
    }

    /**
     * Sets the pressure of the point.
     *
     * @param pressure The pressure of the point.
     */
    public final void setPressure(final Double pressure) {
        mPressure = pressure;
    }

    /**
     * Get the tilt in the X direction.
     *
     * @return tilt in the X direction.
     */
    public final Double getTiltX() {
        return mTiltX;
    }

    /**
     * Set the tilt in the X direction.
     *
     * @param tiltX tilt in the X direction.
     */
    public final void setTiltX(final double tiltX) {
        mTiltX = tiltX;
    }

    /**
     * Get the tilt in the Y direction.
     *
     * @return tilt in the Y direction.
     */
    public final Double getTiltY() {
        return mTiltY;
    }

    /**
     * Set the tilt in the Y direction.
     *
     * @param tiltY tilt in the Y direction.
     */
    public final void setTiltY(final double tiltY) {
        mTiltY = tiltY;
    }

    /**
     * Get the current x value of the point.
     *
     * @return current x value of the point
     */
    public final double getX() {
        return mXList.get(mCurrentElement);
    }

    /**
     * We keep a history of the x values as the point is transformed.
     * This returns a clone of that history.
     *
     * @return the history of the x values
     */
    public final List<Double> getXList() {
        return new ArrayList<>(mXList);
    }

    /**
     * Get the current y value of the point.
     *
     * @return current y value of the point
     */
    public final double getY() {
        return mYList.get(mCurrentElement);
    }

    /**
     * We keep a history of the y values as the point is transformed.
     * This returns a clone of that history.
     *
     * @return the history of the y values
     */
    public final List<Double> getYList() {
        return new ArrayList<>(mYList);
    }

    /**
     * Get the original value of the point.
     *
     * @return a point where getx and gety return the first values that were added to the history
     */
    public final SrlPoint goBackToInitial() {
        if (mCurrentElement >= 0) {
            mCurrentElement = 0;
        }
        return this;
    }

    @SuppressWarnings("checkstyle:designforextension")
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
    public final void scale(final double x, final double y) {
        mXList.add(x * getX());
        mYList.add(y * getY());
        mCurrentElement = mXList.size() - 1;
    }

    /**
     * Delete the entire point history and
     * use these values as the starting point.
     *
     * @param x new initial x location
     * @param y new initial y location
     */
    public final void setOrigP(final double x, final double y) {
        mXList = new ArrayList<Double>();
        mYList = new ArrayList<Double>();
        setP(x, y);
    }

    /**
     * Updates the location of the point.
     * Also add this point to the history of the points
     * so this can be undone.
     *
     * @param x the new x location for the point
     * @param y the new y location for the point
     */
    public final void setP(final double x, final double y) {
        mXList.add(x);
        mYList.add(y);
        mCurrentElement = mXList.size() - 1;
    }

    /**
     * Set the pressure of the point.
     *
     * @param pressure pressure of the point.
     */
    public final void setPressure(final double pressure) {
        mPressure = pressure;
    }

    /**
     * Set the tilt of the point.
     *
     * @param tiltX tilt in the X direction.
     * @param tiltY tilt in the Y direction.
     */
    public final void setTilt(final double tiltX, final double tiltY) {
        setTiltX(tiltX);
        setTiltY(tiltY);
    }

    /**
     * Converts the Core Sketch Point object to the equivalent AWT Point.
     *
     * @return the Core Sketch-to-AWT converted point
     */
    public final java.awt.Point toAWTPoint() {

        final java.awt.Point awtPoint = new java.awt.Point(new Double(getX()).intValue(),
                new Double(getY()).intValue());

        return awtPoint;
    }

    /**
     * Returns a string of [x,y,time].
     *
     * @return a string of [x,t,time]
     */
    @SuppressWarnings("checkstyle:designforextension")
    public String toString() {
        return "[" + getX() + "," + getY() + "," + getTime() + "]";
    }

    /**
     * Translate the point in the amount x,y.
     * Saves the point it was before in case we need to undo the translation
     *
     * @param x amount to move in the x direction
     * @param y amount to move in the y direction
     */
    public final void translate(final double x, final double y) {
        mXList.add(x + getX());
        mYList.add(y + getY());
        mCurrentElement = mXList.size() - 1;
    }

    /**
     * Remove last point update.
     * If there is only one x,y value in the history,
     * then it does nothing
     * @return The updated shape (this).
     */
    public final SrlPoint undoLastChange() {
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
