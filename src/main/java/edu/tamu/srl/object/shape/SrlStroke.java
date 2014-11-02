package edu.tamu.srl.object.shape;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Set;

/**
 * Stroke data class.
 *
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlStroke extends SrlShape {

    /**
     * Holds the list of points contained within the stroke.
     *
     * Note that the actual
     */
    private transient ArrayList<SrlPoint> mPoints = null;

    /**
     * General constructor added because it is needed by superclasses.
     */
    public SrlStroke() {
        super();
    }

    /**
     * @param resampled a resampled list of points stored for the stroke.
     */
    public SrlStroke(final ArrayList<SrlPoint> resampled) {
        super();
        for (SrlPoint p : resampled) {
            addPoint(p);
        }
    }

    /**
     * Constructor setting the initial point in the stroke.
     *
     * @param startPoint The starting point of the stroke.
     */
    public SrlStroke(final SrlPoint startPoint) {
        add(startPoint);
        startPoint.setName("p" + getPoints().size());
    }

    /**
     * This is a constructor to be used in place of the clone method.
     *
     * @param s
     */
    public SrlStroke(final SrlStroke s) {
        super(s);
    }

    /**
     * Adding another point to the stroke
     *
     * @param point
     */
    public void addPoint(final SrlPoint point) {
        flagExternalUpdate();
        add(point);
        point.setName("p" + getPoints().size());
    }

    /**
     * Adds all of the points to the stroke
     *
     * @param points points to add to the stroke
     */
    public void addPoints(final ArrayList<SrlPoint> points) {
        flagExternalUpdate();
        getPoints().addAll(points);

    }

    @Override
    protected void applyTransform(AffineTransform xform, Set<SrlObject> xformed) {
        // TODO Auto-generated method stub

    }

    /**
     * Called to calculate the convex hull of the object.
     */
    @Override protected void calculateConvexHull() {

    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.object.shape.SrlObject}.  This cloned object is only a shallow copy.
     */
    @Override public Object clone() {
        return null;
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @Override public SrlObject deepClone() {
        return null;
    }

    @Override
    public boolean equalsByContent(SrlObject other) {
        SrlStroke otherstroke = (SrlStroke) other;
        int i = 0;
        for (SrlPoint p : getPoints()) {
            if (!p.equalsByContent(otherstroke.getPoint(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Flags an external update.
     */
    public void flagExternalUpdate() {
        super.flagExternalUpdate();
        mPoints = null;
    }

    /**
     * Gets all the points in the substrokes as well as this stroke
     *
     * @return
     */
    public ArrayList<SrlPoint> getAllPoints() {
        return super.getPoints();
    }

    /**
     * Returns the first point in the stroke.
     * if the stroke has no points, it returns null.
     *
     * @return first point in the stroke
     */
    public SrlPoint getFirstPoint() {
        if (getPoints().size() == 0) {
            return null;
        }
        return getPoints().get(0);
    }

    /**
     * Returns the last point in the stroke
     * If the stroke has no points, it returns null.
     *
     * @return last point in the stroke.
     */
    public SrlPoint getLastPoint() {
        if (getPoints().size() == 0) {
            return null;
        }
        return getPoints().get(getPoints().size() - 1);
    }

    /**
     * Gets the number of points in the stroke
     *
     * @return number of points in the stroke
     */
    public int getNumPoints() {
        return getPoints().size();
    }

    /**
     * Get the i'th point in the stroke
     * The first point has index i = 0
     *
     * @param i the index of the stroke
     * @return the point at index i
     */
    public SrlPoint getPoint(final int i) {
        if (i >= getPoints().size()) {
            return null;
        }
        return getPoints().get(i);
    }

    /**
     * Gets the immediate points in the stroke since this may also
     * contain substrokes, we don't want those points to be included in
     * this. (getAllPoints() also gets the sub points in any substrokes)
     */
    @Override
    public ArrayList<SrlPoint> getPoints() {
        if (mPoints != null) {
            return mPoints;
        }
        final ArrayList<SrlPoint> allPoints = new ArrayList<SrlPoint>();
        for (SrlObject o : getSubShapes()) {
            if (o instanceof SrlPoint) {
                allPoints.add((SrlPoint) o);
            }
        }
        mPoints = allPoints;
        return allPoints;
    }
}
