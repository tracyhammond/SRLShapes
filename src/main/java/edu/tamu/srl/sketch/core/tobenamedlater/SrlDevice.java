package edu.tamu.srl.sketch.core.tobenamedlater;

/**
 * Created by gigemjt on 11/3/14.
 * <br>
 * Class containing information about the device that produced a stroke.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
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
     * @param dpiX The number of pixels per inch in the x direction.
     * @param dpiY The number of pixels per inch in the Y direction.
     */
    public SrlDevice(final double dpiX, final double dpiY) {
        this.mDpiX = dpiX;
        this.mDpiY = dpiY;
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
