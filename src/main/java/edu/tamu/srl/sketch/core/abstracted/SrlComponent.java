package edu.tamu.srl.sketch.core.abstracted;

import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.Comparator;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 * <p/>
 * Oversees all javaobjects and virtual objects and centralizes all code used by everything.
 */
public abstract class SrlComponent implements Comparable<SrlComponent> {

    /**
     * Default comparator using time.
     */
    private static Comparator<SrlComponent> timeComparator;

    /**
     * Each object has a unique ID associated with it.
     */
    private final UUID mId;

    /**
     * The creation time of the object.
     */
    private final long mTime;

    /**
     * The name of the object, such as "triangle1" or stroke1.
     */
    private String mName = "";

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param id   The unique identifier of the shape.
     */
    public SrlComponent(final long time, final UUID id) {
        this.mId = id;
        this.mTime = time;
    }

    /**
     * Default constructor.
     * <p/>
     * Creates an object with an id and a time.
     */
    public SrlComponent() {
        mId = UUID.randomUUID();
        mTime = System.currentTimeMillis();
    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     *
     * @param o the object that is being copied.
     */
    public SrlComponent(final SrlComponent o) {
        this.mTime = o.getTime();
        this.mId = o.getId();
        this.mName = o.getName();
    }

    /**
     * Gets the comparator for the time values. Compares two objects based on
     * their time stamps. Might be better to compare based on another method,
     * but haven't decided yet what that is.
     * <p/>
     * The Comparator will return 0 if the times are equal.
     *
     * @return the comparator for the time values
     */
    public static Comparator<SrlComponent> getTimeComparator() {

        if (timeComparator == null) {
            timeComparator = new Comparator<SrlComponent>() {
                @Override
                public int compare(final SrlComponent arg0, final SrlComponent arg1) {
                    return (arg0.getTime() < arg1.getTime()) ? -1 : (arg0
                            .getTime() > arg1.getTime()) ? 1 : 0;
                }
            };
        }
        return timeComparator;
    }

    /**
     * @return unique UUID for an object
     */
    public final UUID getId() {
        return mId;
    }

    /**
     * Gets the time associated with the object. The default time is the time it
     * was created.
     *
     * @return the time the object was created.
     */
    public final long getTime() {
        return mTime;
    }

    /**
     * An object can have a name, such as "triangle1".
     *
     * @return the string name of the object.
     */
    public final String getName() {
        return mName;
    }

    /**
     * @param name sets the components name this can be useful for debugging. Example "triangle1".
     */
    public final void setName(final String name) {
        this.mName = mName;
    }

    /**
     * Translate the object by the amount x,y.
     *
     * @param x the amount in the x direction to move the object by.
     * @param y the amount in the y direction to move the object by.
     */
    public abstract void translate(final double x, final double y);

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xFactor the x-factor
     * @param yFactor the y-factor
     */
    public abstract void scale(final double xFactor, final double yFactor);

    /**
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians the number of radians to rotate
     * @param xCenter the x-coordinate to rotate from
     * @param yCenter the y-coordinate to rotate from
     */
    public abstract void rotate(final double radians, final double xCenter, final double yCenter);

    /**
     * Rotates the SrlObject from the origin.
     *
     * @param radians the number of radians to rotate
     */
    public final void rotate(final double radians) {
        rotate(radians, 0, 0);
    }

    /**
     * @return A cloned object that is an instance of {@link SrlComponent}.  This cloned object is only a shallow copy.
     */
    @Override
    public abstract Object clone();

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    public abstract SrlComponent deepClone();

    /**
     * Default uses time. You can also use x or y to compare.
     *
     * @param o The object that this instance is being compared to.
     * @return a value that depends on the result of the comparator used.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public int compareTo(final SrlComponent o) {
        return getTimeComparator().compare(this, o);
    }

    /**
     * Checks if two {@link SrlComponent} are equal.
     * This default implementation only checks that the two ids equal after checking if
     * {@link #shallowEquals(SrlComponent)} returns true.
     *
     * @param other the other object.
     * @return by default. true if the two ids are equal.
     */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public boolean equals(final Object other) {
        if (!(other instanceof SrlComponent)) {
            return false;
        }
        return shallowEquals((SrlComponent) other) && getId().equals(((SrlComponent) other).getId());
    }

    /**
     * Performs a shallow equals.
     * By default this is called by the {@link #equals(Object)} method.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    public abstract boolean shallowEquals(SrlComponent other);

    /**
     * Looks deep into two components to check equality.
     * It would probably be smart to also have this call shallowEquals.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    public abstract boolean deepEquals(SrlComponent other);

    /**
     * @return default hash code just creates a hash code of its UUID.
     * This default is good enough in a majority of cases as UUID should always be unique.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public int hashCode() {
        return getId().hashCode();
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
    public abstract SrlPoint getCenterPoint();

    /**
     * Return the distance from the point specified by (x,y) to the center of this object.
     *
     * @param x the x value of the other point
     * @param y the y value of the other point
     * @return the distance
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double distanceToCenter(final double x, final double y) {
        final SrlPoint center = getCenterPoint();
        final double xdiff = x - center.getX();
        final double ydiff = y - center.getY();
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }

    /**
     * Return the distance from point rp to this point.
     *
     * @param o the other point
     * @return the distance
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double distanceToCenter(final SrlComponent o) {
        final SrlPoint other = o.getCenterPoint();
        return distanceToCenter(other.getX(), other.getY());
    }

}
