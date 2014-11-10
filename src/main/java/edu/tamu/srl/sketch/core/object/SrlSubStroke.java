package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.List;

/**
 * Created by gigemjt on 11/3/14.
 *
 * <p/>
 * A substroke is defined as a portion of a complete stroke.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
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
     * The ending index works exactly the same way the ending index in {@link String#substring(int, int)} works.
     */
    private int mEndIndex;

    /**
     * The parent object that contains all of the points of this stroke.
     */
    private SrlStroke mParentStroke;

    /**
     * Returns the object representing the parent stroke.
     * The parent stroke contains all of the points that this {@link edu.tamu.srl.sketch.core.object.SrlSubStroke} was derived from.
     * The returned object may not actually exist in the sketch.
     * @return parent stroke.
     */
    public final SrlStroke getParentStroke() {
        return mParentStroke;
    }

    /**
     * @param endIndex The index after the last point you want in the substroke.
     * @param startIndex The first point in the substroke.
     * @param parent The parent that contains all of the points of the substroke.
     */
    public SrlSubStroke(final SrlStroke parent, final int startIndex, final int endIndex) {
        if (parent == null) {
            throw new IllegalArgumentException("The parent stroke must not be null");
        }
        if (startIndex < 0) {
            throw new IllegalArgumentException("The starting index must be greater than zero");
        }
        if (endIndex > parent.getNumPoints()) {
            throw new IllegalArgumentException("The ending index can not be larger then the length of the parent stroke");
        }
        if (startIndex > endIndex) {
            throw new IllegalArgumentException("The starting index must be smaller than the ending index");
        }
        this.mParentStroke = parent;
        this.mEndIndex = endIndex;
        this.mStartIndex = startIndex;
    }

    @Override
    public final List<SrlPoint> getPoints() {
        return mParentStroke.getPoints().subList(mStartIndex, mEndIndex);
    }

    /**
     * Sets the parent substroke.
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
        this.mStartIndex = startIndex;
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
        this.mEndIndex = endIndex;
    }
}
