package edu.tamu.srl.sketch.core.abstracted;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 *
 * An abstract class for representing all objects that do not take up space
 * Virtual objects are useful but should not appear naturally on a sketch surface.
 * Virtual objects are not able to be recognized and should not be sent to a recognition system.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
@SuppressWarnings("PMD.AbstractNaming")
public abstract class SrlVirtualObject extends AbstractSrlComponent {

    /**
     * Default constructor.
     * <p/>
     * Creates an object with an id and a time.
     */
    public SrlVirtualObject() {
        super();
    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     *
     * @param original the object that is being copied.
     */
    public SrlVirtualObject(final SrlVirtualObject original) {
        super(original);
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param uuid   The unique identifier of the shape.
     */
    public SrlVirtualObject(final long time, final UUID uuid) {
        super(time, uuid);
    }
}
