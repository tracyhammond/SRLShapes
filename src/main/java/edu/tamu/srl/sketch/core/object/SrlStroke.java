package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.AbstractSrlComponent;
import edu.tamu.srl.sketch.core.abstracted.SrlObject;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlAuthor;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlDevice;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlPen;
import edu.tamu.srl.sketch.core.virtual.SrlBoundingBox;
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
 * <h1>Implementation Comments</h1>
 * All methods when interacting with the list (unless inserting into the list or removing from the list)
 * use the getPoints() method.  This is so that subclasses can have augmented versions of the list without
 * needing to overwrite every method or get passed a modifiable version of the list.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 * @author gigemjt
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.CloneMethodMustImplementCloneable", "PMD.AvoidDuplicateLiterals" })
public class SrlStroke extends SrlObject {

    /**
     * Holds the list of points contained within the stroke.
     */
    private final List<SrlPoint> mPoints;

    /**
     * Author who drew the stroke.
     * <br>
     * This value will never change even if it is made in combination of strokes made by other people.
     */
    private final SrlAuthor mAuthor;

    /**
     * Pen used to draw the stroke.
     * <br>
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
     * @param uuid          The unique identifier of the shape.
     * @param isUserCreated True if the user created the stroke instead of the computer.
     */
    public SrlStroke(final long time, final UUID uuid, final boolean isUserCreated) {
        super(time, uuid, isUserCreated);
        this.mAuthor = null;
        this.mPen = null;
        this.mDevice = null;
        mPoints = new ArrayList<>();
    }

    /**
     * A copy constructor.
     * <br>
     * Copies all values from the given object.
     * Performs a shallow copy.
     *
     * @param original the object that is being copied.
     */
    public SrlStroke(final SrlStroke original) {
        this(original, false);
    }

    /**
     * A copy constructor.
     * <br>
     * Copies all values from the given object.
     * Performs a shallow copy.
     *
     * @param original the object that is being copied.
     * @param deep     true if a deep copy is being performed.  Otherwise a shallow copy is performed.
     */
    public SrlStroke(final SrlStroke original, final boolean deep) {
        super(original);
        this.mAuthor = original.getAuthor();
        this.mPen = original.getPen();
        this.mDevice = original.getDevice();
        this.mPoints = new ArrayList<>();
        if (deep) {
            final List<SrlPoint> cache = original.getPoints();
            for (int i = 0; i < cache.size(); i++) {
                this.addPoint((SrlPoint) cache.get(i).deepClone());
            }
        } else {
            // shallow copy
            this.addPoints(original.getPoints());
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
        super(isUserCreated);
        this.mAuthor = author;
        this.mPen = pen;
        this.mDevice = device;
        mPoints = new ArrayList<>();
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time          The time the shape was originally created.
     * @param uuid          The unique identifier of the shape.
     * @param isUserCreated True if the user created the stroke instead of the computer.
     * @param author        Who made the stroke.
     * @param pen           What made the stroke.
     * @param device        where was the stroke made.
     */
    public SrlStroke(final long time, final UUID uuid, final boolean isUserCreated,
            final SrlAuthor author, final SrlPen pen, final SrlDevice device) {
        super(time, uuid, isUserCreated);
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
     * @param xOffset the amount in the x direction to move the object by.
     * @param yOffset the amount in the y direction to move the object by.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void translate(final double xOffset, final double yOffset) {
        final List<SrlPoint> cache = getPoints();
        for (int i = 0; i < cache.size(); i++) {
            cache.get(i).translate(xOffset, yOffset);
        }
        resetBounders();
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
        resetBounders();
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
        resetBounders();
    }

    /**
     * Gets the length of the path, that is, the sum of the Euclidean distances
     * between all pairs of consecutive points.
     *
     * @return the length of the stroke&#39;s path.
     */
    public final double getPathLength() {
        double res = 0.0;
        final List<SrlPoint> points = getPoints();
        for (int i = 1; i < points.size(); ++i) {
            res += points.get(i - 1).distanceToCenter(points.get(i));
        }
        return res;
    }

    /**
     * @return The average of all of the points in the shape.
     *
     * The time of this point actually contains the total number of points in this sub object.
     * This value can be grabbed with {@link SrlPoint#getTime()}.
     * Obviously this has a worst case of O(n) where n is the number of points in the shape.
     */
    @Override public final SrlPoint getAveragedPoint() {
        final int numPoints = this.getNumPoints();
        final List<SrlPoint> cache = getPoints();

        // use the averages together.
        double xAvg = 0;
        double yAvg = 0;
        for (int index = 0; index < numPoints; index++) {
            xAvg += cache.get(index).getX() / ((double) numPoints);
            yAvg += cache.get(index).getY() / ((double) numPoints);
        }
        return new SrlPoint(xAvg, yAvg, numPoints);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlStroke(this, false);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public AbstractSrlComponent deepClone() {
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
    @Override public boolean shallowEquals(final AbstractSrlComponent other) {
        return super.shallowEquals(other) && other instanceof SrlStroke
                && getNumPoints() == ((SrlStroke) other).getNumPoints();
    }

    /**
     * Looks deep into two components to check equality.
     * It would probably be smart to also have this call shallowEquals.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean deepEquals(final AbstractSrlComponent other) {
        if (!shallowEquals(other)) {
            return false;
        }
        // If the above is true then they are the same instance.
        final List<SrlPoint> cache = getPoints();
        boolean result = true;
        for (int index = 0; index < cache.size() && result; index++) {
            result &= cache.get(index).deepEquals(other);
        }
        return result;
    }

    /**
     * Attempts to find the closest distance to this other component.
     *
     * @param srlComponent
     *         the component we are trying to find this distance to.
     * @return the distance between the components.
     * <b>NOTE: due to possible heuristics used this method is not commutative.</b>  <pre>Meaning that:
     *
     * <code>shape1.distance(shape2) == shape2.distance(shape1);</code>
     * May be false.
     * </pre>
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double distance(final AbstractSrlComponent srlComponent) {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * Calculates the bounding box.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override protected void calculateBBox() {
        double maxX = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        final List<SrlPoint> cache = getPoints();
        // loops are faster on android with length predefined.
        final int length = cache.size();
        for (int i = 0; i < length; i++) {
            final SrlPoint point = cache.get(i);
            maxX = Math.max(point.getX(), maxX);
            maxY = Math.max(point.getY(), maxY);

            minX = Math.min(point.getX(), minX);
            minY = Math.min(point.getY(), minY);
        }
        this.setBoundingBox(new SrlBoundingBox(minX, minY, maxX, maxY));
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
    @Override public final double getMaxX() {
        double max = Double.NEGATIVE_INFINITY;
        final List<SrlPoint> shapeList = getPoints();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlPoint shape = shapeList.get(index);
            max = Math.max(max, shape.getX());
        }
        return max;
    }

    /**
     * @return the largest Y value. (larger Y values are denoted as being at the bottom the screen.
     */
    @Override public final double getMaxY() {
        double max = Double.NEGATIVE_INFINITY;
        final List<SrlPoint> shapeList = getPoints();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlPoint shape = shapeList.get(index);
            max = Math.max(max, shape.getY());
        }
        return max;
    }

    /**
     * @return the smallest X value. (smaller x values are denoted as being on the left hand side of the screen.
     */
    @Override public final double getMinX() {
        double min = Double.POSITIVE_INFINITY;
        final List<SrlPoint> shapeList = getPoints();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlPoint shape = shapeList.get(index);
            min = Math.min(min, shape.getX());
        }
        return min;
    }

    /**
     * @return the smallest Y value. (smaller Y values are denoted as being at the top of the screen.
     */
    @Override public final double getMinY() {
        double min = Double.POSITIVE_INFINITY;
        final List<SrlPoint> shapeList = getPoints();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlPoint shape = shapeList.get(index);
            min = Math.min(min, shape.getY());
        }
        return min;
    }

    /**
     * Adds a subObject to this object at the specified index.
     *
     * @param index        point to add the content
     * @param point the sub object.
     */
    public final void addPoint(final int index, final SrlPoint point) {
        mPoints.add(index, point);
        resetBounders();
    }

    /**
     * Adding another point to the stroke.
     *
     * @param point the point being added to the stroke.
     */
    public final void addPoint(final SrlPoint point) {
        mPoints.add(point);
        point.setName("p" + mPoints.size());
        resetBounders();
    }

    /**
     * Adds all of the points to the stroke.
     *
     * @param points points to add to the stroke
     */
    public final void addPoints(final List<? extends SrlPoint> points) {
        mPoints.addAll(points);
        resetBounders();
    }

    /**
     * Checks if this container contains the target point.
     *
     * @param point the target point
     * @return true if this container contains the target point, false
     * otherwise
     */
    public final boolean contains(final SrlPoint point) {
        return getPoints().contains(point);
    }

    /**
     * Checks if this container contains any of the given points.
     *
     * @param points the target points
     * @return true if this container contains any of the target points,
     * false otherwise.
     */
    public final boolean containsAny(final List<? extends SrlPoint> points) {
        final int size = points.size();
        for (int i = 0; i < size; i++) {
            final SrlPoint point = points.get(i);
            if (contains(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the stroke of all points.
     */
    public final void clear() {
        mPoints.clear();
        resetBounders();
    }

    /**
     * Removes a subObject from this container.
     *
     * @param subObject subObject to remove
     * @return true if something was removed
     */
    public final boolean remove(final SrlPoint subObject) {
        final boolean result = mPoints.remove(subObject);
        if (result) {
            resetBounders();
        }
        return result;
    }

    /**
     * @param index the index of that point that is being removed.
     * @return the value removed.
     * @see List#remove(int)
     */
    public final SrlPoint remove(final int index) {
        final SrlPoint obj = mPoints.remove(index);
        resetBounders();
        return obj;
    }

    /**
     * This creates an unmodifiableList of the points.
     * The list of points can only be modified by going through methods in the stroke itself.
     *
     * @return a list of points used by the stroke.  <b>This should never return null.</b>
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
     * Get the index'th point in the stroke.
     * The first point has index index = 0
     * <br>
     * For performance purposes in loops it is better to get the entire list and act on that instead of call this method.
     *
     * @param index the index of the stroke
     * @return The point at index index.
     */
    public final SrlPoint getPoint(final int index) {
        if (index >= getNumPoints()) {
            throw new IndexOutOfBoundsException("index: " + index + "is greater than size " + getNumPoints());
        }
        return getPoints().get(index);
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
