/**
 * ISrlSketch.java
 * 
 * Revision History: <br>
 * (5/26/08) awolin - Created the interface <br>
 * (5/26/08) bpaulson - Added adders for strokes and shapes <br>
 * (5/27/08) bpaulson - Added clear() function <br>
 * (5/27/08) awolin - Added getPoints() function
 * 
 * <p>
 * 
 * <pre>
 * This work is released under the BSD License:
 * (C) 2008 Sketch Recognition Lab, Texas A&amp;M University (hereafter SRL @ TAMU)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sketch Recognition Lab, Texas A&amp;M University 
 *       nor the names of its contributors may be used to endorse or promote 
 *       products derived from this software without specific prior written 
 *       permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY SRL @ TAMU ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SRL @ TAMU BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * </pre>
 */

package srl.sketch.core;

import java.util.List;
import java.util.Set;
import java.util.UUID;



/**
 * ISrlSketch interface containing a list of IShapes and IStrokes
 * 
 * @author awolin
 */
public interface ISrlSketch extends Cloneable {

	/**
	 * Gets the IStrokes associated with the ISrlSketch
	 * 
	 * @return A List of IStrokes
	 */
	public List<SrlStroke> getStrokes();

	/**
	 * Gets the IShapes associated with the ISrlSketch
	 * 
	 * @return A List of IShapes
	 */
	public List<SrlShape> getShapes();

	/**
	 * Get the ID of the ISrlSketch
	 * 
	 * @return UUID of the ISrlSketch
	 */
	public UUID getID();

	/**
	 * Get the number of strokes in the sketch
	 * 
	 * @return The number of strokes in the sketch
	 */
	public int getNumStrokes();

	/**
	 * Get the number of shapes in the sketch
	 * 
	 * @return The number of shapes in the sketch
	 */
	public int getNumShapes();

	/**
	 * Gets a bounding box for the entire sketch
	 * 
	 * @return A SrlBoundingBox for the sketch
	 */
	public SrlBoundingBox getBoundingBox();

	/**
	 * Get a list of points in temporal order. The list is built from the stroke
	 * list and does not contain repeated points.
	 * 
	 * @return A list of points in temporal order
	 */
	public List<SrlPoint> getPoints();

	/**
	 * Sets the IStrokes associated with the ISrlSketch
	 * 
	 * @param strokes
	 *            A List of IStrokes
	 */
	public void setStrokes(List<? extends SrlStroke> strokes);

	/**
	 * Sets the IShapes associated with the ISrlSketch
	 * 
	 * @param shapes
	 *            A List of IShapes
	 */
	public void setShapes(List<? extends SrlShape> shapes);

	/**
	 * Add a stroke to the list of strokes
	 * 
	 * @param stroke
	 *            stroke to add
	 */
	public void addStroke(SrlStroke stroke);

	/**
	 * Add a shape to the list of shapes
	 * 
	 * @param shape
	 *            shape to add
	 */
	public void addShape(SrlShape shape);

	/**
	 * Removes a stroke from the sketch. Also removes any shapes that contain
	 * this stroke.
	 * 
	 * @param stroke
	 *            Stroke to remove from the sketch
	 * @return True if the stroke was removed, false otherwise
	 */
	public boolean removeStroke(SrlStroke stroke);

	/**
	 * Removes a stroke with the given ID from the sketch. Also removes any
	 * shapes that contain a stroke with the given ID.
	 * 
	 * @param strokeID
	 *            ID of the stroke to remove from the sketch
	 * @return True if the a stroke with the given ID was found and removed,
	 *         false otherwise
	 */
	public boolean removeStroke(UUID strokeID);

	/**
	 * Removes a shape from the sketch
	 * 
	 * @param shape
	 *            Shape to remove from the sketch
	 * @return True if the shape was removed, false otherwise
	 */
	public boolean removeShape(SrlShape shape);

	/**
	 * Clears all the sketch parameters (strokes, shapes, etc.)
	 */
	public void clear();

	/**
	 * Clone an ISrlSketch
	 * 
	 * @return A new, deep copied ISrlSketch
	 */
	public Object clone();

	/**
	 * Get the last stroke added to a sketch
	 * 
	 * @return Last ISrlStroke added
	 */
	public SrlStroke getLastStroke();

	/**
	 * Get the first stroke added to the sketch
	 * 
	 * @return The first stroke added, or null if not strokes added
	 */
	public SrlStroke getFirstStroke();

	/**
	 * Get all the shape objects that contain a given stroke.
	 * 
	 * @param stroke
	 *            the stroke of interest
	 * @return a set containing all shape objects with the given stroke.
	 */
	public Set<SrlShape> getShapesForStroke(SrlStroke stroke);

	/**
	 * Find all shapes that contain all of the strokes given.
	 * 
	 * @param strokes
	 *            a list of strokes
	 * @return a set of shapes containing all of the given strokes.
	 */
	public Set<SrlShape> getShapesForStrokes(List<SrlStroke> strokes);

	public void translate(double x, double y);

	public void scale(double x, double y);

}
