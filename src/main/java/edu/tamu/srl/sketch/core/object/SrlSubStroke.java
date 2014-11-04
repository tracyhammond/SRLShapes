package edu.tamu.srl.sketch.core.object;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * A substroke is defined as a portion of a complete stroke.
 */
public class SrlSubStroke extends SrlStroke {

    /**
     * This is the index of where the stroke starts in the complete stroke.
     */
    private int startIndex;

    /**
     * This is the index after the last point where the stroke ends in the complete stroke.
     * If this {@linkplain edu.tamu.srl.sketch.core.object.SrlSubStroke} ends where the complete stroke ends
     * then this value will be equal to the size of the list of points in the stroke.
     */
    private int endIndex;
}
