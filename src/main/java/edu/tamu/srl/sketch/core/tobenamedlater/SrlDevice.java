package edu.tamu.srl.sketch.core.tobenamedlater;

/**
 * Created by gigemjt on 11/3/14.
 * <p/>
 * Class containing information about the device that produced a stroke.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlDevice {

    /**
     * Dots per inch (DPI) in the X axis
     */
    private final double mDpiX;
    /**
     * Dots per inch (DPI) in the Y axis
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
    public double getDpiY() {
        return mDpiY;
    }

    /**
     * @return The number of pixels per inch in the X direction.
     */
    public double getDpiX() {
        return mDpiX;
    }
}
