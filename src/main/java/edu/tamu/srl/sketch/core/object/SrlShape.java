package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.SrlObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * A shape is anything that can be composed one or more subOjects. ({@link edu.tamu.srl.sketch.core.abstracted.SrlObject}).
 * A shape is assumed to have been recognized and may contain information pertaining to recognition.
 */
public class SrlShape extends SrlObject {

    /**
     * Was this object made up from a collection of sub{@link edu.tamu.srl.sketch.core.abstracted.SrlObject}s?
     * If so they are in this list.
     * This list usually gets filled in through recognition.
     * This list can be examined hierarchically.
     * e.g., an arrow might have three lines inside, and each line might have a stroke.
     */
    private ArrayList<SrlObject> mSubShapes = new ArrayList<SrlObject>();

    /**
     * This is the id of the interpretation and should be unique across all interpretations.
     * This value should not be generated at runtime but instead should be linked to a specific interpretation of a certain recognizer
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     *
     * This id may not be unique across multiple recognizers.
     * This id in combination with the {@link #mRecognizerId} is guaranteed to be unique.
     */
    private final UUID mInterpretationId;

    /**
     * This is the id of the recognizer and should be unique across all recognizer.
     * This value should not be generated at runtime but instead should be linked to a specific recognizer.
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     *
     * Each recognizer may have multiple interpretations so this id may not be unique across multiple interpretations.
     * This id in combination with the {@link #mInterpretationId} is guaranteed to be unique.
     */
    private final UUID mRecognizerId;

    /**
     * A human readable label of the interpretation.
     * This also is used to signify what the shape actually is regardless of where it comes from.
     */
    private final String mInterpretation;

    /**
     * The confidence of the interpretation (a value between 0 and 1).
     */
    private final double mConfidence;

    /**
     * The complexity is not bounded but should always be larger than 0.
     */
    private final double mComplexity;

    /**
     * True if a user forced this interpretation.
     * This is a signal to the recognizer that this interpretation can not be overwritten with a different possibility.
     * An interpretation shall remain forced until a user performs an action that changes it from forced to not forced.
     */
    private final boolean mIsforced;

    /**
     * True if this interpretation is the final form of this shape.
     * This signifies to the recognizer that a shape with this interpretation
     * should not be used as a possible subshape of an even larger more complex shape.
     *
     * This value should not be used for determining the order of interpretations.
     */
    private final boolean mIsEndState;

    /**
     * Description of the object. (or its interpretation)
     */
    private String mDescription = "";
}
