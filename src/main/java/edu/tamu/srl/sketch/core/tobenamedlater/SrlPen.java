package edu.tamu.srl.sketch.core.tobenamedlater;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * <br>
 * Class containing information about a particular pen used to create a stroke.
 * A pen could be anything used to make a stroke.  It could be a keyboard or a mouse or even a finger.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 *
 * @author gigemjt
 * @author bpaulson
 */
public class SrlPen {

    /**
     * Unique ID number for this pen.
     */
    private UUID mId = UUID.randomUUID();
    /**
     * Manufacturer's pen ID number.
     */
    private String mPenId = null;
    /**
     * Brand of the pen.
     * (if pen is a finger or a mouse then this should be null).
     */
    private String mBrand = null;
    /**
     * Description of the pen.
     */
    private String mDescription = null;
    /**
     * True if the pen is a finger.
     */
    private boolean mIsPenFinger = false;
    /**
     * True if the pen is a mouse.
     */
    private boolean mIsPenMouse = false;
    /**
     * the digit of the pen that created the stroke.
     * For instance if a device supports multiple pens this will differentiate them even if the author is the same.
     * A more common example is if the user is using touch input and this will decide which finger it is.
     * Typically the digit is the first digit that is touched and the second one is the second digit on the screen.
     * also typically the first digit is the index finger.  The second digit is the middle finger.
     * Note: this is not perfect but instead is just a guess about how it should work.
     * (we have numbered them below for each hand separately as if they belong to two different people).
     * <pre>
     *            __ __              __ __
     *           /  V  \ _ _    _ _ /  V  \
     *          |   |  |/ V \  / V \|  |   |
     *          |   |  |  |  ||  |  |  |   |
     *          | 4 |3 |2 |1 || 1| 2| 3| 4 |
     *          |   |  |  |  ||  |  |  |   |
     *          |   |  |  |  ||  |  |  |   |
     *          |   |  |  |  ||  |  |  |   |
     * _____   |          |  ||  |          |   _____
     * |  5  ''-/             ||             \-''  5  |
     * \_                    ||                    _/
     *   ''-_     \          ||          /     _-''
     *       \     )         /\         (     /
     *        \             /  \             /
     *        |            |    |            |
     * </pre>
     * (<a href="https://gist.github.com/sublee/600592/">code to create ascii hand</a>)
     */
    private int mPenDigit = -1;

    /**
     * @param uuid
     *         Unique ID number for this pen.
     * @param penId
     *         Manufacturer's pen ID number.
     * @param penBrand
     *         Brand of the pen.
     * @param description
     *         Description of the pen.
     * @param penDigit
     *         {@link #mPenDigit}.
     * @param isPenFinger
     *         True if the pen is a finger.
     * @param isPenMouse
     *         True if the pen is a mouse.
     */
    public SrlPen(final UUID uuid, final String penId, final String penBrand, final String description,
            final int penDigit, final boolean isPenFinger, final boolean isPenMouse) {
        this.mId = uuid;
        this.mPenId = penId;
        this.mBrand = penBrand;
        this.mDescription = description;
        this.mIsPenFinger = isPenFinger;
        this.mPenDigit = penDigit;
        this.mIsPenMouse = isPenMouse;
    }

    /**
     * @return Unique ID number for this pen.
     */
    public final UUID getId() {
        return mId;
    }

    /**
     * @param uuid
     *         Unique ID number for this pen.
     */
    public final void setId(final UUID uuid) {
        this.mId = uuid;
    }

    /**
     * @return Manufacturer's pen ID number.
     */
    public final String getPenId() {
        return mPenId;
    }

    /**
     * @param penId
     *         Manufacturer's pen ID number.
     */
    public final void setPenId(final String penId) {
        this.mPenId = penId;
    }

    /**
     * @return Brand of the pen.
     */
    public final String getBrand() {
        return mBrand;
    }

    /**
     * @param penBrand
     *         Brand of the pen.
     */
    public final void setBrand(final String penBrand) {
        this.mBrand = penBrand;
    }

    /**
     * @return Description of the pen.
     */
    public final String getDescription() {
        return mDescription;
    }

    /**
     * @param description
     *         Description of the pen.
     */
    public final void setDescription(final String description) {
        this.mDescription = description;
    }

    /**
     * @return True if the pen is a finger.
     */
    public final boolean isPenIsFinger() {
        return mIsPenFinger;
    }

    /**
     * @param isPenFinger
     *         True if the pen is a finger.
     */
    public final void setPenIsFinger(final boolean isPenFinger) {
        this.mIsPenFinger = isPenFinger;
    }

    /**
     * @return True if the pen is a mouse.
     */
    public final boolean isPenIsMouse() {
        return mIsPenMouse;
    }

    /**
     * @param isPenMouse
     *         True if the pen is a mouse.
     */
    public final void setPenIsMouse(final boolean isPenMouse) {
        this.mIsPenMouse = isPenMouse;
    }

    /**
     * @return {@link #mPenDigit}.
     */
    public final int getPenDigit() {
        return mPenDigit;
    }

    /**
     * @param penDigit
     *         {@link #mPenDigit}.
     */
    public final void setPenDigit(final int penDigit) {
        this.mPenDigit = penDigit;
    }
}
