package edu.tamu.srl.object.shape;

import java.util.ArrayList;

/**
 * Stroke data class.
 *
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlStroke extends SrlShape {

    /**
     * Holds the list of points contained within the stroke.
     * <p/>
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
     * @param s the stroke being copied.
     */
    public SrlStroke(final SrlStroke s) {
        super(s);
    }

    /**
     * Adding another point to the stroke.
     *
     * @param point the point being added to the stroke.
     */
    public final void addPoint(final SrlPoint point) {
        flagExternalUpdate();
        add(point);
        point.setName("p" + getPoints().size());
    }

    /**
     * Adds all of the points to the stroke.
     *
     * @param points points to add to the stroke
     */
    public final void addPoints(final ArrayList<SrlPoint> points) {
        flagExternalUpdate();
        getPoints().addAll(points);

    }

    /**
     * Called to calculate the convex hull of the object.
     */
    @Override protected void calculateConvexHull() {

    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.object.shape.SrlObject}.  This cloned object is only a shallow copy.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlStroke(this);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public SrlObject deepClone() {
        return new SrlStroke(this);
    }

    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public boolean equalsByContent(final SrlObject other) {
        final SrlStroke otherstroke = (SrlStroke) other;
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
    @SuppressWarnings("checkstyle:designforextension")
    public void flagExternalUpdate() {
        super.flagExternalUpdate();
        mPoints = null;
    }

    /**
     * Rotates the SComponent from the given x- and y-coordinate.
     *
     * @param radians the number of radians to rotate
     * @param xCenter the x-coordinate to rotate from
     * @param yCenter the y-coordinate to rotate from
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void rotate(final double radians, final double xCenter, final double yCenter) {
        // TODO implement
        throw new UnsupportedOperationException("rotate is not supported");
    }

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xfactor the x-factor
     * @param yfactor the y-factor
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void scale(final double xfactor, final double yfactor) {
        // TODO implement
        throw new UnsupportedOperationException("scale is not supported");
    }

    /**
     * @return all the points in the substrokes as well as this stroke.
     */
    public final ArrayList<SrlPoint> getAllPoints() {
        return super.getPoints();
    }

    /**
     * Returns the first point in the stroke.
     * if the stroke has no points, it returns null.
     *
     * @return first point in the stroke
     */
    @SuppressWarnings("checkstyle:designforextension")
    public final SrlPoint getFirstPoint() {
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
    @SuppressWarnings("checkstyle:designforextension")
    public final SrlPoint getLastPoint() {
        if (getPoints().size() == 0) {
            return null;
        }
        return getPoints().get(getPoints().size() - 1);
    }

    /**
     * Gets the number of points in the stroke.
     *
     * @return number of points in the stroke
     */
    public final int getNumPoints() {
        return getPoints().size();
    }

    /**
     * Get the i'th point in the stroke.
     * The first point has index i = 0
     *
     * @param i the index of the stroke
     * @return The point at index i.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public final SrlPoint getPoint(final int i) {
        if (i >= getPoints().size()) {
            return null;
        }
        return getPoints().get(i);
    }

    /**
     * Gets the immediate points in the stroke since this may also
     * contain substrokes, we don't want those points to be included in
     * this. (getAllPoints() also gets the sub points in any substrokes).
     * @return a list of all of the points in the immediate stroke.
     */
    @SuppressWarnings("checkstyle:designforextension")
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