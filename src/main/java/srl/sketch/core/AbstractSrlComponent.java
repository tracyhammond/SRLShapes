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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractSrlComponent implements Cloneable, Serializable, ISrlTransformable {
	/**
	 * counter will be incremented by 0x10000 for each new AbstractSrlComponent that is
	 * created counter is used as the most significant bits of the UUID
	 * 
	 * initialized to 0x4000 (the version -- 4: randomly generated UUID) along
	 * with 3 bytes of randomness: Math.random()*0x1000 (0x0 - 0xFFF)
	 * 
	 * the randomness further reduces the chances of collision between multiple
	 * sketches created on multiple computers simultaneously
	 */
	public static long counter = 0x4000L | (long) (Math.random() * 0x1000);

	protected UUID id;

	protected transient SrlBoundingBox boundingBox;
	protected transient Polygon convexHull;

	protected Map<String, String> attributes;

	public AbstractSrlComponent() {
		id = nextID();
		boundingBox = null;
		convexHull = null;
		attributes = new HashMap<String, String>();

	}

	public AbstractSrlComponent(AbstractSrlComponent copyFrom) {
		id = copyFrom.id;
		boundingBox = copyFrom.boundingBox;
		convexHull = copyFrom.convexHull;

		attributes = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : copyFrom.attributes
				.entrySet()) {
			attributes.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Checks if this AbstractSrlContainer has the given attribute
	 * 
	 * @param attr
	 *            the name of the attribute to check for
	 * @return true if this AbstractSrlContainer has the given attribute
	 */
	public final boolean hasAttribute(String attr) {
		if(attributes!=null)
			return attributes.containsKey(attr);
		return false;
	}

	/**
	 * Set an attribute value. Will overwrite any value currently set for that
	 * attribute
	 * 
	 * @param attr
	 *            attribute name
	 * @param value
	 *            attribute value
	 * @return the old value of the attribute, or null if none was set
	 */
	public final String setAttribute(String attr, String value) {
		return attributes.put(attr, value);
	}

    /**
     * Get the value of the attribute with the given key from this shape's
     * attribute map. If no attribute with that key exists, returns null.
     *
     * @param key
     *            the key of the attribute to get.
     * @return the String value for the attribute with the given key, or null if
     *         there is no such attribute.
     */
	public final String getAttribute(String attr) {
		if (!hasAttribute(attr))
			return null;
		return attributes.get(attr);
	}

	/**
	 * Remove the given attribute from this AbstractSrlContainer.
	 * 
	 * @param attr
	 *            the name of the attribute
	 * @return the value that was removed, or null if nothing was removed
	 */
	public final String removeAttribute(String attr) {
		return attributes.remove(attr);
	}

	public void applyTransform(AffineTransform xform) {
		applyTransform(xform, new HashSet<ISrlTransformable>());
	}

	public void scale(double xfactor, double yfactor) {
		applyTransform(AffineTransform.getScaleInstance(xfactor, yfactor));
	}

	public void translate(double xincrement, double yincrement) {
		applyTransform(AffineTransform.getTranslateInstance(xincrement,
				yincrement));
	}

	public void rotate(double radians) {
		applyTransform(AffineTransform.getRotateInstance(radians));
	}

	public void rotate(double radians, double xcenter, double ycenter) {
		applyTransform(AffineTransform.getRotateInstance(radians, xcenter,
				ycenter));
	}

    /**
     * Let the class know that it needs to recompute any cached values. Use this
     * method if you {@link #getPoints()} and modify the list directly. The
     * method {@link #setPoints(java.util.List)} should automatically call this method so
     * any cached values are forced to update next time it is requested. The
     * method {@link #addPoint(ISrlPoint)} should update cached values with each
     * point addition, operating in O(1) time.
     */
	public final void flagExternalUpdate() {
		boundingBox = null;
		convexHull = null;
	}

	public final UUID getId() {
		return id;
	}

	public final void setId(UUID id) {
		this.id = id;
	}

	protected abstract void calculateBBox();

    /**
     * Get the bounding box around this stroke. If you use the
     * {@link #addPoint(ISrlPoint)} method, the bounding box is updated iteratively
     * in O(1) time. If you use something like {@link #getPoints()} and modify
     * points directly, you will need to {@link #flagExternalUpdate()} so we
     * know we need to recompute the bounding box, at a cost of O(1).
     *
     * @return the bounding box around this stroke.
     */
    /**
     * Get the bounding box for this shape. If no bounding box has been computed
     * yet, or if {@link #flagExternalUpdate()} has been called, this method
     * will loop over all strokes to get the bounding box that encompasses all
     * strokes, possibly taking O(m*n) time (m strokes, n points each). Else, it
     * just returns the iteratively computed bounding box that's updated with
     * each call to {@link #addStroke(ISrlStroke)}.
     *
     * @return the bounding box.
     */
	public final SrlBoundingBox getBoundingBox() {
		if (boundingBox == null)
			calculateBBox();
		return boundingBox;
	}

    /**
     * Get the convex hull for this shape. If no convex hull has been computed
     * yet, or if {@link #flagExternalUpdate()} has been called, this method
     * will loop over all strokes to get the convex hull that encompasses all
     * strokes, possibly taking O(n*log(n)) time (n points in shape).
     *
     * @return the convex hull
     */
	public final Polygon getConvexHull() {
		return convexHull;
	}

	public boolean equals(Object other) {
		if (other instanceof AbstractSrlComponent) {
			return id.equals(((AbstractSrlComponent) other).getId());
		}
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public abstract AbstractSrlComponent clone();


	public static Comparator<AbstractSrlComponent> getXComparator() {
		return new Comparator<AbstractSrlComponent>() {

			@Override
			public int compare(AbstractSrlComponent arg0, AbstractSrlComponent arg1) {
				return Double.compare(arg0.getBoundingBox().getLeft(), arg1
						.getBoundingBox().getLeft());
			}

		};
	}

	public static Comparator<AbstractSrlComponent> getYComparator() {
		return new Comparator<AbstractSrlComponent>() {

			@Override
			public int compare(AbstractSrlComponent arg0, AbstractSrlComponent arg1) {
				return Double.compare(arg0.getBoundingBox().getTop(), arg1
						.getBoundingBox().getTop());
			}

		};
	}

    /**
     * Get the attribute map for this shape
     *
     * @return The map of attributes
     */
	public final Map<String, String> getAttributes() {
		return attributes;
	}

	public static final String NAME_ATTR_KEY = "name";

	public final String getName() {
		return (hasAttribute(NAME_ATTR_KEY)) ? getAttribute(NAME_ATTR_KEY)
				: null;
	}

	public final String setName(String name) {
		return setAttribute(NAME_ATTR_KEY, name);
	}

	public static final String DESCRIPTION_ATTR_KEY = "description";

    /**
     * Returns a string description of the shape.
     *
     * @return a String of the shape description.
     */
	public final String getDescription() {
		return (hasAttribute(DESCRIPTION_ATTR_KEY)) ? getAttribute(DESCRIPTION_ATTR_KEY)
				: null;
	}

    /**
     * Sets the description of the shape.
     *
     * @param description
     *            A String describing the shape.
     */
	public final String setDescription(String description) {
		return setAttribute(DESCRIPTION_ATTR_KEY, description);
	}

	/**
	 * Look deep into two components to check equality
	 * 
	 * @param other
	 * @return
	 */
	public abstract boolean equalsByContent(AbstractSrlComponent other);
	
	public static UUID nextID() {

		counter += 0x10000L;
		return new UUID(counter, System.nanoTime() | 0x8000000000000000L);
	}
}
