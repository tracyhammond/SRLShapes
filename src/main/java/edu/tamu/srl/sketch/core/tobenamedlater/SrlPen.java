package edu.tamu.srl.sketch.core.tobenamedlater;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * <br>
 * Class containing information about a particular pen used to create a stroke.
 * A pen could be anything used to make a stroke.  It could be a keyboard or a mouse or even a finger.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 * @author gigemjt
 * @author bpaulson
 */
public class SrlPen {
    /**
     * Unique ID number for this pen
     */
    private UUID mId = UUID.randomUUID();

    /**
     * Manufacturer's pen ID number
     */
    private String mPenID = null;

    /**
     * Brand of the pen
     */
    private String mBrand = null;

    /**
     * Description of the pen
     */
    private String mDescription = null;

    /**
     * true if the pen is a finger
     */
    private boolean mPenIsFinger = false;

    /**
     * the digit of the pen that created the stroke.
     * For instance if a device supports multiple pens this will differentiate them even if the author is the same.
     * A more common example is if the user is using touch input and this will decide which finger it is.
     * <pre>
     *            __ __              __ __
     *           /  V  \ _ _    _ _ /  V  \
     *          |   |  |/ V \  / V \|  |   |
     *          |   |  |  |  ||  |  |  |   |
     *          |   |  |  |  ||  |  |  |   |
     *          |   |  |  |  ||  |  |  |   |
     *          |   |  |  |  ||  |  |  |   |
     *          |   |  |  |  ||  |  |  |   |
     * _____   |          |  ||  |          |   _____
     *|     ''-/             ||             \-''     |
     * \_                    ||                    _/
     *   ''-_     \          ||          /     _-''
     *       \     )         /\         (     /
     *        \             /  \             /
     *        |            |    |            |
     * </pre>
     */
    private int mPenDigit = -1;
}
