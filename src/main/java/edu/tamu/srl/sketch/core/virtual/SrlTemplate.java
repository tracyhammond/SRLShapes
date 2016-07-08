package edu.tamu.srl.sketch.core.virtual;

import com.google.common.collect.Lists;
import edu.tamu.srl.sketch.core.abstracted.SrlObject;

import java.util.List;

/**
 * A template used for recognition purposes.
 */
public class SrlTemplate {
    /**
     * The list of interpretations that apply to this template.
     */
    private final List<String> interpretations;
    /**
     * The object represented by this template.
     */
    private final SrlObject srlObject;
    /**
     * If this is a positive example of the interpretation or a negative example of the interpretation.
     */
    private final boolean positiveExample;

    /**
     * @param interpretation The main interpretation label for this template
     * @param isPositiveExample If The template is a positive example or a negative example
     * @param object The object represented by this shape
     */
    public SrlTemplate(final String interpretation, final boolean isPositiveExample, final SrlObject object) {
        this.positiveExample = isPositiveExample;
        interpretations = Lists.newArrayList(interpretation);
        srlObject = object;
    }

    /**
     * Adds an equivalent interpretation label to this template.
     *
     * @param interpretation The interpretation label
     */
    public final void addEquivalentInterpretation(final String interpretation) {
        interpretations.add(interpretation);
    }

    /**
     * @return The list of equivalent interpretation labels for this shape.
     */
    public final List<String> getInterpretations() {
        return interpretations;
    }

    /**
     * @return Srl object contained in this template
     */
    public final SrlObject getSrlObject() {
        return srlObject;
    }

    /**
     * @return if the template is a positive example or a negative example
     */
    public final boolean isPositiveExample() {
        return positiveExample;
    }
}
