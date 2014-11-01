
/*******************************************************************************
 *  Revision History:<br>
 *  SRL Member - File created
 *  SRL Member - Moved SVG canvas to a different file.
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

import srl.sketch.core.comparators.SrlTimePeriodComparator;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SrlSketch extends AbstractSrlContainer implements ISrlTimePeriod, Comparable<ISrlTimePeriod>, ISrlSketch {

    private static SrlTimePeriodComparator comparator = new SrlTimePeriodComparator();

	public SrlSketch() {

	}

	public SrlSketch(SrlSketch copyFrom) {
		super(copyFrom);
	}

	public SrlSketch(SrlStroke... strokes){
		this();
		for(SrlStroke stroke:strokes)
			add(stroke);
	}

    /**
     * Get the ID of the ISrlSketch
     *
     * @return UUID of the ISrlSketch
     */
    public UUID getID() {
        return id;
    }

    /**
     * Get the number of shapes in the sketch
     *
     * @return The number of shapes in the sketch
     */
    public int getNumShapes() {
        return super.getShapes().size();
    }

    /**
     * Add a stroke to the list of strokes
     *
     * @param stroke
     *            stroke to add
     */
    @Override public void addStroke(SrlStroke stroke) {
        super.add(stroke);
    }

    /**
     * Add a shape to the list of shapes
     *
     * @param shape
     *            shape to add
     */
    @Override public void addShape(SrlShape shape) {
        super.add(shape);
    }

    /**
     * Removes a stroke from the sketch. Also removes any shapes that contain
     * this stroke.
     * TODO: implement?
     * @param stroke
     *            Stroke to remove from the sketch
     * @return True if the stroke was removed, false otherwise
     */
    @Override public boolean removeStroke(SrlStroke stroke) {
        return false;
    }

    /**
     * Removes a stroke with the given ID from the sketch. Also removes any
     * shapes that contain a stroke with the given ID.
     *TODO: implement?
     * @param strokeID
     *            ID of the stroke to remove from the sketch
     * @return True if the a stroke with the given ID was found and removed,
     *         false otherwise
     */
    @Override public boolean removeStroke(UUID strokeID) {
        return false;
    }

    /**
     * Removes a shape from the sketch
     *TODO: implement?
     * @param shape
     *            Shape to remove from the sketch
     * @return True if the shape was removed, false otherwise
     */
    @Override public boolean removeShape(SrlShape shape) {
        return false;
    }

    /**
     * Clone an ISrlSketch
     *
     * @return A new, deep copied ISrlSketch
     */
    @Override
	public SrlSketch clone() {
		return new SrlSketch(this);
	}

    /**
     * Get all the shape objects that contain a given stroke.
     *TODO: implement?
     * @param stroke
     *            the stroke of interest
     * @return a set containing all shape objects with the given stroke.
     */
    @Override public Set<SrlShape> getShapesForStroke(SrlStroke stroke) {
        return null;
    }

    /**
     * Find all shapes that contain all of the strokes given.
     *TODO: implement?
     * @param strokes
     *            a list of strokes
     * @return a set of shapes containing all of the given strokes.
     */
    @Override public Set<SrlShape> getShapesForStrokes(List<SrlStroke> strokes) {
        return null;
    }

	@Override
	public int compareTo(ISrlTimePeriod other) {
		return comparator.compare(this, other);
	}
}



