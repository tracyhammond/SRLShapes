package edu.tamu.srl.sketch.core.virtual;

import edu.tamu.srl.sketch.core.abstracted.AbstractSrlComponent;
import edu.tamu.srl.sketch.core.abstracted.SrlVirtualObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigemjt on 11/3/14.
 *
 * <p/>
 * A bounding box is typically used to find the convex polygon that can encompass all of the points of an
 * {@link edu.tamu.srl.sketch.core.abstracted.SrlObject}.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
@SuppressWarnings({ "PMD.CloneMethodMustImplementCloneable", "PMD.AvoidDuplicateLiterals" })
public class SrlConvexHull extends SrlVirtualObject {
    /**
     * The list of points that make up the convex hull.
     */
    private List<SrlPoint> mPoints;

    /**
     * Default constructor.
     * <p/>
     * Creates an object with an id and a time.
     */
    public SrlConvexHull() {
        this.mPoints = new ArrayList<>();
    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     *
     * @param original the object that is being copied.
     */
    public SrlConvexHull(final SrlConvexHull original) {
        super(original);
        this.mPoints = original.mPoints;
    }

    /**
     * @return the list of points in the convex hull
     */
    public final List<SrlPoint> getPoints() {
        return mPoints;
    }

    /**
     * @param points sets the list of points.
     */
    public final void setPoints(final List<SrlPoint> points) {
        this.mPoints = points;
    }

    /**
     * Translate the object by the amount x,y.
     *
     * @param xOffset the amount in the x direction to move the object by.
     * @param yOffset the amount in the y direction to move the object by.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void translate(final double xOffset, final double yOffset) {
        for (SrlPoint p : mPoints) {
            p.translate(xOffset, yOffset);
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
        for (SrlPoint p : mPoints) {
            p.scale(xFactor, yFactor);
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
        for (SrlPoint p : mPoints) {
            p.rotate(radians, xCenter, yCenter);
        }
    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.sketch.core.abstracted.AbstractSrlComponent}.
     * This cloned object is only a shallow copy.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlConvexHull(this);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public AbstractSrlComponent deepClone() {
        return new SrlConvexHull(this);
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
    @Override public boolean deepEquals(final AbstractSrlComponent other) {
        throw new UnsupportedOperationException("need to implement this");
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
        throw new UnsupportedOperationException("need to implement this");
    }
}
