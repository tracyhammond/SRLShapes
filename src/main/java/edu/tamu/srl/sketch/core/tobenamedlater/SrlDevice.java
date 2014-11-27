package edu.tamu.srl.sketch.core.tobenamedlater;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * <br>
 * Class containing information about the device that produced a stroke.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 *
 * @author gigemjt
 */
public class SrlDevice {

    /**
     * Dots per inch (DPI) in the X axis.
     */
    private final double mDpiX;
    /**
     * Dots per inch (DPI) in the Y axis.
     */
    private final double mDpiY;
    /**
     * Unique ID number for this device.
     * The same Id for a sketch should persist for the entire sketch.
     */
    private UUID mId = UUID.randomUUID();
    /**
     * true if the device supports pen.
     */
    private boolean mPenEnabled = false;
    /**
     * true if the device supports touch.
     */
    private boolean mTouchEnabled = false;
    /**
     * true if the device supports mouse input.
     */
    private boolean mMouseEnabled = false;
    /**
     * true if the device supports a physical keyboard.
     */
    private boolean mPhysicalKeyboard = false;

    /**
     * @param dpiX
     *         The number of pixels per inch in the x direction.
     * @param dpiY
     *         The number of pixels per inch in the Y direction.
     * @param deviceId
     *         The deviceId of the device.
     */
    public SrlDevice(final UUID deviceId, final double dpiX, final double dpiY) {
        this.mDpiX = dpiX;
        this.mDpiY = dpiY;
        this.mId = deviceId;
    }

    /**
     * @return the id of the device.  For a single sketch and a single device this id should be consistent.
     * This is id does not need to consistent for a single device across multiple sketches.
     */
    public final UUID getId() {
        return mId;
    }

    /**
     * @return true if the pen is enabled.
     */
    public final boolean isPenEnabled() {
        return mPenEnabled;
    }

    /**
     * @param penEnabled
     *         true if the pen is enabled
     */
    public final void setPenEnabled(final boolean penEnabled) {
        this.mPenEnabled = penEnabled;
    }

    /**
     * @return True if touch is enabled.
     */
    public final boolean isTouchEnabled() {
        return mTouchEnabled;
    }

    /**
     * @param touchEnabled
     *         True if touch is enabled.
     */
    public final void setTouchEnabled(final boolean touchEnabled) {
        this.mTouchEnabled = touchEnabled;
    }

    /**
     * @return True if mouse input is enabled.
     */
    public final boolean isMouseEnabled() {
        return mMouseEnabled;
    }

    /**
     * @param mouseEnabled
     *         True if mouse input is enabled.
     */
    public final void setMouseEnabled(final boolean mouseEnabled) {
        this.mMouseEnabled = mouseEnabled;
    }

    /**
     * @return True if it is possible to use a physical keyboard.
     */
    public final boolean isPhysicalKeyboard() {
        return mPhysicalKeyboard;
    }

    /**
     * @param physicalKeyboard
     *         True if it is possible to use a physical keyboard.
     */
    public final void setPhysicalKeyboard(final boolean physicalKeyboard) {
        this.mPhysicalKeyboard = physicalKeyboard;
    }

    /**
     * @return The number of pixels per inch in the Y direction.
     */
    public final double getDpiY() {
        return mDpiY;
    }

    /**
     * @return The number of pixels per inch in the X direction.
     */
    public final double getDpiX() {
        return mDpiX;
    }
}
