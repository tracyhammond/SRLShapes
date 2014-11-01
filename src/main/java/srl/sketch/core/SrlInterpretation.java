/*******************************************************************************
 *  Revision History:<br>
 *  SRL Member - File created
 *
 *  <p>
 *  <pre>
 *  This work is released under the BSD License:
 *  (C) 2011 Sketch Recognition Lab, Texas A&M University (hereafter SRL @ TAMU)
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Sketch Recognition Lab, Texas A&M University 
 *        nor the names of its contributors may be used to endorse or promote 
 *        products derived from this software without specific prior written 
 *        permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY SRL @ TAMU ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL SRL @ TAMU BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  </pre>
 *  
 *******************************************************************************/
package srl.sketch.core;

import java.io.Serializable;
import java.util.UUID;

public class SrlInterpretation implements
		Comparable<SrlInterpretation>,Serializable, ISrlConfidence {

    /**
     * This is the id of the interpretation and should be unique across all interpretations.
     * This value should not be generated at runtime but instead should be linked to a specific interpretation and recognizer.
     * This can be used in the case of interpretations having the same label (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     *
     * The least significant set of bits define what recognizer it is
     */
    private UUID interpretationId;

    /**
     * A human readable label of the interpretation.  This also is used to signify what the shape actually is regardless of where it comes from.
     */
	public String label;

    /**
     * The confidence of the interpretation (a value between 0 and 1).
     *
     * This value is typically used in
     */
	public double confidence;

    /**
     * The complexity is not bounded but should always be larger than 0.
     */
    public double complexity;

    /**
     * True if a user forced this interpretation.
     * This is a signal to the recognizer that this interpretation should always be first.
     * An interpretation shall remain forced until a user performs an action that changes it from forced to not forced.
     * Shapes with a forced interpretation should still contain the list of all possible interpretations for use at a later time.
     */
    private boolean forced;

    /**
     * True if this interpretation is the final form of this shape.
     * This signifies to the recognizer that a shape with this interpretation
     * should not be used as a possible subshape of an even larger more complex shape.
     *
     * This value should not be used for determining the order of interpretations.
     */
    private boolean endState;

	public SrlInterpretation() {
        this(null, 0);
	}

	public SrlInterpretation(String label, double confidence) {
		this(label, confidence, 0);
	}

    public SrlInterpretation(String label, double confidence, double complexity) {
        this(label, confidence, complexity, false, false);
    }

    public SrlInterpretation(String label, double confidence, double complexity, boolean forced, boolean endState) {
        this(null, label, confidence, complexity, forced, endState);
    }

    public SrlInterpretation(UUID interpretationId, String label, double confidence, double complexity, boolean forced, boolean endState) {
        this.interpretationId = interpretationId;
        this.label = label;
        this.confidence = confidence;
        this.complexity = complexity;
        this.forced = forced;
        this.endState = endState;
    }

    /**
     * @param other
     * @return the difference of the confidence level in the interpretation.
     */
	@Override
	public int compareTo(SrlInterpretation other) {
		return Double.compare(confidence, other.confidence);
	}

	@Override
	public SrlInterpretation clone() {
		return new SrlInterpretation(label, confidence, complexity);
	}

	@Override
	public String toString() {
		return label;
	}

    @Override public Double getConfidence() {
        return confidence;
    }

    @Override public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
