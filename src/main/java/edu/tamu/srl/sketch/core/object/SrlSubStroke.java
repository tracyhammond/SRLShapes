package edu.tamu.srl.sketch.core.object;

/**
 * Created by gigemjt on 11/3/14.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 * <p/>
 * A substroke is defined as a portion of a complete stroke.
 */
public class SrlSubStroke extends SrlStroke {

    /**
     * This is the index of where the stroke starts in the complete stroke.
     */
    private int mStartIndex;
    /**
     * This is the index after the last point where the stroke ends in the complete stroke.
     * If this {@linkplain edu.tamu.srl.sketch.core.object.SrlSubStroke} ends where the complete stroke ends
     * then this value will be equal to the size of the list of points in the stroke.
     */
    private int mEndIndex;
    /**
     * The parent object that contains all of the points of this stroke.
     */
    private SrlStroke mParentStroke;

    /**
     * TODO comment.
     *
     * @return parent stroke.
     */
    public final SrlStroke getParentStroke() {
        return mParentStroke;
    }

    /**
     * TODO comment.
     *
     * @param parent parent stroke.
     */
    public final void setParentStroke(final SrlStroke parent) {
        this.mParentStroke = parent;
    }

    /**
     * @return the starting index of this sub stroke.
     */
    public final int getStartIndex() {
        return mStartIndex;
    }

    /**
     * @param startIndex sets the starting index of this stroke in the total number of points in the parent stroke.
     */
    public final void setStartIndex(final int startIndex) {
        this.mStartIndex = mStartIndex;
    }

    /**
     * @return the ending index of this sub stroke.
     */
    public final int getEndIndex() {
        return mEndIndex;
    }

    /**
     * @param endIndex sets the ending index of this stroke in the total number of points in the parent stroke.
     */
    public final void setEndIndex(final int endIndex) {
        this.mEndIndex = mEndIndex;
    }
}
