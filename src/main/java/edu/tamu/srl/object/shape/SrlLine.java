package edu.tamu.srl.object.shape;

import edu.tamu.srl.processing.LineProcessing;
import edu.tamu.srl.settings.SrlInitialSettings;

import java.awt.geom.AffineTransform;
import java.util.Set;

/**
 * Line data class
 *
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlLine extends SrlInterpretedShape {

    /**
     * Starting value of the line
     */
    private SrlPoint m_p1;
    /**
     * Ending value of the line
     */
    private SrlPoint m_p2;

    /**
     * Constructor that takes two srlpoints
     *
     * @param p1 start point
     * @param p2 end point
     */
    public SrlLine(SrlPoint p1, SrlPoint p2) {
        setColor(SrlInitialSettings.InitialLineColor);
        m_p1 = p1;
        m_p2 = p2;
        addInterpretation("line", 1, 1);
    }

    /**
     * Create a new line from the end point values
     *
     * @param x1 x value of endpoint 1
     * @param y1 y value of endpoint 1
     * @param x2 x value of endpoint 2
     * @param y2 y value of endpoint 2
     */
    public SrlLine(double x1, double y1, double x2, double y2) {
        this(new SrlPoint(x1, y1), new SrlPoint(x2, y2));
    }

    /**
     * Get the starting point of the line
     *
     * @return starting point of the line
     */
    public SrlPoint getP1() {
        return m_p1;
    }

    /**
     * Set the starting point of the line
     *
     * @param p1 starting point of the line
     */
    public void setP1(SrlPoint p1) {
        m_p1 = p1;
    }

    /**
     * Get the ending point of the line
     *
     * @return ending point of the line
     */
    public SrlPoint getP2() {
        return m_p2;
    }

    /**
     * Set the ending point of the line
     *
     * @param p2 ending point of the line
     */
    public void setP2(SrlPoint p2) {
        m_p2 = p2;
    }

    /**
     * Returns the slope of the line. Note that
     * if this line is vertical, this will cause an error.
     * This is the m in the line equation in y = mx + b.
     *
     * @return slope of the line
     */
    public double getSlope() {
        return (getP2().getY() - getP1().getY()) /
                (getP2().getX() - getP1().getX());
    }

    /**
     * Returns the y-intercept of the line.  (Where the
     * line crosses the y axis.) This is the b in
     * the equation for a line y = mx + b. Note that this will
     * cause an error if this line is vertical.
     *
     * @return the y-intercept
     */
    public double getYIntercept() {
        return getP1().getY() - getSlope() * getP1().getX();
    }

    public SrlPoint getIntersection(SrlLine l) {
        double[] iPoint = LineProcessing.getIntersection(
                getP1().getX(), getP1().getY(),
                getP2().getX(), getP2().getY(),
                l.getP1().getX(), l.getP1().getY(),
                l.getP2().getX(), l.getP2().getY());
        return new SrlPoint(iPoint[0], iPoint[1]);
    }

    /**
     * Compute the distance from this line to
     * the closest point on the line.
     *
     * @param p the point to compare
     * @return the distance
     */
    public double distance(SrlPoint p) {
        SrlLine perp = getPerpendicularLine(p);
        SrlPoint intersectionPoint = getIntersection(perp);
        if (overBoundingBox(intersectionPoint)) {
            return intersectionPoint.distance(p);
        }
        return Math.min(p.distance(getP1()), p.distance(getP2()));
    }

    public double distance(SrlLine l) {
        SrlPoint intersectionPoint;
        if (isParallel(l, .001)) {
            intersectionPoint = l.getP1();
        } else {
            intersectionPoint = getIntersection(l);
        }
        if (intersectionPoint == null) {
            System.err.println("intersection point is null!");
            return 0;
        }
        double di = Math.max(distance(intersectionPoint), l.distance(intersectionPoint));
        double d1 = distance(l.getP1());
        double d2 = distance(l.getP2());
        return Math.min(di, Math.min(d1, d2));
    }

    /**
     * Is this point on the bounding box of the point
     *
     * @param p the point on the bounding box
     * @return true if the point and line share the bounding box
     */
    public boolean overBoundingBox(SrlPoint p) {
        if (p.getX() > getP1().getX() && p.getX() > getP2().getX()) {
            return false;
        }
        if (p.getX() < getP1().getX() && p.getX() < getP2().getX()) {
            return false;
        }
        if (p.getY() > getP1().getY() && p.getY() > getP2().getY()) {
            return false;
        }
        if (p.getY() < getP1().getY() && p.getY() < getP2().getY()) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the lines are parallel
     * within given threshold. If threshold is 0,
     * the lines have to be perfectly parallel.
     * If the threshold is 1, all lines are parallel
     * If the threshold is .5, lines with a difference of  less than 45
     * degrees are parallel.
     *
     * @param line
     * @param percent_threshold
     * @return true if parallel
     */
    public boolean isParallel(SrlLine line, double percent_threshold) {
        double threshold = percent_threshold * Math.PI / 2;
        double diff = getAngleInRadians() - line.getAngleInRadians();
        while (diff < 0) {
            diff += Math.PI;
        }
        while (diff > Math.PI) {
            diff -= Math.PI;
        }
        if (diff <= threshold) {
            return true;
        }
        if (diff >= Math.PI - threshold) {
            return true;
        }
        return false;
    }

    /**
     * Returns the angle of the line in degrees
     *
     * @return angle in degrees from 1-360
     */
    public double getAngleInDegrees() {
        double angle = 360 - getAngleInRadians() * 180 / Math.PI;
        while (angle < 0) {
            angle += 360;
        }
        while (angle >= 360) {
            angle -= 360;
        }
        return angle;
    }

    /**
     * returns the angle in degrees within 180
     *
     * @return angle in degrees
     */
    public double getAngleInDegreesUndirected() {
        double angle = 360 - getAngleInRadians() * 180 / Math.PI;
        while (angle < 0) {
            angle += 180;
        }
        while (angle >= 180) {
            angle -= 180;
        }
        return angle;
    }

    /**
     * returns angle in radians
     *
     * @return angle in radians
     */
    public double getAngleInRadians() {
        return Math.atan2(getP2().getY() - getP1().getY(),
                getP2().getX() - getP1().getX());
    }

    /**
     * Do the two lines intersect?
     *
     * @param line second line that might intersect with this one
     * @return yes if they intersect
     */
    public boolean intersects(SrlLine line) {
        //System.debug.println("distance between lines is " + distance(line));
        if (distance(line) < .1) {
            return true;
        }
        return false;
    }

    /**
     * This function creates a line segment of the same
     * length as this line but is perpendicular to this line
     * and has an endpoint at point p.
     *
     * @param p endpoint of perpendicular line
     * @return the perpendicular line segment.
     */
    public SrlLine getPerpendicularLine(SrlPoint p) {
        double len = getLength();
        double[] perpline = LineProcessing.getPerpendicularLine(p.getX(), p.getY(), len,
                getP1().getX(), getP1().getY(), getP2().getX(), getP2().getY());
        return new SrlLine(perpline[0], perpline[1], perpline[2], perpline[3]);
    }

    /**
     * Returns the euclidean length of the line.  The area of the line
     * is assumed to be the length times the width, where the width is 1.
     * return area of line, which equals the euclidean length
     */
    public double getArea() {
        return getLength();
    }

    /**
     * Returns the euclidean length of the line between the two endpoints
     *
     * @return the length
     */
    public double getLength() {
        return m_p1.distance(m_p2);
    }

    /**
     * Returns a new line in the opposite direction.
     * Note that the points inside the line are not cloned,
     * but rather the same original points.
     *
     * @return a line with the endpoints swapped.
     */
    public SrlLine getFlippedLine() {
        return new SrlLine(getP1(), getP2());
    }

    /**
     * Make this line to be permanently going in the other direction.
     * Note that the timing of the points will not be changed.
     * This just swaps the first and the last point.
     */
    public void flip() {
        SrlPoint temp = m_p1;
        m_p1 = m_p2;
        m_p2 = temp;
    }

    @Override
    /**
     * Returns the x value of the leftmost point.
     * return min x of line
     */
    public double getMinX() {
        return Math.min(getP1().getX(), getP2().getX());
    }

    @Override
    /**
     * Returns the y value of the lower point
     * return min y of line
     */
    public double getMinY() {
        return Math.min(getP1().getY(), getP2().getY());
    }

    @Override
    /**
     * Returns the x value of the rightmost point
     * return max x of line
     */
    public double getMaxX() {
        return Math.max(getP1().getX(), getP2().getX());
    }

    @Override
    /**
     * Returns the y value of the highest point
     * return max y of line
     */
    public double getMaxY() {
        return Math.max(getP1().getY(), getP2().getY());
    }

    /**
     * Returns a string of [x1,y1,t1],[x2,y2,t2]
     *
     * @return a string of [x1,y1,t1],[x2,y2,t2]
     */
    public String toString() {
        return getP1().toString() + "," + getP2().toString();
    }

    /**
     * Translates end points and any subshapes
     *
     * @param x x amount to translate
     * @param y y amount to translate
     */
    public void translate(double x, double y) {
        m_p1.translate(x, y);
        m_p2.translate(x, y);
        translateSubShapes(x, y);
    }

    @Override
    public boolean equalsByContent(SrlObject other) {
        if (!(other instanceof SrlLine)) {
            return false;
        }
        SrlLine otherline = (SrlLine) other;
        if (!getP1().equalsByContent(otherline.getP1())) {
            return false;
        }
        if (!getP2().equalsByContent(otherline.getP2())) {
            return false;
        }
        return true;
    }

    @Override
    protected void applyTransform(AffineTransform xform, Set<SrlObject> xformed) {
        // TODO Auto-generated method stub

    }

    /**
     * Sets the bounding box around the line
     */
    @Override
    protected void calculateBBox() {
        mBoundingBox = new SrlRectangle(getP1(), getP2());
    }
}
