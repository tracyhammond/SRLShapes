package edu.tamu.srl.sketch.core.abstracted;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * Oversees all javaobjects and virtual objects and centralizes all code used by everything.
 *
 */
public abstract class SrlComponent {

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
     * @param name sets the components name this can be useful for debugging. Example "triangle1".
     */
    public void setName(final String name) {
        this.mName = mName;
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
     * Translate the object by the amount x,y.
     *
     * @param x the amount in the x direction to move the object by.
     * @param y the amount in the y direction to move the object by.
     */
    public abstract void translate(final double x, final double y);


    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xfactor the x-factor
     * @param yfactor the y-factor
     */
    @SuppressWarnings("checkstyle:designforextension")
    public abstract void scale(final double xfactor, final double yfactor);

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
}
