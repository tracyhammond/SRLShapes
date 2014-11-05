package edu.tamu.srl.sketch.core.abstracted;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * An abstract class for representing all objects that do not take up space
 * Virtual objects are useful but should not appear naturally on a sketch surface.
 * Virtual objects are not able to be recognized and should not be sent to a recognition system.
 */
public abstract class SrlVirtualObject extends SrlComponent {

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
     * @param o the object that is being copied.
     */
    public SrlVirtualObject(final SrlVirtualObject o) {
        super(o);
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param id   The unique identifier of the shape.
     */
    public SrlVirtualObject(final long time, final UUID id) {
        super(time, id);
    }
}
