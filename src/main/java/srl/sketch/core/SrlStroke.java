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

import srl.sketch.core.comparators.SrlTimePeriodComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * SrlStroke class for a stroke containing {@link SrlPoint}s.
 *
 * @author awolin
 * @author gigemjt
 */
public class SrlStroke extends AbstractSrlComponent implements ISrlClassifiable,
Iterable<SrlPoint>, ISrlTimePeriod, Comparable<ISrlTimePeriod> {

    private static SrlTimePeriodComparator comparator = new SrlTimePeriodComparator();

	protected List<SrlPoint> points;

	protected List<SrlInterpretation> interpretations;

	protected List<SrlSegmentation> segmentations;

	protected SrlStroke parent;

	private transient long timeStart = -1L;
	private transient long timeEnd = -1L;

	public SrlStroke() {
		points = new ArrayList<SrlPoint>();
		interpretations = new ArrayList<SrlInterpretation>();
		segmentations = new ArrayList<SrlSegmentation>();
	}

	public SrlStroke(List<SrlPoint> points) {
		this();
		this.points = points;
	}

	public SrlStroke(SrlStroke copyFrom) {
		super(copyFrom);

		points = new ArrayList<SrlPoint>();
		interpretations = new ArrayList<SrlInterpretation>();
		segmentations = new ArrayList<SrlSegmentation>();

        if(copyFrom.getParentStroke()!=null)
            parent = copyFrom.getParentStroke().clone();

		for (SrlPoint p : copyFrom.points)
			points.add(p.clone());
		for (SrlInterpretation i : copyFrom.interpretations)
			interpretations.add(i.clone());
		for (SrlSegmentation s : copyFrom.segmentations)
			segmentations.add(s.clone());
	}

	@Override
	public void applyTransform(AffineTransform xform, Set<ISrlTransformable> xformed) {
		if (xformed.contains(this))
			return;
		xformed.add(this);

		for (SrlPoint p : points)
			p.applyTransform(xform, xformed);

		flagExternalUpdate();
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
     * Add a point to the list of point. This method will update the cached
     * values, if needed, with a O(1) iterative update. This method DOES NOT
     * ensure the points are put in temporal order. It is assumed you would only
     * add points in ascending temporal order. If you are not, you need to do
     * your own book keeping.
     *
     * @param p
     *            point to add.
     *
     * @throws NullPointerException
     *             if the point argument is null.
     */
	public void addPoint(SrlPoint p) {
		points.add(p);
		flagExternalUpdate();
	}

	/**
	 * Append a list of points onto this stroke
	 * @param points
	 */
	public void addPoints(List<SrlPoint> points){
		this.points.addAll(points);
	}

    /**
     * Gets the list of SrlPoints of the SrlStroke. If you use this method to update
     * points directly, you should also call {@link #flagExternalUpdate()} so
     * the object knows it needs to update cached values. If you do not call
     * {@link #flagExternalUpdate()}, the stroke may NOT assume the point list
     * has changed and any cached values (e.g. bounding box) you get MAY BE
     * INACCURATE.
     *
     * @return a List of IPoints.
     */
	public List<SrlPoint> getPoints() {
		return points;
	}

    /**
     * Get the minimum inter-point distance (for 0 <= i < points.size()-1,
     * min(points(i).distance(points(i+1)))) for this stroke.
     *
     * @return the minimum inter-point distance for the stroke.
     */
    public double getMinInterPointDistance() {
        return 0;
    }

    /**
     * Sets the list of IPoints of the ISrlStroke. You should sort the list of
     * points, or ensure they are in ascending temporal order, BEFORE you use
     * this method. The functionality and definition of time-related methods
     * depends on the list of points in a stroke being in ascending temporal
     * order.
     *
     * @param points
     *            the list of points to set for the ISrlStroke.
     *
     * @throws NullPointerException
     *             if the points argument is null.
     */
	public void setPoints(List<SrlPoint> points) {
		this.points = points;
		flagExternalUpdate();
	}

	/**
	 * Get the distance between endpoints
	 * @return
	 */
	public double getLength()
	{
		if (points.size() == 0) return 0;
		return getFirstPoint().distance(getLastPoint());
	}

    /**
     * Gets the length of the path, that is, the sum of the Euclidean distances
     * between all pairs of consecutive points. This value is cached and updated
     * iteratively in O(1) time with each call to {@link #addPoint(SrlPoint)}.
     * However, if you {@link #getPoints()} or {@link #getPoint(int)} and modify
     * anything, you will need to {@link #flagExternalUpdate()} so we know the
     * value needs to be recomputed at a cost of O(n).
     *
     * @return the length of the stroke&#39;s path.
     */
	public double getPathLength() {
		double res = 0.0;

		for (int i = 1; i < points.size(); ++i)
			res += points.get(i - 1).distance(points.get(i));

		return res;
	}

    /**
     * Sets the list of segmentations (corner finding interpretations) for the
     * stroke.
     *
     * @param segmentations
     *            list of possible segmentations.
     *
     * @throws NullPointerException
     *             if the segmentation argument is null.
     */
    public void setSegmentations(List<SrlSegmentation> segmentations) throws NullPointerException {
        this.segmentations = segmentations;
    }

    /**
     * Remove a segmentation from the list of possible segmentations
     *
     * @param segmentation
     *            segmentation to remove
     * @return true if the segmentation was removed, false otherwise.
     *
     * @throws NullPointerException
     *             if the segmentation argument is null.
     */
    public boolean removeSegmentation(SrlSegmentation segmentation) throws NullPointerException {
        return this.segmentations.remove(segmentation);
    }

    /**
     * Get the number of points in the stroke.
     *
     * @return the number of points within the stroke.
     */
	public int getNumPoints() {
		return points.size();
	}

    /**
     * Get the point at the specified index. This is a pass-through call to
     * {@link List#get(int)}.
     *
     * @param i
     *            the index of point to get.
     * @return the point at the specified index.
     */
	public SrlPoint getPoint(int i) {
		return points.get(i);
	}

    /**
     * Get the first point in this stroke. This is a pass-through call to
     * {@link List#get(int)} with index of 0. Make sure there is a first point
     * first, or you will probably get an exception.
     *
     * @return the first point, if it exists, or exception.
     */
	public SrlPoint getFirstPoint() {
		if (points.size() == 0)
			return null;
		return points.get(0);
	}

    /**
     * Get the last point in this stroke. This is a pass-through call to
     * {@link List#get(int)} with index of {@link #getNumPoints()}-1. Make sure
     * there is a last point (non-empty stroke), or you will probably get an
     * exception.
     *
     * @return the last point, if it exists, or exception.
     */
	public SrlPoint getLastPoint() {
		if (points.size() == 0)
			return null;
		return points.get(points.size() - 1);
	}

    /**
     * Gets the creation time of the stroke. This creation time is defined as
     * the time of the last SrlPoint in the stroke. UPDATING THE STROKE THROUGH
     * NON-APPEND METHODS CAN BREAK THIS FUNCTIONALITY.
     *
     * @return the creation time of the stroke.
     */
    public long getTime() {
        return this.getTimeEnd();
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
     * Clone an ISrlStroke.
     *
     * @return a new, deep copied ISrlStroke.
     */
	@Override
	public SrlStroke clone() {
		return new SrlStroke(this);
	}

    /**
     * Returns a list of segmentations (corner finding interpretations) for the
     * stroke.
     *
     * @return a list of possible segmentations.
     */
	public List<SrlSegmentation> getSegmentations() {
		return segmentations;
	}

    /**
     * Add a segmentation to the list of possible segmentations. Currently,
     * segmentations are only allowed for strokes that do not have a parent.
     * This prevents recursive segmentations.
     *
     * TODO - Throw a SegmentationNotAddedException TODO - Create this
     * exception...
     *
     * @param seg
     *            segmentation (corner finding) interpretation to add.
     */
	public void addSegmentation(SrlSegmentation seg) {
		segmentations.add(seg);
	}
	
	/**
	 * Add segmentations
	 * @param segs
	 */
	public void addSegmentations(List<SrlSegmentation> segs){
		segmentations.addAll(segs);
	}

    /**
     * Set the parent stroke for this stroke.
     *
     * @param parent
     *            parent stroke.
     */
	public void setParentStroke(SrlStroke parent) {
		this.parent = parent;
	}

    /**
     * Get the parent stroke (if this stroke is a sub-stroke). If not a
     * sub-stroke then parent will be null.
     *
     * @return parent stroke (if it exists).
     */
	public SrlStroke getParentStroke() {
		return parent;
	}

	@Override
	public void setInterpretation(String label, double confidence) {
		setInterpretation(new SrlInterpretation(label, confidence));
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
	 * Adds a point to this stroke where the two segments intersect - if they
	 * intersect
	 * 
	 * @return the index of the newly created point in the getPoints() array.
	 *         Returns -1 if added nothing This method automatically calls
	 *         {@link #flagExternalUpdate()}.
	 */
	public int addPointAtIntersection(SrlStroke stroke)
	throws NullPointerException {
		List<SrlPoint> otherPoints = stroke.getPoints();

		int closestIndexThis = -1;
		int closestIndexOther = -1;

		int returnIndex = -1;
		double[] answer = strokeIntersection(stroke);

		if (answer[0] >= .00001 && answer[0] <= .9999 && answer[1] >= 0
				&& answer[1] <= 1) {

			for (int j = 1; j < otherPoints.size(); j++) {
				SrlPoint otherEnd1 = otherPoints.get(j - 1);
				SrlPoint otherEnd2 = otherPoints.get(j);
				if (otherEnd1.getX() > otherEnd2.getX()) {
					SrlPoint temp = otherEnd1;
					otherEnd1 = otherEnd2;
					otherEnd2 = temp;
				}

				for (int i = 1; i < getPoints().size(); i++) {
					SrlPoint this1 = points.get(i - 1);
					SrlPoint this2 = points.get(i);
					if (this1.getX() > this2.getX()) {
						SrlPoint temp = this1;
						this1 = this2;
						this2 = temp;
					}

					answer = segmentIntersection(this1, this2, otherEnd1,
							otherEnd2);
					if (answer[0] >= 0 && answer[0] <= 1 && answer[1] >= 0
							&& answer[1] <= 1) {
						double x = this2.getX();
						x = x - this1.getX();
						x = x * answer[0];
						x = x + this1.getX();

						// y = ((y1-y2) * distance along line) + y2
						double y = this2.getY();
						y = y - this1.getY();
						y = y * answer[0];
						y = y + this1.getY();

						SrlPoint newPoint = new SrlPoint(x, y);

						if (newPoint.distance(this1) < 1)
							return points.indexOf(this1);
						if (newPoint.distance(this2) < 1)
							return points.indexOf(this2);

						newPoint.setTime(this1.getTime()
								+ (this2.getTime() - this1.getTime()) / 2);// simple
						// average
						points.add(i, newPoint);

						returnIndex = i;

						flagExternalUpdate();

						break;
					}
				}
			}
		} else if (answer[0] >= .00001 && answer[0] <= .9999 && answer[1] >= -1
				&& answer[1] <= 2) {
			SrlPoint otherEnd1 = otherPoints.get(0);
			SrlPoint otherEnd2 = otherPoints.get(otherPoints.size() - 1);
			if (otherEnd1.getX() > otherEnd2.getX()) {
				SrlPoint temp = otherEnd1;
				otherEnd1 = otherEnd2;
				otherEnd2 = temp;
			}

			for (int i = 1; i < getPoints().size(); i++) {
				SrlPoint this1 = points.get(i - 1);
				SrlPoint this2 = points.get(i);
				if (this1.getX() > this2.getX()) {
					SrlPoint temp = this1;
					this1 = this2;
					this2 = temp;
				}

				answer = segmentIntersection(this1, this2, otherEnd1, otherEnd2);
				if (answer[0] >= 0 && answer[0] <= 1) {
					double x = this2.getX();
					x = x - this1.getX();
					x = x * answer[0];
					x = x + this1.getX();

					// y = ((y1-y2) * distance along line) + y2
					double y = this2.getY();
					y = y - this1.getY();
					y = y * answer[0];
					y = y + this1.getY();

					SrlPoint newPoint = new SrlPoint(x, y);

					if (newPoint.distance(this1) < 1)
						return points.indexOf(this1);
					if (newPoint.distance(this2) < 1)
						return points.indexOf(this2);

					newPoint.setTime(this1.getTime()
							+ (this2.getTime() - this1.getTime()) / 2);// simple
					// average
					points.add(i, newPoint);

					returnIndex = i;

					flagExternalUpdate();

					break;
				}
			}
		}

		if (returnIndex == -1) {
			// System.out.println("did not add point");
		}
		flagExternalUpdate();
		return returnIndex;
	}

	/**
	 * Calculate intersection point along both line segments.
	 * 
	 * ua is 0 if the intersection is at alast and 1 if it is at ahere ua is
	 * 0.333 if it is 1/3 the way from alast to ahere.
	 * 
	 * ub acts a lot like ua, except with blast and bhere.
	 * 
	 * @param one
	 *            end point for first line
	 * @param two
	 *            endpoint for first line
	 * @param a
	 *            endpoint for second line
	 * @param b
	 *            endpoint for second line
	 * @return [ua,ub]
	 */
	public static double[] segmentIntersection(SrlPoint one, SrlPoint two, SrlPoint a, SrlPoint b) {

		double x1 = one.getX();
		double x2 = two.getX();
		double x3 = a.getX();
		double x4 = b.getX();
		double y1 = one.getY();
		double y2 = two.getY();
		double y3 = a.getY();
		double y4 = b.getY();

		double denom = ((y4 - y3) * (x2 - x1)) - ((x4 - x3) * (y2 - y1));
		if (Math.abs(denom) < 1e-5)
			return new double[] { Double.MAX_VALUE, Double.MAX_VALUE };
		// parallel (ish) lines

		double ua = ((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3));
		double ub = ((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3));

		return new double[] { ua / denom, ub / denom };
	}

	/**
	 * Calculate intersection point along both line segments.
	 * 
	 * ua is 0 if the intersection is at alast and 1 if it is at ahere ua is
	 * 0.333 if it is 1/3 the way from alast to ahere.
	 * 
	 * ub acts a lot like ua, except with blast and bhere.
	 *
     * @param s
     *            The stroke that the intersection is being created for
     *
	 * one
	 *            end point for first line
	 * two
	 *            endpoint for first line
	 * a
	 *            endpoint for second line
	 * b
	 *            endpoint for second line
	 * @return [ua,ub]
	 */
	public double[] strokeIntersection(SrlStroke s) {
		SrlPoint one = getFirstPoint();
		SrlPoint two = getLastPoint();
		if (one.getX() > two.getX()) {
			SrlPoint temp = one;
			one = two;
			two = temp;
		}

		SrlPoint a = s.getFirstPoint();
		SrlPoint b = s.getLastPoint();
		if (a.getX() > b.getX()) {
			SrlPoint temp = a;
			a = b;
			b = temp;
		}

		return segmentIntersection(one, two, a, b);
	}

	@Override
	protected void calculateBBox() {
		double minX = Double.POSITIVE_INFINITY;
		double minY = minX;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = maxX;

		for (SrlPoint p : points) {
			if (p.x < minX)
				minX = p.x;
			if (p.x > maxX)
				maxX = p.x;
			if (p.y < minY)
				minY = p.y;
			if (p.y > maxY)
				maxY = p.y;
		}

		boundingBox = new SrlBoundingBox(minX, minY, maxX, maxY);
	}

	public boolean equalsByContent(AbstractSrlComponent other) {
		if (this == other)
			return true;

		if (!(other instanceof SrlStroke))
			return false;

		SrlStroke o = (SrlStroke) other;
		if (points.size() != o.points.size())
			return false;

		for (int i = 0; i < points.size(); ++i) {
			if (!points.get(i).equalsByContent(o.points.get(i)))
				return false;
		}
		return true;
	}

	@Override
	public Iterator<SrlPoint> iterator() {
		return points.iterator();
	}

	private synchronized void rebuildTime(){
		timeStart = Long.MAX_VALUE;
		timeEnd = Long.MIN_VALUE;
		for(SrlPoint comp : points){
			long otherTimeEnd,otherTimeStart;
			otherTimeEnd = comp.getTime();
			otherTimeStart = comp.getTime();
			timeStart = Math.min(timeStart, otherTimeStart);
			timeEnd = Math.max(timeEnd, otherTimeEnd);
		}
	}
	
	@Override
	public long getTimeEnd() {
		if (timeEnd == -1L) {
			rebuildTime();
		}

		return timeEnd;
	}
	/**
	 * Get the starting time of this container. This is the minimum starting time of all contained SComponents and SContainers.
	 * @return
	 */
	public long getTimeStart(){
		if(timeStart == -1L){
			rebuildTime();
		}
		return timeStart;
	}
	/**
	 * Get the difference in time between the start time and end time;
	 * @return the difference in time between the start time and end time
	 */
	public long getTimeLength(){
		return Math.abs(getTimeEnd()-getTimeStart());
	}

	public String toString() {
		return super.toString() + " " + id;
	}

	/**
	 * Find the index of the point with the given id.
	 * 
	 * @param id
	 * @return the index of the point with the given id or -1;
	 */
	public int indexOf(UUID id) {
		for (int i = 0; i < points.size(); ++i)
			if (points.get(i).id.equals(id))
				return i;
		return -1;
	}

	@Override
	public int compareTo(ISrlTimePeriod other) {
		return comparator.compare(this, other);
	}
}
