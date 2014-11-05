package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.SrlObject;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlAuthor;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlDevice;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlPen;
import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.List;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * A stroke is defined by pen/mouse/finger/etc down to pen/mouse/finger/etc up.
 * A stroke contains a list of {@link SrlPoint}.
 * The stroke also will contain data on the author of the stroke and the pen that made the stroke.
 */
public class SrlStroke extends SrlObject {

    /**
     * Holds the list of points contained within the stroke.
     */
    private List<SrlPoint> mPoints;

    /**
     * Author who drew the stroke.
     *
     * This value will never change even if it is made in combination of strokes made by other people.
     */
    private final SrlAuthor mAuthor;

    /**
     * Pen used to draw the stroke.
     *
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
    }

    /**
     * A copy constructor.
     *
     * Copies all values from the given object.
     *
     * @param o the object that is being copied.
     */
    public SrlStroke(final SrlStroke o) {
        super(o);
        this.mAuthor = o.getAuthor();
        this.mPen = o.getPen();
        this.mDevice = o.getDevice();
    }

    /**
     * Default constructor.
     * @param author Who made the stroke.
     * @param pen What made the stroke.
     * @param device where was the stroke made.
     */
    public SrlStroke(final SrlAuthor author, final SrlPen pen, final SrlDevice device) {
        super();
        this.mAuthor = author;
        this.mPen = pen;
        this.mDevice = device;
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param id   The unique identifier of the shape.
     * @param author Who made the stroke.
     * @param pen What made the stroke.
     * @param device where was the stroke made.
     */
    public SrlStroke(final long time, final UUID id, final SrlAuthor author, final SrlPen pen,
            final SrlDevice device) {
        super(time, id);
        this.mAuthor = author;
        this.mPen = pen;
        this.mDevice = device;
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time          The time the shape was originally created.
     * @param id            The unique identifier of the shape.
     * @param isUserCreated True if the user created the stroke instead of the computer.
     * @param author Who made the stroke.
     * @param pen What made the stroke.
     * @param device where was the stroke made.
     */
    public SrlStroke(final long time, final UUID id, final boolean isUserCreated,
            final SrlAuthor author, final SrlPen pen, final SrlDevice device) {
        super(time, id, isUserCreated);
        this.mAuthor = author;
        this.mPen = pen;
        this.mDevice = device;
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
