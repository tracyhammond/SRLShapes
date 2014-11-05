package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.SrlObject;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlInterpretationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * A shape is anything that can be composed one or more subOjects. ({@link edu.tamu.srl.sketch.core.abstracted.SrlObject}).
 * A shape is assumed to have been recognized and may contain information pertaining to recognition.
 * A shape itself is an interpretation and as such it does not contain any interpretation.
 */
public class SrlShape extends SrlObject {

    /**
     * Was this object made up from a collection of sub{@link edu.tamu.srl.sketch.core.abstracted.SrlObject}s?
     * If so they are in this list.
     * This list usually gets filled in through recognition.
     * This list can be examined hierarchically.
     * e.g., an arrow might have three lines inside, and each line might have a stroke.
     */
    private List<SrlObject> mSubShapes;

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
     * With 1 being 100% confident
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
    private final boolean mIsForced;

    /**
     * True if this interpretation is the final form of this shape.
     * This signifies to the recognizer that a shape with this interpretation
     * should not be used as a possible subshape of an even larger more complex shape.
     *
     * This value should not be used for determining the order of interpretations.
     */
    private final boolean mIsEndState;

    /**
     * Description of the object. Typically a sentence.
     */
    private String mDescription;

    /**
     * Default constructor.
     * <p/>
     * Creates an object with an id and a time.
     */
    public SrlShape() {
        super();
        mDescription = "";
        mSubShapes = new ArrayList<>();
        mInterpretationId = null;
        mRecognizerId = null;
        mConfidence = 0;
        mComplexity = -1;
        mIsForced = false;
        mIsEndState = false;
        mInterpretation = null;
    }

    /**
     * A copy constructor.
     * <p/>
     * Copies all values from the given object.
     *
     * @param o the object that is being copied.
     */
    public SrlShape(final SrlShape o) {
        super(o);
        this.mDescription = o.getDescription();
        mInterpretationId = o.getInterpretationId();
        mRecognizerId = o.getRecognizerId();
        mConfidence = o.getConfidence();
        mComplexity = o.getComplexity();
        mIsForced = o.isForced();
        mIsEndState = o.isEndState();
        mInterpretation = o.getInterpretation();
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time The time the shape was originally created.
     * @param id   The unique identifier of the shape.
     * @param config The set of data used to configure interpretation.
     * @param description the description of the shape.
     */
    public SrlShape(final long time, final UUID id, final SrlInterpretationConfig config, final String description) {
        super(time, id);
        if (config != null) {
            mInterpretationId = config.interpretationId;
            mRecognizerId = config.recognizerId;
            mConfidence = config.confidence;
            mComplexity = config.complexity;
            mIsForced = config.isForced;
            mIsEndState = config.isEndState;
            mInterpretation = config.interpretation;
        } else {
            mInterpretationId = null;
            mRecognizerId = null;
            mConfidence = 0;
            mComplexity = -1;
            mIsForced = false;
            mIsEndState = false;
            mInterpretation = null;
        }
        this.mDescription = description;
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time          The time the shape was originally created.
     * @param id            The unique identifier of the shape.
     * @param isUserCreated True if the user created the stroke instead of the computer.
     * @param config The set of data used to configure interpretation.
     * @param description the description of the shape.
     */
    public SrlShape(final long time, final UUID id, final boolean isUserCreated, final SrlInterpretationConfig config, final String description) {
        super(time, id, isUserCreated);
        if (config != null) {
            mInterpretationId = config.interpretationId;
            mRecognizerId = config.recognizerId;
            mConfidence = config.confidence;
            mComplexity = config.complexity;
            mIsForced = config.isForced;
            mIsEndState = config.isEndState;
            mInterpretation = config.interpretation;
        } else {
            mInterpretationId = null;
            mRecognizerId = null;
            mConfidence = 0;
            mComplexity = -1;
            mIsForced = false;
            mIsEndState = false;
            mInterpretation = null;
        }
        this.mDescription = description;
    }

    /**
     * This is the id of the interpretation and should be unique across all interpretations.
     * This value should not be generated at runtime but instead should be linked to a specific interpretation of a certain recognizer
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     *
     * This id may not be unique across multiple recognizers.
     * This id in combination with the {@link #mRecognizerId} is guaranteed to be unique.
     * @return {@link UUID}
     */
    public final UUID getInterpretationId() {
        return mInterpretationId;
    }

    /**
     * This is the id of the recognizer and should be unique across all recognizer.
     * This value should not be generated at runtime but instead should be linked to a specific recognizer.
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     *
     * Each recognizer may have multiple interpretations so this id may not be unique across multiple interpretations.
     * This id in combination with the {@link #mInterpretationId} is guaranteed to be unique.
     * @return {@link UUID}
     */
    public final UUID getRecognizerId() {
        return mRecognizerId;
    }

    /**
     * A human readable label of the interpretation.
     * This also is used to signify what the shape actually is regardless of where it comes from.
     * @return The interpretation.
     */
    public final String getInterpretation() {
        return mInterpretation;
    }

    /**
     * The confidence of the interpretation (a value between 0 and 1).
     * With 1 being 100% confident.
     * @return confidence level
     */
    public final double getConfidence() {
        return mConfidence;
    }

    /**
     * The complexity is not bounded but should always be larger than 0.
     * @return complexity level
     */
    public final double getComplexity() {
        return mComplexity;
    }

    /**
     * True if a user forced this interpretation.
     * This is a signal to the recognizer that this interpretation can not be overwritten with a different possibility.
     * An interpretation shall remain forced until a user performs an action that changes it from forced to not forced.
     * @return true if the shape is forced
     */
    public final boolean isForced() {
        return mIsForced;
    }

    /**
     * True if this interpretation is the final form of this shape.
     * This signifies to the recognizer that a shape with this interpretation
     * should not be used as a possible subshape of an even larger more complex shape.
     *
     * This value should not be used for determining the order of interpretations.
     * @return true if the shape is in its end state.
     */
    public final boolean isEndState() {
        return mIsEndState;
    }

    /**
     * Description of the object. Typically a sentence.
     * @return description of the object
     */
    public final String getDescription() {
        return mDescription;
    }
}
