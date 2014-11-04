package edu.tamu.srl.sketch.core.virtual;

import edu.tamu.srl.sketch.core.abstracted.SrlVirtualObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * In addition to its x and y a point will also contains information about pressure and its x and y direction.
 * It also will contain a history of all of its transformations. That way you can always get the original point.
 *
 * In addition to being part of a {@link edu.tamu.srl.sketch.core.object.SrlStroke} this also can be used for general x,y referencing.
 */
public class SrlPoint extends SrlVirtualObject {

    /**
     * A counter that keeps track of where you are in the history of points.
     */
    private int mCurrentElement = -1;

    /**
     * Points can have pressure depending on the input device.
     */
    private double mPressure;

    /**
     * Tilt in the X direction when the point was created.
     */
    private double mTiltX;

    /**
     * Tilt in the Y direction when the point was created.
     */
    private double mTiltY;

    /**
     * Holds an history list of the x points.
     * Purpose is so that we can redo and undo and go back to the original points.
     * Note that this means that we cannot just set one value, and not the other.
     */
    private List<Double> mXList = new ArrayList<Double>();

    /**
     * Holds a history list of the y points.
     * Purpose is so that we can redo and undo and go back to the original points
     * Note that this means that we cannot just set one value, and not the other.
     */
    private List<Double> mYList = new ArrayList<Double>();
}
