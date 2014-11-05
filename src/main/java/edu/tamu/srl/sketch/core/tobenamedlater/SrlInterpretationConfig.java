package edu.tamu.srl.sketch.core.tobenamedlater;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 *
 * Can be used as a parameter to a {@link edu.tamu.srl.sketch.core.object.SrlShape} constructor instead of a long list.
 * This is not actually stored inside the shape but instead its values are stored inside the shape.
 * This is because the shape itself is an interpretation and does not actually contain an interpretation.
 */
@SuppressWarnings({ "checkstyle:visibilitymodifier" })
public class SrlInterpretationConfig {

    /**
     * This is the id of the interpretation and should be unique across all interpretations.
     * This value should not be generated at runtime but instead should be linked to a specific interpretation of a certain recognizer
     * This can be used in the case of interpretations having the same {@link #interpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     *
     * This id may not be unique across multiple recognizers.
     * This id in combination with the {@link #recognizerId} is guaranteed to be unique.
     */
    public UUID interpretationId;

    /**
     * This is the id of the recognizer and should be unique across all recognizer.
     * This value should not be generated at runtime but instead should be linked to a specific recognizer.
     * This can be used in the case of interpretations having the same {@link #interpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     *
     * Each recognizer may have multiple interpretations so this id may not be unique across multiple interpretations.
     * This id in combination with the {@link #interpretationId} is guaranteed to be unique.
     */
    public UUID recognizerId;

    /**
     * A human readable label of the interpretation.
     * This also is used to signify what the shape actually is regardless of where it comes from.
     */
    public String interpretation;

    /**
     * The confidence of the interpretation (a value between 0 and 1).
     * With 1 being 100% confident
     */
    public double confidence;

    /**
     * The complexity is not bounded but should always be larger than 0.
     */
    public double complexity;

    /**
     * True if a user forced this interpretation.
     * This is a signal to the recognizer that this interpretation can not be overwritten with a different possibility.
     * An interpretation shall remain forced until a user performs an action that changes it from forced to not forced.
     */
    public boolean isForced;

    /**
     * True if this interpretation is the final form of this shape.
     * This signifies to the recognizer that a shape with this interpretation
     * should not be used as a possible subshape of an even larger more complex shape.
     *
     * This value should not be used for determining the order of interpretations.
     */
    public boolean isEndState;

    /**
     * @param interpretationId {@link #interpretationId}
     * @param recognizerId {@link #recognizerId}
     * @param interpretation {@link #interpretation}
     * @param confidence {@link #confidence}
     * @param complexity {@link #complexity}
     * @param isForced {@link #isForced}
     * @param isEndState {@link #isEndState}
     */
    @SuppressWarnings({ "checkstyle:hiddenfield" })
    public SrlInterpretationConfig(final UUID interpretationId, final UUID recognizerId, final String interpretation, final double confidence,
            final double complexity,
            final boolean isForced, final boolean isEndState) {
        this.interpretationId = interpretationId;
        this.recognizerId = recognizerId;
        this.interpretation = interpretation;
        this.confidence = confidence;
        this.complexity = complexity;
        this.isForced = isForced;
        this.isEndState = isEndState;
    }
}
