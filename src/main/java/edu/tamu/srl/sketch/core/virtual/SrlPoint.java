package edu.tamu.srl.sketch.core.virtual;

import edu.tamu.srl.sketch.core.abstracted.SrlComponent;
import edu.tamu.srl.sketch.core.abstracted.SrlVirtualObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 *
 * In addition to its x and y a point will also contains information about pressure and its x and y direction.
 * It also will contain a history of all of its transformations. That way you can always get the original point.
 *
 * In addition to being part of a {@link edu.tamu.srl.sketch.core.object.SrlStroke} this also can be used for general x,y referencing.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlPoint extends SrlVirtualObject {

    /**
     * A counter that keeps track of where you are in the history of points.
     */
    private int mCurrentElement = -1;

    /**
     * Points can have pressure depending on the input device.
     */
    private double mPressure;

    /**
     * Tilt in the X direction when the point was created.
     */
    private double mTiltX;

    /**
     * Tilt in the Y direction when the point was created.
     */
    private double mTiltY;

    /**
     * Holds an history list of the x points.
     * Purpose is so that we can redo and undo and go back to the original points.
     * Note that this means that we cannot just set one value, and not the other.
     */
    private List<Double> mXList = new ArrayList<Double>();

    /**
     * Holds a history list of the y points.
     * Purpose is so that we can redo and undo and go back to the original points
     * Note that this means that we cannot just set one value, and not the other.
     */
    private List<Double> mYList = new ArrayList<Double>();


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
        super(time, UUID.randomUUID());
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
     * @param x        x value of the point.
     * @param y        y value of the point.
     * @param time     time stamp.
     * @param id       point ID
     * @param tiltX    the pen tiltX of the point.
     * @param tiltY    the pen tiltY of the point.
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
     *
     * @param deep True if a deep copy is wanted otherwise a shallow copy is performed.
     * @param p    The point that is being copied.
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
     *
     * @param x The location in the x direction of the point.
     * @param y The location in the y direction of the point.
     */
    private void setPointDataFromConstructor(final double x, final double y) {
        setP(x, y);
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
     * Set the pressure of the point.
     *
     * @param pressure pressure of the point.
     */
    public final void setPressure(final double pressure) {
        mPressure = pressure;
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
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians the number of radians to rotate
     * @param xCenter the x-coordinate to rotate from
     * @param yCenter the y-coordinate to rotate from
     */
    @Override public final void rotate(final double radians, final double xCenter, final double yCenter) {
        // TODO rotation stuff...
        throw new UnsupportedOperationException("rotate is not supported");
    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.sketch.core.abstracted.SrlComponent}.
     * This cloned object is only a shallow copy.
     * This copies all values and only the original location and the current location out of the history
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlPoint(this, false);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public SrlComponent deepClone() {
        return new SrlPoint(this, true);
    }

    /**
     * Performs a shallow equals.
     *
     * This checks the x, y, tilt, pressure and time.
     * This does not check id.
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public boolean shallowEquals(final SrlComponent other) {
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
     * In addition to calling {@link #shallowEquals(edu.tamu.srl.sketch.core.abstracted.SrlComponent)}
     * this also compares the history and returns true if those are equal too.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean deepEquals(final SrlComponent other) {
        if (shallowEquals(other)) {
            return true;
        }
        return this.getXList().equals(((SrlPoint) other).getXList())
                && this.getYList().equals(((SrlPoint) other).getYList());
    }

    /**
     * Compare this point to another point based on time.
     * unless they have the same time then it is compared based on location (starting with X).
     *
     * @param p point to compare to.
     * @return time difference between points.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public int compareTo(final SrlComponent p) {
        if (!(p instanceof SrlPoint)) {
            return super.compareTo(p);
        }
        final int timeDiff = (int) (this.getTime() - p.getTime());
        if (timeDiff != 0) {
            return timeDiff;
        }

        final int xDiff = (int) (this.getX() - ((SrlPoint) p).getX());
        if (xDiff != 0) {
            return xDiff;
        }

        final int yDiff = (int) (this.getY() - ((SrlPoint) p).getY());
        if (yDiff != 0) {
            return yDiff;
        }

        return 0;
    }

    /**
     * Get a Point that corresponds to the center of this component. The
     * only thing that will be stored in this Point are X and Y values. Thus,
     * you should /only/ use the interface's getX() and getY() methods, and
     * should not cast the point to a concrete implementation. Time of the point
     * is set to 0.
     *
     * @return the Point with X and Y coordinates set to the center of this
     * bounding box.
     */
    @Override public final SrlPoint getCenterPoint() {
        return this;
    }

    /**
     * Scales the point by the amount x and y.
     * Having x and y specified allows one to change the
     * relative dimensions to match another shape if wanted.
     * Usually, these values will be the same to keep the
     * relative dimensions equal
     *
     * @param xFactor the amount to scale in the x direction
     * @param yFactor the amount to scale in the y direction
     */
    public final void scale(final double xFactor, final double yFactor) {
        mXList.add(xFactor * getX());
        mYList.add(yFactor * getY());
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
     *
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
