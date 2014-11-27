package edu.tamu.srl.sketch.core.abstracted;

import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.Comparator;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 *
 * <br>
 * Oversees all javaobjects and virtual objects and centralizes all code used by everything.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 * @author gigemjt
 *
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals" })
public abstract class AbstractSrlComponent implements Comparable<AbstractSrlComponent>, Cloneable {

    /**
     * Default comparator using time.
     */
    private static volatile Comparator<AbstractSrlComponent> timeComparator;

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
     * @param uId   The unique identifier of the shape.
     */
    public AbstractSrlComponent(final long time, final UUID uId) {
        this.mId = uId;
        this.mTime = time;
    }

    /**
     * Default constructor.
     *
     * Creates an object with an id and a time.
     */
    public AbstractSrlComponent() {
        mId = UUID.randomUUID();
        mTime = System.currentTimeMillis();
    }

    /**
     * A copy constructor.
     *
     * Copies all values from the given object.
     *
     * @param original the object that is being copied.
     */
    public AbstractSrlComponent(final AbstractSrlComponent original) {
        this.mTime = original.getTime();
        this.mId = original.getId();
        this.mName = original.getName();
    }

    /**
     * Gets the comparator for the time values. Compares two objects based on
     * their time stamps. Might be better to compare based on another method,
     * but haven't decided yet what that is.
     *
     * The Comparator will return 0 if the times are equal.
     *
     * @return the comparator for the time values
     */
    @SuppressWarnings("checkstyle:innerassignment")
    public static Comparator<AbstractSrlComponent> getTimeComparator() {
        Comparator<AbstractSrlComponent> result = timeComparator;
        // Double checked locking.
        if (result == null) {
            synchronized (AbstractSrlComponent.class) {
                result = timeComparator;
                if (result == null) {
                    timeComparator = result = timeComparator = new Comparator<AbstractSrlComponent>() {
                        @SuppressWarnings({ "PMD.CommentRequired", "PMD.UselessParentheses" })
                        @Override
                        public int compare(final AbstractSrlComponent arg0, final AbstractSrlComponent arg1) {
                            return (arg0.getTime() < arg1.getTime()) ? -1 : (arg0
                                    .getTime() > arg1.getTime()) ? 1 : 0;
                        }
                    };
                }
            }
        }
        return result;
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
        this.mName = name;
    }

    /**
     * Translate the object by the amount xOffset,yOffset.
     *
     * @param xOffset the amount in the xOffset direction to move the object by.
     * @param yOffset the amount in the yOffset direction to move the object by.
     */
    public abstract void translate(final double xOffset, final double yOffset);

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
     * @return A cloned object that is an instance of {@link AbstractSrlComponent}.  This cloned object is only a shallow copy.
     */
    @Override
    public abstract Object clone();

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    public abstract AbstractSrlComponent deepClone();

    /**
     * Default uses time. You can also use x or y to compare.
     *
     * @param srlComponent The object that this instance is being compared to.
     * @return a value that depends on the result of the comparator used.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public int compareTo(final AbstractSrlComponent srlComponent) {
        return getTimeComparator().compare(this, srlComponent);
    }

    /**
     * Checks if two {@link AbstractSrlComponent} are equal.
     * This default implementation only checks that the two ids equal after checking if
     * {@link #shallowEquals(AbstractSrlComponent)} returns true.
     *
     * @param other the other object.
     * @return by default. true if the two ids are equal.
     */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public boolean equals(final Object other) {
        if (!(other instanceof AbstractSrlComponent)) {
            return false;
        }
        return shallowEquals((AbstractSrlComponent) other) && getId().equals(((AbstractSrlComponent) other).getId());
    }

    /**
     * Performs a shallow equals.
     * By default this is called by the {@link #equals(Object)} method.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    public abstract boolean shallowEquals(AbstractSrlComponent other);

    /**
     * Looks deep into two components to check equality.
     * It would probably be smart to also have this call shallowEquals.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    public abstract boolean deepEquals(AbstractSrlComponent other);

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
     * Return the distance from the point specified by (otherX,otherY) to the center of this object.
     *
     * @param otherX the otherX value of the other point
     * @param otherY the otherY value of the other point
     * @return the distance
     */
    public final double distanceToCenter(final double otherX, final double otherY) {
        final SrlPoint center = getCenterPoint();
        final double xDiff = otherX - center.getX();
        final double yDiff = otherY - center.getY();
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * Return the distance from point rp to this point.
     *
     * @param srlComponent the other point
     * @return the distance
     */
    @SuppressWarnings("checkstyle:designforextension")
    public double distanceToCenter(final AbstractSrlComponent srlComponent) {
        final SrlPoint other = srlComponent.getCenterPoint();
        return distanceToCenter(other.getX(), other.getY());
    }

    /**
     * Attempts to find the closest distance to this other component.
     * @param srlComponent the component we are trying to find this distance to.
     * @return the distance between the components.
     * <b>NOTE: due to possible heuristics used this method is not commutative.  Meaning that:
     *
     * <code>shape1.distance(shape2) == shape2.distance(shape1);</code>
     * May be false
     *</b>
     */
    public abstract double distance(final AbstractSrlComponent srlComponent);

    /**
     * Attempts to return the closest distance between two shapes.
     * This is an approximation and may not be the absolute closest distance.
     * @param srlComponent1
     * @param srlComponent2
     * @return
     */
    public static final double findShortestDistance(final AbstractSrlComponent srlComponent1, final AbstractSrlComponent srlComponent2) {
        final double distance1 = srlComponent1.distance(srlComponent2);
        final double distance2 = srlComponent2.distance(srlComponent1);
        return (distance1 + distance2) / 2.0;
    }
}
