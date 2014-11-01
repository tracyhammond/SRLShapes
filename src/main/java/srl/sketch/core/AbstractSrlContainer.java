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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractSrlContainer extends AbstractSrlComponent implements
		Iterable<AbstractSrlComponent> ,ISrlTimePeriod, Comparable<ISrlTimePeriod> {

	protected List<AbstractSrlComponent> contents;

	private transient long timeStart = -1L;
	private transient long timeEnd = -1L;

	public AbstractSrlContainer() {
		super();

		contents = new ArrayList<AbstractSrlComponent>();
	}

	public AbstractSrlContainer(AbstractSrlContainer copyFrom) {
		super(copyFrom);

		contents = new ArrayList<AbstractSrlComponent>();

		for (AbstractSrlComponent comp : copyFrom.contents) {
			contents.add(comp.clone());
		}
	}

	public int size() {
		return contents.size();
	}

	@Override
	public void applyTransform(AffineTransform xform, Set<ISrlTransformable> xformed) {
		if (xformed.contains(this))
			return;

		xformed.add(this);
		for (AbstractSrlComponent comp : contents)
			comp.applyTransform(xform, xformed);
	}

	/**
	 * Add a subcomponent to this container.
	 * 
	 * @param subcomponent
	 */
	public final void add(AbstractSrlComponent subcomponent) {
		contents.add(subcomponent);
	}

	/**
	 * Add a subcomponent to this container at the specified index.
	 * 
	 * @param index
	 * @param subcomponent
	 */
	public final void add(int index, AbstractSrlComponent subcomponent) {
		contents.add(index, subcomponent);
	}

	/**
	 * Add a bunch of subcomponents to this container.
	 * 
	 * @param subcomponents
	 */
	public final void addAll(Collection<? extends AbstractSrlComponent> subcomponents) {
		contents.addAll(subcomponents);
	}
	
	/**
	 * Remove a subcomponent from this container.
	 * 
	 * @param subcomponent
	 *            subcomponent to remove
	 * @return true if something was removed
	 */
	public final boolean remove(AbstractSrlComponent subcomponent) {
		return contents.remove(subcomponent);
	}

	/**
	 * Remove the ith Component from this AbstractSrlContainer
	 * 
	 * @param i
	 * @return the value removed
	 */
	public final AbstractSrlComponent remove(int i) {
		return contents.remove(i);
	}

	/**
	 * Remove a component by its id
	 * @param id the id of the subcomponent to remove
	 * @return the removed subcomponent. If the container had no subcomponent with that id, the return value is null.
	 */
	public final synchronized AbstractSrlComponent remove(UUID id){
		AbstractSrlComponent comp = get(id);
		if(comp!=null)
			remove(comp);
		return comp;
	}
	
	/**
	 * Remove a bunch of subcomponents from this AbstractSrlContainer.
	 * 
	 * @param subcomponents
	 * @return
	 */
	public final boolean removeAll(Collection<? extends AbstractSrlComponent> subcomponents) {
		return contents.removeAll(subcomponents);
	}

	/**
	 * Find the subcomponents of a certain class.
	 * 
	 * @param clazz
	 * @return
	 */
	public final List<? extends AbstractSrlComponent> getByClassExact(
			Class<? extends AbstractSrlComponent> clazz) {
		ArrayList<AbstractSrlComponent> res = new ArrayList<AbstractSrlComponent>();

		for (AbstractSrlComponent sub : contents)
			if (clazz.equals(sub.getClass()))
				res.add(sub);

		return res;
	}

	/**
	 * Find the subcomponents that are or extend a certain class.
	 *
	 * @see #getByClassExact(Class<? extends AbstractSrlComponent >)
	 * @param clazz
	 * @return
	 */
	public final List<? extends AbstractSrlComponent> getByClassAssignable(
			Class<? extends AbstractSrlComponent> clazz) {
		ArrayList<AbstractSrlComponent> res = new ArrayList<AbstractSrlComponent>();

		for (AbstractSrlComponent sub : contents)
			if (clazz.isInstance(sub))
				res.add(sub);

		return res;
	}

	/**
	 * Recursively descend through the container and its contents.
	 * 
	 * @param clazz
	 * @return
	 */
	public final List<? extends AbstractSrlComponent> getRecursiveByClassExact(
			Class<? extends AbstractSrlComponent> clazz) {
		List<AbstractSrlComponent> res = new ArrayList<AbstractSrlComponent>();

		for (AbstractSrlComponent sub : contents) {
			if (clazz.equals(sub.getClass()))
				res.add(sub);
			if (sub instanceof AbstractSrlContainer)
				res.addAll(((AbstractSrlContainer) sub).getRecursiveByClassExact(clazz));
		}

		return res;
	}

	/**
	 * Recursively descend through the container and its contents.
	 * 
	 * @param clazz
	 * @return
	 */
	public final List<? extends AbstractSrlComponent> getRecursiveByClassAssignable(
			Class<? extends AbstractSrlComponent> clazz) {
		List<AbstractSrlComponent> res = new ArrayList<AbstractSrlComponent>();

		for (AbstractSrlComponent sub : contents) {
			if (clazz.isInstance(sub))
				res.add(sub);
			if (sub instanceof AbstractSrlContainer)
				res.addAll(((AbstractSrlContainer) sub)
						.getRecursiveByClassAssignable(clazz));
		}

		return res;
	}
	
	public final List<AbstractSrlComponent> getAllComponents(){
		return new ArrayList<AbstractSrlComponent>(contents);
	}
	
	/**
	 * Get a recursive list of all subcomponents;
	 * 
	 * @return
	 */
	public final List<AbstractSrlComponent> getRecursiveSubcomponents() {
		List<AbstractSrlComponent> res = new ArrayList<AbstractSrlComponent>();
		for (AbstractSrlComponent sub : contents) {
			res.add(sub);
			if (sub instanceof AbstractSrlContainer)
				res.addAll(((AbstractSrlContainer) sub).getRecursiveSubcomponents());
		}
		return res;
	}

	/**
	 * Check if the given component is contained by this container or any
	 * container it contains.
	 * 
	 * @param component
	 * @return
	 */
	public final boolean containsRecursive(AbstractSrlComponent component) {
		return getRecursiveSubcomponents().contains(component);
	}

	/**
	 * Recursively descend through the container and its contents.
	 * 
	 * @param clazz
	 * @return
	 */
	public final List<? extends AbstractSrlComponent> getRecursiveLeavesByClassExact(
			Class<? extends AbstractSrlComponent> clazz) {
		List<AbstractSrlComponent> res = new ArrayList<AbstractSrlComponent>();

		for (AbstractSrlComponent sub : contents) {
			if (sub instanceof AbstractSrlContainer) {
				List<? extends AbstractSrlComponent> toAdd = ((AbstractSrlContainer) sub)
						.getRecursiveByClassExact(clazz);
				res.addAll(toAdd);

				if (toAdd.size() > 0)
					continue;
			}

			if (clazz.equals(sub.getClass())) {
				res.add(sub);
			}
		}

		return res;
	}

	/**
	 * Recursively descend through the container and its contents.
	 * 
	 * @param clazz
	 * @return
	 */
	public final List<? extends AbstractSrlComponent> getRecursiveLeavesByClassAssignable(
			Class<? extends AbstractSrlComponent> clazz) {
		List<AbstractSrlComponent> res = new ArrayList<AbstractSrlComponent>();

		for (AbstractSrlComponent sub : contents) {
			if (sub instanceof AbstractSrlContainer) {
				List<? extends AbstractSrlComponent> toAdd = ((AbstractSrlContainer) sub)
						.getRecursiveLeavesByClassAssignable(clazz);
				res.addAll(toAdd);

				if (toAdd.size() > 0)
					continue; // if nothing was added, then check sub
			}

			if (clazz.isInstance(sub)) {
				res.add(sub);
			}
		}

		return res;
	}

    /**
     * Returns the list of strokes in the shape, as well as all strokes in any
     * subshapes.
     *
     * @return the strokes used in this shape and all subshapes.
     */
	/**
	 * Get a recursive list of the strokes.
	 * 
	 * @return
	 */
	public final List<SrlStroke> getRecursiveStrokes() {
		return (List<SrlStroke>) getRecursiveByClassAssignable(SrlStroke.class);
	}

    /**
     * Returns the list of parent strokes in the shape, as well as all parent
     * strokes in any subshapes down to a given {@code level}. A parent stroke
     * is either a stroke with no substrokes, or it is found using the
     * {@link SrlStroke#getParentStroke()}.
     *
     * @return the parent strokes used in this shape and all subshapes.
     */
	/**
	 * Get a recursive list of the parent strokes.
	 * 
	 * if stroke.parent == null, use stroke instead
	 * 
	 * @return
	 */
	public final List<SrlStroke> getRecursiveParentStrokes() {
		ArrayList<SrlStroke> res = new ArrayList<SrlStroke>();

		for (SrlStroke s : getRecursiveStrokes())
			res.add((s.getParentStroke() != null) ? s.getParentStroke() : s);

		return res;
	}

	/**
	 * Get a recursive list of the leaf-shapes (ie shapes that don't contain
	 * more shapes)
	 * 
	 * @return
	 */
	public final List<SrlShape> getRecursiveShapes() {
		ArrayList<SrlShape> res = new ArrayList<SrlShape>();

		for (AbstractSrlComponent s : getRecursiveLeavesByClassAssignable(SrlShape.class))
			res.add((SrlShape) s);

		return res;
	}

	/**
	 * Get a list of all points recursively contained
	 * 
	 * @return
	 */
	public final List<SrlPoint> getPoints() {
		List<SrlPoint> res = new ArrayList<SrlPoint>();

		for (SrlStroke s : getRecursiveStrokes())
			res.addAll(s.getPoints());

		return res;
	}


    /**
     * Gets the list of the ISrlStroke for the IShape. If you modify any strokes in
     * the list, you need to {@link #flagExternalUpdate()} so this object knows
     * it needs to update cached values.
     * <p>
     * The list of strokes returned is only the list of strokes that are present
     * at this level of recognition. That is, this list will NOT contain any
     * strokes in the subshapes of this shape. To get these strokes, you'll have
     * to traverse the subshape tree recursively and gather all the strokes.
     *
     * @return a list of the strokes in this shape.
     */
	/**
	 * Get a list of the strokes in this AbstractSrlContainer
	 * TODO:combine docs
	 * @return
	 */
	public final List<SrlStroke> getStrokes() {
		ArrayList<SrlStroke> res = new ArrayList<SrlStroke>();

		for (AbstractSrlComponent s : getByClassAssignable(SrlStroke.class))
			res.add((SrlStroke) s);

		return res;
	}

    /**
     * Get the number of strokes in the sketch
     *
     * @return The number of strokes in the sketch
     */
	public final int getNumStrokes() {
		int i = 0;

		for (AbstractSrlComponent s : getByClassAssignable(SrlStroke.class))
			++i;

		return i;
	}

	/**
	 * Set the list of strokes in this AbstractSrlContainer
	 *
	 * @param strokes
	 */
	public final void setStrokes(List<? extends SrlStroke> strokes) {
		removeAll(getByClassAssignable(SrlStroke.class));
		addAll(strokes);
	}

    /**
     * Get the first stroke in the shape, using {@link List#get(int)} with an
     * index of 0. Throws an exception if there are no strokes. You need to call
     * {@link #flagExternalUpdate()} if you update the stroke.
     *
     * @return the first stroke.
     */
	/**
	 * Return the first stroke in this AbstractSrlContainer
	 *
	 * @return
	 */
	public final SrlStroke getFirstStroke() {
		List<SrlStroke> strokes = getStrokes();
		return (strokes == null || strokes.size() == 0) ? null : strokes.get(0);
	}

    /**
     * Get the last stroke in the shape, using {@link List#get(int)} with an
     * index of size-1. Throws an exception if there are no strokes. You need to
     * call {@link #flagExternalUpdate()} if you update the stroke.
     *
     * @return the last stroke.
     */
	/**
	 * Return the last stroke in this AbstractSrlContainer
	 * 
	 * @return
	 */
	public final SrlStroke getLastStroke() {
		List<SrlStroke> strokes = getStrokes();
		return (strokes == null || strokes.size() == 0) ? null : strokes
				.get(strokes.size() - 1);
	}

	/**
	 * Get a list of the subshapes in this AbstractSrlContainer
	 * 
	 * @return
	 */
	public final List<SrlShape> getShapes() {
		ArrayList<SrlShape> res = new ArrayList<SrlShape>();

		for (AbstractSrlComponent s : getByClassAssignable(SrlShape.class))
			res.add((SrlShape) s);

		return res;
	}

	/**
	 * Set the list of subshapes in this AbstractSrlContainer
	 * 
	 * @param subshapes
	 */
	public final void setShapes(List<? extends SrlShape> subshapes) {
		removeAll(getByClassAssignable(SrlShape.class));
		addAll(subshapes);
	}

	/**
	 * Get the ith shape
	 * 
	 * @param i
	 */
	public final SrlShape getShape(int i) {
		return getShapes().get(i);
	}

    /**
     * Returns the stroke in a shape, found by index in the shape's stroke list.
     * If you modify the stroke, you need to {@link #flagExternalUpdate()} so
     * this object knows it needs to update cached value.
     *
     * @param index
     *            the position of the stroke in the Shape's stroke list.
     * @return a Stroke (ISrlStroke).
     *
     */
	/**
	 * get the ith stroke
	 * 
	 * @param i
	 * @return
	 */
	public final SrlStroke getStroke(int i) {
		return getStrokes().get(i);
	}

	/**
	 * Find a component by ID
	 * 
	 * @param id
	 * @return
	 */
	public final AbstractSrlComponent get(UUID id) {
		return get(id, false);
	}

	/**
	 * Find a component by ID
	 * 
	 * @param id
	 *            ID to search for
	 * @param searchRecursive
	 *            look inside other containers?
	 * @return
	 */
	public final AbstractSrlComponent get(UUID id, boolean searchRecursive) {
		for (AbstractSrlComponent comp : contents) {
			if (comp.getId().equals(id))
				return comp;
			if (searchRecursive && comp instanceof AbstractSrlContainer) {
				AbstractSrlComponent maybe = ((AbstractSrlContainer) comp).get(id, true);
				if (maybe != null)
					return maybe;
			}
		}
		return null;
	}

	/**
	 * Get the ith component.
	 * 
	 * @param i
	 * @return
	 */
	public final AbstractSrlComponent get(int i) {
		return contents.get(i);
	}

    /**
     * Get the stroke in the shape that has the given UUID. If there is no such
     * stroke, return null.
     * <p>
     * See the note about the list of strokes at {@link #getStrokes()}.
     *
     * @see UUID#equals(Object)
     * @param strokeId
     *            the UUID of the stroke you wish to get from this shape.
     * @return the stroke that has the given UUID (using equals()), or null if
     *         there is no such stroke in this shape.
     */
	/**
	 * Find a Stroke by ID
	 * 
	 * @param id
	 * @return
	 */
	public final SrlStroke getStroke(UUID id) {
		for (AbstractSrlComponent comp : contents)
			if (comp instanceof SrlStroke && comp.getId().equals(id))
				return (SrlStroke) comp;

		return null;
	}

	/**
	 * Find a Shape by ID
	 * 
	 * @param id
	 * @return
	 */
	public final SrlShape getShape(UUID id) {
		for (AbstractSrlComponent comp : contents)
			if (comp instanceof SrlShape && comp.getId().equals(id))
				return (SrlShape) comp;

		return null;
	}

	/**
	 * Get the number of components;
	 * 
	 * @return
	 */
	public final int getComponentCount() {
		return contents.size();
	}


	/**
	 * Checks if this container contains a component
	 * 
	 * @param component
	 * @return
	 */
	public final boolean contains(AbstractSrlComponent component) {
		for (AbstractSrlComponent sub : contents)
			if (sub.equals(component))
				return true;

		return false;
	}
	
	public final boolean contains(UUID id, boolean recursive){
		for(AbstractSrlComponent sub : contents){
			if(sub.getId().equals(id))
				return true;
			if(recursive && sub instanceof AbstractSrlContainer)
				if(((AbstractSrlContainer) sub).contains(id, recursive))
					return true;
		}
		return false;
	}

	/**
	 * Checks if this container contains all of the given components
	 * 
	 * @param components
	 * @return
	 */
	public final boolean containsAll(Collection<? extends AbstractSrlComponent> components) {
		for (AbstractSrlComponent component : components) {
			if (!contains(component))
				return false;
		}

		return true;
	}

	/**
	 * Checks if this container contains any of the given components
	 * 
	 * @param components
	 * @return
	 */
	public final boolean containsAny(Collection<? extends AbstractSrlComponent> components) {
		for (AbstractSrlComponent component : components)
			if (contains(component))
				return true;

		return false;
	}

	/**
	 * Clear this container
	 */
	public final void clear() {
		contents.clear();
		flagExternalUpdate();
	}

	/**
	 * Find all SContainers in this container that recursively contain the given
	 * component
	 * 
	 * @param comp
	 * @return
	 */
	public final List<AbstractSrlContainer> getContainersForComponent(AbstractSrlComponent comp) {
		List<AbstractSrlContainer> res = new ArrayList<AbstractSrlContainer>();

		for (AbstractSrlComponent c : contents) {
			if (c instanceof AbstractSrlContainer
					&& ((AbstractSrlContainer) c).containsRecursive(comp))
				res.add((AbstractSrlContainer) c);
		}

		return res;
	}

	/**
	 * Find all SContainers in this container that recursively contain the given
	 * components
	 * 
	 * @param comps
	 * @return
	 */
	public final Set<? extends AbstractSrlContainer> getContainersForComponents(
			Collection<? extends AbstractSrlComponent> comps) {
		Set<AbstractSrlContainer> res = new HashSet<AbstractSrlContainer>();

		for (AbstractSrlComponent c : contents) {
			boolean found = true;
			for (AbstractSrlComponent cc : comps) {
				if (!(c instanceof AbstractSrlContainer && ((AbstractSrlContainer) c)
						.containsRecursive(cc))) {
					found = false;
					break;
				}
			}
			if (found)
				res.add((AbstractSrlContainer) c);
		}

		return res;
	}

	@Override
	protected final void calculateBBox() {
		double minX = Double.POSITIVE_INFINITY;
		double minY = minX;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = maxX;

		for (AbstractSrlComponent c : contents) {
			SrlBoundingBox b = c.getBoundingBox();
			if (b.getLeft() < minX)
				minX = b.getLeft();
			if (b.getRight() > maxX)
				maxX = b.getRight();
			if (b.getBottom() < minY)
				minY = b.getBottom();
			if (b.getTop() > maxY)
				maxY = b.getTop();
		}

		boundingBox = new SrlBoundingBox(minX, minY, maxX, maxY);
	}

	/**
	 * Check if two SContainers equal each other based on the contents
	 * 
	 * @param o
	 * @return
	 */
	public final boolean equalsByContent(AbstractSrlComponent o) {
		if (this == o)
			return true;

		if (!getClass().isAssignableFrom(o.getClass()))
			return false;

		AbstractSrlContainer other = (AbstractSrlContainer) o;

		if (contents.size() != other.contents.size())
			return false;

		ArrayList<AbstractSrlComponent> otherCopy = new ArrayList<AbstractSrlComponent>(
				other.contents);

		for (AbstractSrlComponent comp : contents) {
			boolean found = false;
			Iterator<AbstractSrlComponent> it = otherCopy.iterator();
			while (it.hasNext()) {
				if (comp.equalsByContent(it.next())) {
					it.remove();
					found = true;
					break;
				}
			}

			if (!found)
				return false;
		}

		return true;
	}
	
	public final Iterator<AbstractSrlComponent> iterator() {
		return contents.iterator();
	}

	private synchronized void rebuildTime(){
		timeStart = Long.MAX_VALUE;
		timeEnd = Long.MIN_VALUE;
		for(AbstractSrlComponent comp:this){
			if(comp instanceof ISrlTimePeriod){
				ISrlTimePeriod timed = (ISrlTimePeriod)comp;
				long otherTimeEnd,otherTimeStart;
				otherTimeEnd = timed.getTimeEnd();
				otherTimeStart = timed.getTimeEnd();
				timeStart = Math.min(timeStart, otherTimeStart);
				timeEnd = Math.max(timeEnd, otherTimeEnd);
			}
		}
	}
	@Override
	public final long getTimeStart() {
		if(timeStart == -1)
			rebuildTime();
		return timeStart;
	}

	@Override
	public final long getTimeEnd() {
		if(timeEnd == -1)
			rebuildTime();
		return timeEnd;
	}

	@Override
	public final long getTimeLength() {
		return getTimeEnd() - getTimeStart();
	}
	

	public static Comparator<ISrlTimePeriod> getTimeComparator(){
		return timeComparator;
	}
	
	private static Comparator<ISrlTimePeriod> timeComparator = new Comparator<ISrlTimePeriod>()  {

		@Override
		public int compare(ISrlTimePeriod first, ISrlTimePeriod second) {

			if(first.getTimeStart() < second.getTimeStart())
				return -1;
			else if(first.getTimeStart() > second.getTimeStart())
				return 1;
			else
				return 0;
		}
		
	};
}
