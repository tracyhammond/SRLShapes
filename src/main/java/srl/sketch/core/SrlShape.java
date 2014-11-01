package srl.sketch.core;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import srl.sketch.core.comparators.SrlTimePeriodComparator;

public class SrlShape extends AbstractSrlContainer implements ISrlClassifiable {

    private static final String SHAPE_LABEL_ATTR_KEY = "shape_label_text";
    private static SrlTimePeriodComparator comparator = new SrlTimePeriodComparator();

	protected ArrayList<SrlInterpretation> interpretations;

	protected ArrayList<SrlAlias> aliases;

	private transient long timeStart = -1L;
	private transient long timeEnd = -1L;

	public SrlShape() {
		super();

		interpretations = new ArrayList<SrlInterpretation>();
		aliases = new ArrayList<SrlAlias>();
	}

	public SrlShape(SrlShape copyFrom) {
		super(copyFrom);

		interpretations = new ArrayList<SrlInterpretation>();
		for (SrlInterpretation i : copyFrom.interpretations)
			interpretations.add(i.clone());

		aliases = new ArrayList<SrlAlias>();
		for (SrlAlias a : copyFrom.aliases)
			aliases.add(a.clone());
	}

	public SrlShape(Collection<? extends SrlStroke> subStrokes,
            Collection<? extends SrlShape> subShapes) {
		this();
		addAll(subStrokes);
		addAll(subShapes);
	}

	@Override
	public SrlInterpretation getInterpretation() {
		return (interpretations.size() > 0) ? interpretations
				.get(interpretations.size() - 1) : null;
	}

	@Override
	public List<SrlInterpretation> getNBestList() {
		return interpretations;
	}

	@Override
	public void addInterpretation(SrlInterpretation i) {
		int position = Collections.binarySearch(interpretations, i);

		if (position >= 0)
			return;

		interpretations.add(-(position + 1), i);
	}

	@Override
	public void setNBestList(List<SrlInterpretation> list) {
		interpretations = new ArrayList<SrlInterpretation>(list);
	}

	/**
	 * Set the label of this shape.
	 * 
	 * Override all current interpretations.
	 * 
	 * @param label
	 */
	public void setInterpretation(String label) {
		if (interpretations.size() > 1) {
			System.err
					.println("warning: clearning interpretations with setInterpretation");
		}
		interpretations.clear();
		interpretations.add(new SrlInterpretation(label, 1.0));
	}

    /**
     * Set the time when this shape was recognized by the recognizer. This field
     * is optional and may be set to null if you're not using recognition time.
     *
     * TODO: implement
     * @param recTime
     *            the time the shape was recognized.
     */
    public void setRecognitionTime(Long recTime) {

    }

    /**
     * Sets the List of ISrlStroke for the IShape. Automatically calls
     * {@link #flagExternalUpdate()} to invalidate cached values and force
     * recalculation.
     * TODO: find out if this flag thing is actually needed
     *
     * @param strokes
     *            the list of strokes to set to the IShape.
     */
    @Override public void setStrokes(List<? extends SrlStroke> strokes) {
        super.setStrokes(strokes);
        this.flagExternalUpdate();
    }

    /**
     * Set the list of subshapes.
     * TODO: implement
     * @param subShapes
     *            the list of subshapes to set.
     */
    public void setSubShapes(List<SrlShape> subShapes) {

    }

    /**
     * Returns whether two shapes are equal by comparing their strokes and all
     * the strokes of their SubShapes.
     * TODO: implement
     * @param os
     *            The other shape to compare to
     *
     * @return True if the shapes are equal, false otherwise
     */
    public boolean equalsByContent(SrlShape os) {
        return false;
    }

    @Override
	public void setInterpretation(SrlInterpretation i) {
		if (interpretations.size() > 1) {
			System.err
					.println("warning: clearning interpretations with setInterpretation");
		}
		interpretations.clear();
		interpretations.add(i);
	}

    /**
     * Clone an {@link ISrlShape}.
     *
     * @return a new, deep copied IShape.
     */
	@Override
	public SrlShape clone() {
		return new SrlShape(this);
	}

    /**
     * Returns if a shape contains a stroke
     * TODO: implement
     * @param stroke
     *            The stroke to check for
     *
     * @return boolean, stating if stroke is a member of the shape
     */
    public boolean containsStroke(SrlStroke stroke) {
        return false;
    }

    /**
     * Returns if a shape or its subshapes contain a stroke
     * TODO: implement
     * @param stroke
     *            The stroke to check for
     *
     * @return boolean, stating if stroke is a member of the shape
     */
    public boolean containsStrokeRecursive(SrlStroke stroke) {
        return false;
    }

    /**
     * Add the given alias into this shape. Aliases are uniquely identified by
     * name, and this alias will replace any existing aliases with the same
     * name.
     *
     * @param a
     *            the alias to set for this shape.
     */
    public void addAlias(SrlAlias a) {
		aliases.add(a);
	}

    /**
     * Add a stroke to the list of strokes. Strokes should be added in ascending
     * temporal order to the shape. We don't enforce this, so it's your job to
     * do book keeping. Temporal ordering of strokes means that the last points
     * in the strokes have ascending timestamps. We don't care if strokes
     * overlap some in time, just as long as the stroke that was completed last
     * <p>
     * This method updates cached values in an iterative, O(1) manner.
     *
     * @param stroke
     *            the stroke to add to the list of strokes.
     */
    public void addStroke(SrlStroke stroke) {
        super.add(stroke);
    }

    /**
     * Get all the aliases that have been set for this shape. There is no
     * guaranteed ordering for the aliases. If no aliases have been set, this
     * method will return an empty collection.
     * <p>
     * This method returns a
     * {@link Collections#unmodifiableCollection(Collection)}, so you cannot
     * modify its elements outside this class.
     *
     * @return a collection of all the aliases set for this shape.
     */
    public List<SrlAlias> getAliases() {
		return aliases;
	}

    /**
     * Get the ID of the IShape.
     *
     * @return UUID of the IShape.
     */
    public UUID getID() {
        return id;
    }

    /**
     * Get the time when this shape was recognized by the recognizer. This field
     * is optional and if not set, will return null.
     *TODO: implement
     * @return the time the shape was recognized.
     */
    public Long getRecognitionTime() {
        return null;
    }

    /**
     * Returns the list of bottom level subshapes in the shape
     *TODO: implement
     * @return all bottom-level subshapes
     */
    public List<SrlShape> getRecursiveSubShapes() {
        return null;
    }

    /**
     * Returns the list of all shapes recursively contained by this shape and
     * its children
     *TODO: implement
     * @return
     */
    public List<SrlShape> getAllRecursiveSubShapes() {
        return null;
    }

    /**
     * Returns the subshape in a shape, found by index in the shape's subshape
     * list. If you modify the subshape, you need to
     * {@link #flagExternalUpdate()} so this object knows it needs to update
     * cached value.
     *TODO: implement
     * @param index
     *            the position of the subshape in the Shape's stroke list.
     * @return a Shape (IShape).
     *
     */
    public SrlShape getSubShape(int index) {
        return null;
    }

    /**
     * Get the subshape from the list of subshapes that has the given UUID. If
     * there is no such subshape, return null.
     *TODO: implement
     * @param shapeId
     *            the ID of the subshape you're looking for.
     * @return the shape that has the given UUID (based on equals), else null if
     *         there is no such subshape.
     */
    public SrlShape getSubShape(UUID shapeId) {
        return null;
    }

    /**
     * Gets the list of subshapes in this shape.
     *TODO: implement
     * @return the list of subshapes.
     */
    public List<SrlShape> getSubShapes() {
        return null;
    }

    /**
     * Gets the creation time of the shape. This creation time is defined as the
     * maximum time of the strokes (the stroke completed last). UPDATING THE
     * STROKES OR POINTS THROUGH NON-APPEND METHODS CAN BREAK THIS
     * FUNCTIONALITY. This value is cached and updated through append methods
     * {@link #addStroke(SrlStroke)}. If you modify strokes manually with
     * {@link #getStrokes()} or any variant of {@link #getStroke(int)}, you need
     * to call {@link #flagExternalUpdate()} to force an update of this cached
     * value.
     *TODO: implement
     * @return the creation time of the shape.
     */
    public long getTime() {
        return 0;
    }

    /**
     * Get the alias with the given name. If an alias with the given name does
     * not exist, this method will return null.
     *
     * @param s
     *            the name of the alias to get.
     * @return the alias with the given name that's been set for this shape, or
     *         null if there is no such named alias.
     */
    public SrlAlias getAlias(String s) {
		for (SrlAlias a : aliases)
			if (a.name.equals(s))
				return a;
		return null;
	}

	@Override
	public void setInterpretation(String label, double confidence) {
		setInterpretation(new SrlInterpretation(label, confidence));
	}

	@Override
	public String toString() {
		return super.toString() + " " + getInterpretation() + " " + id;
	}

	@Override
	public int compareTo(ISrlTimePeriod other) {
		return comparator.compare(this, other);
	}

    /**
     * Return shape's label associated with the IShape. It is far too annoying
     * to re-type shape.getAttribute("shape label text") every time and far too
     * likely there will be a typo
     *
     * @return whatever string is associated with this IShape
     */
    public String getShapeLabelText() {
        return (hasAttribute(SHAPE_LABEL_ATTR_KEY)) ? getAttribute(SHAPE_LABEL_ATTR_KEY)
                : null;
    }

    /**
     * Sets the "shape label text" attribute
     *
     * @param text
     *            A string to be displayed next to the shape
     */
    public void setShapeLabelText(String text) {
        setAttribute(SHAPE_LABEL_ATTR_KEY, text);
    }
}
