package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.SrlComponent;
import edu.tamu.srl.sketch.core.abstracted.SrlObject;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlAuthor;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlDevice;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlPen;
import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * <p>
 * A stroke is defined by pen/mouse/finger/etc down to pen/mouse/finger/etc up.
 * A stroke contains a list of {@link SrlPoint}.
 * The stroke also will contain data on the author of the stroke and the pen that made the stroke.
 * </p>
 *
 * <h4>Implementation Comments</h4>
 * All methods when interacting with the list (unless inserting into the list or removing from the list)
 * use the getPoints() method.  This is so that subclasses can have augmented versions of the list without
 * needing to overwrite every method or get passed a modifiable version of the list.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlStroke extends SrlObject {

    /**
     * Holds the list of points contained within the stroke.
     */
    private final List<SrlPoint> mPoints;

    /**
     * Author who drew the stroke.
     * <p/>
     * This value will never change even if it is made in combination of strokes made by other people.
     */
    private final SrlAuthor mAuthor;

    /**
     * Pen used to draw the stroke.
     * <p/>
     * The pen is a term used to define anything that made the stroke.
     * Because a single device can support multiple types of input he pen could be a computer mouse,
     * it could be a digital pen or it could be an actual finger.
     */
    private final SrlPen mPen;

    /**
     * Device the stroke was drawn on.
     */
    private final SrlDevice mDevice;

    /**
     * Default constructor.
     */
    public SrlStroke() {
        super();
        this.mAuthor = null;
        this.mPen = null;
        this.mDevice = null;
        mPoints = new ArrayList<>();
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time          The time the shape was originally created.
     * @param id            The unique identifier of the shape.
     * @param isUserCreated True if the user created the stroke instead of the computer.
     */
    public SrlStroke(final long time, final UUID id, final boolean isUserCreated) {
        super(time, id, isUserCreated);
        this.mAuthor = null;
        this.mPen = null;
        this.mDevice = null;
        mPoints = new ArrayList<>();
    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     * Performs a shallow copy.
     *
     * @param o the object that is being copied.
     */
    public SrlStroke(final SrlStroke o) {
        this(o, false);
    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     * Performs a shallow copy.
     *
     * @param o the object that is being copied.
     * @param deep true if a deep copy is being performed.  Otherwise a shallow copy is performed.
     */
    public SrlStroke(final SrlStroke o, final boolean deep) {
        super(o);
        this.mAuthor = o.getAuthor();
        this.mPen = o.getPen();
        this.mDevice = o.getDevice();
        this.mPoints = new ArrayList<>();
        if (deep) {
            final List<SrlPoint> cache = o.getPoints();
            for (int i = 0; i < cache.size(); i++) {
                this.addPoint((SrlPoint) cache.get(i).deepClone());
            }
        } else {
            // shallow copy
            this.addPoints(o.getPoints());
        }
    }

    /**
     * Default constructor.
     *
     * @param isUserCreated True if the user created the stroke instead of the computer.
     * @param author        Who made the stroke.
     * @param pen           What made the stroke.
     * @param device        where was the stroke made.
     */
    public SrlStroke(final boolean isUserCreated, final SrlAuthor author, final SrlPen pen, final SrlDevice device) {
        super();
        this.mAuthor = author;
        this.mPen = pen;
        this.mDevice = device;
        mPoints = new ArrayList<>();
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time   The time the shape was originally created.
     * @param id     The unique identifier of the shape.
     * @param author Who made the stroke.
     * @param pen    What made the stroke.
     * @param device where was the stroke made.
     */
    public SrlStroke(final long time, final UUID id, final SrlAuthor author, final SrlPen pen,
            final SrlDevice device) {
        super(time, id);
        this.mAuthor = author;
        this.mPen = pen;
        this.mDevice = device;
        mPoints = new ArrayList<>();
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time          The time the shape was originally created.
     * @param id            The unique identifier of the shape.
     * @param isUserCreated True if the user created the stroke instead of the computer.
     * @param author        Who made the stroke.
     * @param pen           What made the stroke.
     * @param device        where was the stroke made.
     */
    public SrlStroke(final long time, final UUID id, final boolean isUserCreated,
            final SrlAuthor author, final SrlPen pen, final SrlDevice device) {
        super(time, id, isUserCreated);
        this.mAuthor = author;
        this.mPen = pen;
        this.mDevice = device;
        mPoints = new ArrayList<>();
    }

    /**
     * Note that if this is called then we assume it is a resampled stroke so we say it is not user created.
     * It also has no author, device or pen.
     *
     * @param resampled a resampled list of points stored for the stroke.
     */
    public SrlStroke(final List<SrlPoint> resampled) {
        // not user created and it has no author or pen or device.
        this(false, null, null, null);
        for (SrlPoint p : resampled) {
            addPoint(p);
        }
    }

    /**
     * Translate the object by the amount x,y.
     *
     * @param x the amount in the x direction to move the object by.
     * @param y the amount in the y direction to move the object by.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void translate(final double x, final double y) {
        final List<SrlPoint> cache = getPoints();
        for (int i = 0; i < cache.size(); i++) {
            cache.get(i).translate(x, y);
        }
    }

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xFactor the x-factor
     * @param yFactor the y-factor
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void scale(final double xFactor, final double yFactor) {
        final List<SrlPoint> cache = getPoints();
        for (int i = 0; i < cache.size(); i++) {
            cache.get(i).scale(xFactor, yFactor);
        }
    }

    /**
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians the number of radians to rotate
     * @param xCenter the x-coordinate to rotate from
     * @param yCenter the y-coordinate to rotate from
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void rotate(final double radians, final double xCenter, final double yCenter) {
        final List<SrlPoint> cache = getPoints();
        for (int i = 0; i < cache.size(); i++) {
            cache.get(i).rotate(radians, xCenter, yCenter);
        }
    }

    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlStroke(this, false);
    }

    @SuppressWarnings("checkstyle:designforextension")
    @Override public SrlComponent deepClone() {
        return new SrlStroke(this, true);
    }

    /**
     * Performs a shallow equals.
     * By default this is called by the {@link #equals(Object)} method.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean shallowEquals(final SrlComponent other) {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * Looks deep into two components to check equality.
     * It would probably be smart to also have this call shallowEquals.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean deepEquals(final SrlComponent other) {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * Calculates the bounding box.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override protected void calculateBBox() {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * Called to calculate the convex hull of the object.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override protected void calculateConvexHull() {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * @return the largest X value. (larger x values are denoted as being on the right hand side of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMaxX() {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * @return the largest Y value. (larger Y values are denoted as being at the bottom the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMaxY() {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * @return the smallest X value. (smaller x values are denoted as being on the left hand side of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMinX() {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * @return the smallest Y value. (smaller Y values are denoted as being at the top of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMinY() {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * Adding another point to the stroke.
     *
     * @param point the point being added to the stroke.
     */
    public final void addPoint(final SrlPoint point) {
        mPoints.add(point);
        point.setName("p" + mPoints.size());
    }

    /**
     * Adds all of the points to the stroke.
     *
     * @param points points to add to the stroke
     */
    public final void addPoints(final List<SrlPoint> points) {
        mPoints.addAll(points);
    }

    /**
     * Clears the stroke of all points.
     */
    public final void clear() {
        mPoints.clear();
    }

    /**
     * @param point the point that is being removed.
     * @see List#remove(Object)
     */
    public final void remove(final SrlPoint point) {
        mPoints.remove(point);
    }

    /**
     * @param index the index of that point that is being removed.
     * @see List#remove(int)
     */
    public final void remove(final int index) {
        mPoints.remove(index);
    }

    /**
     * This creates an unmodifiableList of the points.
     * The list of points can only be modified by going through methods in the stroke itself.
     *
     * @return a list of points used by the stroke.
     * @see Collections#unmodifiableList
     */
    @SuppressWarnings("checkstyle:designforextension")
    public List<SrlPoint> getPoints() {
        return Collections.unmodifiableList(mPoints);
    }

    /**
     * Returns the first point in the stroke.
     * if the stroke has no points, it returns null.
     *
     * @return first point in the stroke
     */
    public final SrlPoint getFirstPoint() {
        if (getNumPoints() == 0) {
            throw new IllegalStateException("The list of points is empty.");
        }
        return getPoints().get(0);
    }

    /**
     * Returns the last point in the stroke
     * If the stroke has no points, it returns null.
     *
     * @return last point in the stroke.
     */
    public final SrlPoint getLastPoint() {
        if (getNumPoints() == 0) {
            throw new IllegalStateException("The list of points is empty.");
        }
        return getPoints().get(getNumPoints() - 1);
    }

    /**
     * Gets the number of points in the stroke.
     *
     * @return number of points in the stroke
     */
    public final int getNumPoints() {
        return getPoints().size();
    }

    /**
     * Get the i'th point in the stroke.
     * The first point has index i = 0
     *
     * For performance purposes in loops it is better to get the entire list and act on that instead of call this method.
     * @param i the index of the stroke
     * @return The point at index i.
     */
    public final SrlPoint getPoint(final int i) {
        if (i >= getNumPoints()) {
            throw new IndexOutOfBoundsException("index: " + i + "is greater than size " + getNumPoints());
        }
        return getPoints().get(i);
    }

    /**
     * @return Who made the stroke.
     */
    public final SrlAuthor getAuthor() {
        return mAuthor;
    }

    /**
     * @return What made the stroke.
     */
    public final SrlPen getPen() {
        return mPen;
    }

    /**
     * @return Where was the stroke made.
     */
    public final SrlDevice getDevice() {
        return mDevice;
    }
}
