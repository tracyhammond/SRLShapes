package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.SrlObject;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlAuthor;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlPen;
import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.List;

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
     * The pen could be a computer mouse, it could be a digital pen or it could be an actual finger.
     */
    private final SrlPen mPen;
}
