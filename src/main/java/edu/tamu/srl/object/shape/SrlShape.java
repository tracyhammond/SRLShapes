package edu.tamu.srl.object.shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * Shape data class.
 *
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public abstract class SrlShape extends SrlObject implements Iterable<SrlObject> {

    /**
     * Was this object made up from a collection of subObjects?
     * If so they are in this list.
     * This list usually gets filled in through recognition.
     * This list can be examined hierarchically.
     * e.g., an arrow might have three lines inside, and each line might have a stroke.
     */
    private ArrayList<SrlObject> mSubShapes = new ArrayList<SrlObject>();

    /**
     * Copy Constructor.  Performs a deep copy.
     *
     * @param s the shape being copied.
     */
    public SrlShape(final SrlShape s) {
        this(s, true);
    }

    /**
     * Copy Constructor.
     *
     * @param s the shape being copied.
     * @param deep true if a deep copy is being performed.  Otherwise a shallow copy is performed.
     */
    public SrlShape(final SrlShape s, final boolean deep) {
        super(s);
        if (deep) {
            for (SrlObject sub : s.getSubShapes()) {
                if (sub instanceof SrlPoint) {
                    this.add(new SrlPoint((SrlPoint) sub));
                } else if (sub instanceof SrlStroke) {
                    this.add(new SrlStroke((SrlStroke) sub));
                }
            }
        } else {
            // shallow copy
            this.addAll(s.getSubShapes());
        }
    }

    /**
     * Empty super constructor must exist for later
     */
    public SrlShape() {
        super();
    }

    public Iterator<SrlObject> iterator() {
        return getSubShapes().iterator();
    }

    /**
     * Adds a subshape to this object at the specified index.
     *
     * @param index        point to add the content
     * @param subcomponent the subshape
     */
    public void addSubShape(final int index, final SrlObject subcomponent) {
        mSubShapes.add(index, subcomponent);
        flagExternalUpdate();
    }

    /**
     * Adds a subshape to this object.
     * This usually happens during recognition, when a new object
     * is made up from one or more objects
     *
     * @param subshape
     */
    public void addSubShape(final SrlObject subShape) {
        mSubShapes.add(subShape);
    }

    /**
     * Adds a subshape to this object.
     * This usually happens during recognition, when a new object
     * is made up from one or more objects
     *
     * @param subshape
     */
    public void add(final SrlObject subShape) {
        flagExternalUpdate();
        mSubShapes.add(subShape);
    }

    /**
     * adds a list of subobjects to this object
     *
     * @param subshapes list of subshapes to add
     */
    public void addSubShapes(final ArrayList<SrlObject> subshapes) {
        mSubShapes.addAll(subshapes);
        flagExternalUpdate();
    }

    /**
     * adds a list of subobjects to this object
     *
     * @param subshapes list of subshapes to add
     */
    public void addAll(final ArrayList<SrlObject> subshapes) {
        mSubShapes.addAll(subshapes);
        flagExternalUpdate();
    }

    /**
     * Clears the container.
     */
    public void clear() {
        mSubShapes.clear();
        flagExternalUpdate();
    }

    /**
     * Checks if this container contains the target component.
     *
     * @param component the target component
     * @return true if this container contains the target component, false
     * otherwise
     */
    public boolean contains(final SrlObject component) {
        for (SrlObject sub : mSubShapes) {
            if (sub.equals(component)) {
                return true;
            } else if (sub instanceof SrlShape) {
                if (((SrlShape) sub).contains(component)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the ith component.
     *
     * @param i the index
     * @return the ith component
     */
    public SrlObject get(final int i) {
        return mSubShapes.get(i);
    }

    /**
     * Gets the number of components.
     *
     * @return the number of components
     */
    public int getComponentCount() {
        return mSubShapes.size();
    }

    /**
     * Gets a list of all of the points that make up this object.
     * It searches recursively to get all of the strokes of this object.
     * If it does not have any strokes, and then returns the points in those strokes.
     *
     * @return The list of points.
     */
    public ArrayList<SrlPoint> getPoints() {
        final ArrayList<SrlPoint> allPoints = new ArrayList<SrlPoint>();
        for (SrlObject o : mSubShapes) {
            if (o instanceof SrlPoint) {
                allPoints.add((SrlPoint) o);
            } else if (o instanceof SrlShape) {
                allPoints.addAll((ArrayList<SrlPoint>) ((SrlShape) o).getPoints());
            }
        }
        return allPoints;
    }

    /**
     * Gets a list of all of the objects that make up this object.
     * This is a recursive search through all of the subobjects.
     * This objects is also included on the list.
     *
     * @return
     */
    public ArrayList<SrlInterpretedShape> getRecursiveSubShapeList() {
        final ArrayList<SrlInterpretedShape> completeList = new ArrayList<SrlInterpretedShape>();
        completeList.add((SrlInterpretedShape) this);
        for (SrlObject o : mSubShapes) {
            if (o instanceof SrlInterpretedShape) {
                SrlInterpretedShape sis = (SrlInterpretedShape) o;
                completeList.addAll(sis.getRecursiveSubShapeList());
            }
        }
        return completeList;
    }

    /**
     * Find the stroke by ID.
     *
     * @param id the ID
     * @return the stroke with the given ID
     */
    public SrlStroke getStroke(final UUID id) {
        for (SrlObject comp : getRecursiveSubShapeList()) {
            if (comp instanceof SrlStroke && comp.getId().equals(id)) {
                return (SrlStroke) comp;
            }
        }
        return null;
    }

    /**
     * Gets a list of all of the strokes that make up this object.
     * It searches recursively to get all of the strokes of this object.
     * If it does not have any strokes, the list will be empty.
     *
     * @return
     */
    public ArrayList<SrlStroke> getStrokes() {
        final ArrayList<SrlStroke> completeList = new ArrayList<SrlStroke>();
        for (SrlObject o : mSubShapes) {
            if (o instanceof SrlStroke) {
                completeList.add((SrlStroke) o);
            } else if (o instanceof SrlShape) {
                completeList.addAll(((SrlShape) o).getStrokes());
            }
        }
        return completeList;
    }

    /**
     * Gets the list of subshapes
     *
     * @return list of objects that make up this object
     */
    public ArrayList<SrlObject> getSubShapes() {
        return mSubShapes;
    }

    /**
     * Removes the ith Component from this SContainer
     *
     * @param i the ith component of the container
     * @return the value removed
     */
    public SrlObject remove(final int i) {

        flagExternalUpdate();
        return mSubShapes.remove(i);
    }

    /**
     * Removes a subcomponent from this container.
     *
     * @param subcomponent subcomponent to remove
     * @return true if something was removed
     */
    public boolean remove(final SrlObject subcomponent) {
        flagExternalUpdate();
        return mSubShapes.remove(subcomponent);
    }

    /**
     * Removes a bunch of subcomponents from this SContainer.
     *
     * @param subcomponents the collection of subcomponents
     * @return true if all target subcomponents were removed from myContents,
     * false otherwise
     */
    public boolean removeAll(final Collection<? extends SrlObject> subcomponents) {

        flagExternalUpdate();
        return mSubShapes.removeAll(subcomponents);
    }

    /**
     * Gets the size of the myContents.
     *
     * @return the size of the myContents
     */
    public int size() {

        return mSubShapes.size();
    }

    /**
     * To be called by the classes that extend this
     *
     * @param x x amount to translate by
     * @param y y amount to translate by
     */
    protected void translateSubShapes(final double x, final double y) {
        for (SrlObject s : getSubShapes()) {
            s.translate(x, y);
        }
    }

    /**
     * Calculates the bounding box of the shape
     */
    protected void calculateBBox() {
        setBoundingBox(new SrlRectangle(new SrlPoint(getMinX(), getMinY()),
                new SrlPoint(getMaxX(), getMaxY())));
    }

    /**
     * Checks if this container contains any of the given components.
     *
     * @param components the target components
     * @return true if this container contains any of the target components,
     * false otherwise
     */
    public boolean containsAny(final Collection<? extends SrlObject> components) {

        for (SrlObject component : components) {
            if (contains(component)) {
                return true;
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMinX()
     */
    @Override
    public double getMinX() {
        double min = Double.POSITIVE_INFINITY;
        for (SrlObject shape : getSubShapes()) {
            min = Math.min(min, shape.getMinX());
        }
        return min;
    }

    /* (non-Javadoc)
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMinY()
     */
    @Override
    public double getMinY() {
        double min = Double.POSITIVE_INFINITY;
        for (SrlObject shape : getSubShapes()) {
            min = Math.min(min, shape.getMinY());
        }
        return min;
    }

    /* (non-Javadoc)
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxX()
     */
    @Override
    public double getMaxX() {
        double max = Double.NEGATIVE_INFINITY;
        for (SrlObject shape : getSubShapes()) {
            max = Math.max(max, shape.getMaxX());
        }
        return max;
    }

    /* (non-Javadoc)
     * @see edu.tamu.srl.object.shape.SRL_Shape#getMaxY()
     */
    @Override
    public double getMaxY() {
        double max = Double.NEGATIVE_INFINITY;
        for (SrlObject shape : getSubShapes()) {
            max = Math.max(max, shape.getMaxY());
        }
        return max;
    }

    /**
     * Translate the object by the amount x,y
     *
     * @param x
     * @param y
     */
    public void translate(final double x, final double y) {
        for (SrlObject o : getSubShapes()) {
            o.translate(x, y);
        }
    }

}
