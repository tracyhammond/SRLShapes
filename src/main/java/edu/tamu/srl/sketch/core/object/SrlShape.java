package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.AbstractSrlComponent;
import edu.tamu.srl.sketch.core.abstracted.SrlObject;
import edu.tamu.srl.sketch.core.tobenamedlater.SrlShapeConfig;
import edu.tamu.srl.sketch.core.virtual.SrlBoundingBox;
import edu.tamu.srl.sketch.core.virtual.SrlPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * <p>
 * A shape is anything that can be composed one or more subOjects. ({@link edu.tamu.srl.sketch.core.abstracted.SrlObject}).
 * A shape is assumed to have been recognized and may contain information pertaining to recognition.
 * A shape itself is an interpretation and as such it does not contain any interpretation.
 * </p>
 * <h1>Implementation Comments</h1>
 * All methods when interacting with the list (unless inserting into the list or removing from the list)
 * use the getPoints() method.  This is so that subclasses can have augmented versions of the list without
 * needing to overwrite every method or get passed a modifiable version of the list.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 * @author gigemjt
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.CloneMethodMustImplementCloneable", "PMD.AvoidDuplicateLiterals" })
public class SrlShape extends SrlObject {

    /**
     * This is the id of the interpretation and should be unique across all interpretations.
     * This value should not be generated at runtime but instead should be linked to a specific interpretation of a certain recognizer
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     * <br>
     * This id may not be unique across multiple recognizers.
     * This id in combination with the {@link #mRecognizerId} is guaranteed to be unique.
     */
    private final UUID mInterpretationId;
    /**
     * This is the id of the recognizer and should be unique across all recognizer.
     * This value should not be generated at runtime but instead should be linked to a specific recognizer.
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     * <br>
     * Each recognizer may have multiple interpretations so this id may not be unique across multiple interpretations.
     * This id in combination with the {@link #mInterpretationId} is guaranteed to be unique.
     */
    private final UUID mRecognizerId;
    /**
     * A human readable label of the interpretation.
     * This also is used to signify what the shape actually is regardless of where it comes from.
     */
    private final String mInterpretation;
    /**
     * The complexity is not bounded but should always be larger than 0.
     */
    private final double mComplexity;
    /**
     * True if this interpretation is the final form of this shape.
     * This signifies to the recognizer that a shape with this interpretation
     * should not be used as a possible subshape of an even larger more complex shape.
     * <br>
     * This value should not be used for determining the order of interpretations.
     */
    private final boolean mIsEndState;
    /**
     * Was this object made up from a collection of sub{@link edu.tamu.srl.sketch.core.abstracted.SrlObject}s?
     * If so they are in this list.
     * This list usually gets filled in through recognition.
     * This list can be examined hierarchically.
     * e.g., an arrow might have three lines inside, and each line might have a stroke.
     */
    private final List<SrlObject> mSubShapes;
    /**
     * The confidence of the interpretation (a value between 0 and 1).
     * With 1 being 100% confident
     */
    private double mConfidence;
    /**
     * True if a user forced this interpretation.
     * This is a signal to the recognizer that this interpretation can not be overwritten with a different possibility.
     * An interpretation shall remain forced until a user performs an action that changes it from forced to not forced.
     */
    private boolean mIsForced;
    /**
     * Description of the object. Typically a sentence.
     */
    private String mDescription;

    /**
     * Default constructor.
     * <br>
     * Creates an object with an id and a time.
     */
    public SrlShape() {
        super();
        mDescription = "";
        mSubShapes = new ArrayList<>();
        mInterpretationId = null;
        mRecognizerId = null;
        mConfidence = 0;
        mComplexity = -1;
        mIsForced = false;
        mIsEndState = false;
        mInterpretation = null;
    }

    /**
     * A copy constructor.
     *
     * Copies all values from the given object.
     * Performs a shallow copy
     *
     * @param original the object that is being copied.
     */
    public SrlShape(final SrlShape original) {
        this(original, false);
    }

    /**
     * Copy Constructor.
     *
     * @param original the shape being copied.
     * @param deep     true if a deep copy is being performed.  Otherwise a shallow copy is performed.
     */
    public SrlShape(final SrlShape original, final boolean deep) {
        super(original);
        this.mDescription = original.getDescription();
        mInterpretationId = original.getInterpretationId();
        mRecognizerId = original.getRecognizerId();
        mConfidence = original.getConfidence();
        mComplexity = original.getComplexity();
        mIsForced = original.isForced();
        mIsEndState = original.isEndState();
        mInterpretation = original.getInterpretation();
        mSubShapes = new ArrayList<>();
        if (deep) {
            final List<SrlObject> cache = original.getSubObjects();
            for (int i = 0; i < cache.size(); i++) {
                this.add((SrlObject) cache.get(i).deepClone());
            }
        } else {
            // shallow copy
            this.addAll(original.getSubObjects());
        }
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time        The time the shape was originally created.
     * @param uuid        The unique identifier of the shape.
     * @param config      The set of data used to configure interpretation.
     * @param description the description of the shape.
     */
    public SrlShape(final long time, final UUID uuid, final SrlShapeConfig config, final String description) {
        super(time, uuid);
        if (config != null) {
            mInterpretationId = config.interpretationId;
            mRecognizerId = config.recognizerId;
            mConfidence = config.confidence;
            mComplexity = config.complexity;
            mIsForced = config.isForced;
            mIsEndState = config.isEndState;
            mInterpretation = config.interpretation;
        } else {
            mInterpretationId = null;
            mRecognizerId = null;
            mConfidence = 0;
            mComplexity = -1;
            mIsForced = false;
            mIsEndState = false;
            mInterpretation = null;
        }
        mSubShapes = new ArrayList<>();
        this.mDescription = description;
    }

    /**
     * Accepts values that can only be set during construction.
     *
     * @param time          The time the shape was originally created.
     * @param uuid          The unique identifier of the shape.
     * @param isUserCreated True if the user created the stroke instead of the computer.
     * @param config        The set of data used to configure interpretation.
     * @param description   the description of the shape.
     */
    public SrlShape(final long time, final UUID uuid, final boolean isUserCreated, final SrlShapeConfig config, final String description) {
        super(time, uuid, isUserCreated);
        if (config != null) {
            mInterpretationId = config.interpretationId;
            mRecognizerId = config.recognizerId;
            mConfidence = config.confidence;
            mComplexity = config.complexity;
            mIsForced = config.isForced;
            mIsEndState = config.isEndState;
            mInterpretation = config.interpretation;
        } else {
            mInterpretationId = null;
            mRecognizerId = null;
            mConfidence = 0;
            mComplexity = -1;
            mIsForced = false;
            mIsEndState = false;
            mInterpretation = null;
        }
        mSubShapes = new ArrayList<>();
        this.mDescription = description;
    }

    /**
     * Translate the object by the amount x,y.
     *
     * @param xOffset the amount in the x direction to move the object by.
     * @param yOffset the amount in the y direction to move the object by.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void translate(final double xOffset, final double yOffset) {
        final List<SrlObject> cache = getSubObjects();
        for (int i = 0; i < cache.size(); i++) {
            cache.get(i).translate(xOffset, yOffset);
        }
        resetBounders();
    }

    /**
     * Scales the SComponent by the given x- and y-factor.
     *
     * @param xFactor the x-factor
     * @param yFactor the y-factor
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public void scale(final double xFactor, final double yFactor) {
        final List<SrlObject> cache = getSubObjects();
        for (int i = 0; i < cache.size(); i++) {
            cache.get(i).scale(xFactor, yFactor);
        }
        resetBounders();
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
        final List<SrlObject> cache = getSubObjects();
        for (int i = 0; i < cache.size(); i++) {
            cache.get(i).rotate(radians, xCenter, yCenter);
        }
        resetBounders();
    }

    /**
     * @return A cloned object that is an instance of {@link edu.tamu.srl.sketch.core.abstracted.AbstractSrlComponent}.
     * This cloned object is only a shallow copy.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public Object clone() {
        return new SrlShape(this, false);
    }

    /**
     * @return performs a deep clone of the object cloning all objects contained as well.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public AbstractSrlComponent deepClone() {
        return new SrlShape(this, true);
    }

    /**
     * Performs a shallow equals.
     * By default this is called by the {@link #equals(Object)} method.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean shallowEquals(final AbstractSrlComponent other) {
        if (!super.shallowEquals(other)) {
            return false;
        }

        // We at least know it is an SrlObject now.
        if (!(other instanceof SrlShape)) {
            return false;
        }

        if (this.mInterpretationId != null) {
            if (!this.mInterpretationId.equals(((SrlShape) other).getInterpretationId())) {
                return false;
            }
        } else if (((SrlShape) other).getInterpretationId() != null) {
            return false;
        }

        if (this.mInterpretation != null) {
            if (!this.mInterpretation.equals(((SrlShape) other).getInterpretation())) {
                return false;
            }
        } else if (((SrlShape) other).getInterpretation() != null) {
            return false;
        }
        return this.mComplexity == ((SrlShape) other).getComplexity()
                && this.mIsEndState == ((SrlShape) other).isEndState()
                && this.isForced() == ((SrlShape) other).isForced()
                && this.getNumChildren() == ((SrlShape) other).getNumChildren();
    }

    /**
     * @return The average of all of the points in the shape.
     *
     * The time of this point actually contains the total number of points in this sub object.
     * This value can be grabbed with {@link SrlPoint#getTime()}.
     * Obviously this has a worst case of O(n) where n is the number of points in the shape.
     */
    @Override public final SrlPoint getAveragedPoint() {
        final int numChilds = this.getNumChildren();
        final double[] xArray = new double[numChilds];
        final double[] yArray = new double[numChilds];
        long totalPoints = 0;
        final List<SrlObject> cache = getSubObjects();

        // recursively gather averages.
        for (int index = 0; index < numChilds; index++) {
            final SrlObject subObject = cache.get(index);
            final SrlPoint averagedCenter = subObject.getAveragedPoint();
            xArray[index] = averagedCenter.getX() * ((double) averagedCenter.getTime());
            yArray[index] = averagedCenter.getY() * ((double) averagedCenter.getTime());
            totalPoints += averagedCenter.getTime();
        }

        // use the averages together.
        double xAvg = 0;
        double yAvg = 0;
        for (int index = 0; index < numChilds; index++) {
            xAvg += xArray[index] / ((double) totalPoints);
            yAvg += yArray[index] / ((double) totalPoints);
        }
        return new SrlPoint(xAvg, yAvg, totalPoints);
    }

    /**
     * Looks deep into two components to check equality.
     * It would probably be smart to also have this call shallowEquals.
     *
     * @param other the other SrlObject.
     * @return true if content is equal, false otherwise
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public boolean deepEquals(final AbstractSrlComponent other) {
        if (!this.shallowEquals(other)) {
            return false;
        }
        // We know it is an SrlShape and they have the same number of children.
        final List<SrlObject> otherCache = ((SrlShape) other).getSubObjects();
        final List<SrlObject> localCache = this.getSubObjects();
        boolean equal = true;
        for (int index = 0; index < otherCache.size() && equal; index++) {
            equal &= otherCache.get(index).deepEquals(localCache.get(index));
        }
        return equal;
    }

    /**
     * Attempts to find the closest distance to this other component.
     *
     * @param srlComponent
     *         the component we are trying to find this distance to.
     * @return the distance between the components.
     * <b>NOTE: due to possible heuristics used this method is not commutative.</b>  <pre>Meaning that:
     *
     * <code>shape1.distance(shape2) == shape2.distance(shape1);</code>
     * May be false.
     * </pre>
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double distance(final AbstractSrlComponent srlComponent) {
        throw new UnsupportedOperationException("need to implement this");
    }

    /**
     * Calculates the bounding box.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override protected void calculateBBox() {
        final List<SrlObject> cache = getSubObjects();
        final SrlBoundingBox[] subBoxes = new SrlBoundingBox[cache.size()];
        for (int i = 0; i < cache.size(); i++) {
            subBoxes[i] = cache.get(i).getBoundingBox();
        }
        this.setBoundingBox(SrlBoundingBox.union(subBoxes));
    }

    /**
     * Called to calculate the convex hull of the object.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override protected void calculateConvexHull() {
        throw new UnsupportedOperationException("implement this");
    }

    /**
     * @return the largest X value. (larger x values are denoted as being on the right hand side of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMaxX() {
        double max = Double.NEGATIVE_INFINITY;
        final List<SrlObject> shapeList = getSubObjects();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlObject shape = shapeList.get(index);
            max = Math.max(max, shape.getMaxX());
        }
        return max;
    }

    /**
     * @return the largest Y value. (larger Y values are denoted as being at the bottom the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMaxY() {
        double max = Double.NEGATIVE_INFINITY;
        final List<SrlObject> shapeList = getSubObjects();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlObject shape = shapeList.get(index);
            max = Math.max(max, shape.getMaxY());
        }
        return max;
    }

    /**
     * @return the smallest X value. (smaller x values are denoted as being on the left hand side of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMinX() {
        double min = Double.POSITIVE_INFINITY;
        final List<SrlObject> shapeList = getSubObjects();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlObject shape = shapeList.get(index);
            min = Math.min(min, shape.getMinX());
        }
        return min;
    }

    /**
     * @return the smallest Y value. (smaller Y values are denoted as being at the top of the screen.
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Override public double getMinY() {
        double min = Double.POSITIVE_INFINITY;
        final List<SrlObject> shapeList = getSubObjects();
        for (int index = 0; index < shapeList.size(); index++) {
            final SrlObject shape = shapeList.get(index);
            min = Math.min(min, shape.getMinY());
        }
        return min;
    }

    /**
     * @param isForced if the user decides one of the interpretations is forced.
     */
    public final void setIsForced(final boolean isForced) {
        this.mIsForced = isForced;
    }

    /**
     * Adds a subObject to this object at the specified index.
     *
     * @param index        point to add the content
     * @param subObject the sub object.
     */
    public final void add(final int index, final SrlObject subObject) {
        mSubShapes.add(index, subObject);
        resetBounders();
    }

    /**
     * Adds a subshape to this object.
     * This usually happens during recognition, when a new object
     * is made up from one or more objects
     *
     * @param subObject an object that can be added to a shape.
     */
    public final void add(final SrlObject subObject) {
        mSubShapes.add(subObject);
        resetBounders();
    }

    /**
     * adds a list of subObjects to this object.
     *
     * @param subShapes list of subShapes to add
     */
    public final void addAll(final List<? extends SrlObject> subShapes) {
        mSubShapes.addAll(subShapes);
        resetBounders();
    }

    /**
     * Clears the container.
     */
    public final void clear() {
        mSubShapes.clear();
        resetBounders();
    }

    /**
     * Checks if this container contains the target component.
     *
     * @param component the target component
     * @return true if this container contains the target component, false
     * otherwise
     */
    public final boolean contains(final SrlObject component) {
        for (SrlObject sub : mSubShapes) {
            if (sub.equals(component)) {
                return true;
            } else if (sub instanceof SrlShape && ((SrlShape) sub).contains(component)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this container contains any of the given components.
     *
     * @param components the target components
     * @return true if this container contains any of the target components,
     * false otherwise.
     */
    public final boolean containsAny(final Collection<? extends SrlObject> components) {
        for (SrlObject component : components) {
            if (contains(component)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ith component.
     *
     * @param index the index
     * @return the ith component
     */
    public final SrlObject get(final int index) {
        return getSubObjects().get(index);
    }

    /**
     * Gets the number of direct children of this shape.
     *
     * This does not look go any deeper than the children of this shape.
     *
     * @return The number of objects.
     */
    public final int getNumChildren() {
        return getSubObjects().size();
    }

    /**
     * Gets a list of all of the objects that make up this shape.
     *
     * Note that this returns all shapes not just the leaf nodes.
     * Here is an example shape tree and how this method returns the shapes.
     * <pre>
     * KEY:  Shape = S, Stroke = T
     * S1 is the shape this method is called on.
     *       S1
     *      /| \
     *    S2 S3 S4
     *   / |  |    \
     *  T1 S5 T2   S6
     *     |        |
     *     T3      T4
     *
     * Resultant List:
     * S1, S2, T1, S5, T3, S3, T2, S4, S6, T4
     * </pre>
     *
     * @return a list of {@link SrlObject}s.
     */
    public final List<SrlObject> getRecursiveSubObjectList() {
        final List<SrlObject> completeList = new ArrayList<SrlObject>();
        completeList.add(this);
        final List<SrlObject> cache = getSubObjects();
        for (int index = 0; index < cache.size(); index++) {
            final SrlObject srlObject = cache.get(index);
            if (srlObject instanceof SrlShape) {
                completeList.addAll(((SrlShape) srlObject).getRecursiveSubObjectList());
            } else {
                completeList.add(srlObject);
            }
        }
        return completeList;
    }

    /**
     * Gets a list of all of the strokes that make up this shape.
     *
     * <pre>
     * KEY:  Shape = S, Stroke = T
     * S1 is the shape this method is called on.
     *       S1
     *      /| \
     *    S2 S3 S4
     *   / |  |    \
     *  T1 S5 T2   S6
     *     |        |
     *     T3      T4
     *
     * Resultant List:
     * T1, T3, T2, T4
     * </pre>
     *
     *
     * @return a list of {@link SrlStroke}s.
     */
    public final List<SrlStroke> getRecursiveStrokeList() {
        final List<SrlStroke> completeList = new ArrayList<>();
        final List<SrlObject> cache = getSubObjects();
        for (int index = 0; index < cache.size(); index++) {
            final SrlObject srlObject = cache.get(index);
            if (srlObject instanceof SrlShape) {
                completeList.addAll(((SrlShape) srlObject).getRecursiveStrokeList());
            } else if (srlObject instanceof SrlStroke) {
                completeList.add((SrlStroke) srlObject);
            }
        }
        return completeList;
    }

    /**
     * Gets a list of all of the leaf shapes that make up this shape.
     * These are shapes that do not contain any shapes but only contains strokes.
     *
     * <pre>
     * KEY:  Shape = S, Stroke = T
     * S1 is the shape this method is called on.
     *       S1
     *      /| \
     *    S2 S3 S4
     *   / |  |    \
     *  T1 S5 T2   S6
     *     |        |
     *     T3      T4
     *
     * Resultant List:
     * S5, S3, S6
     * </pre>
     *
     * @return a list of {@link SrlShape}s.
     */
    public final List<SrlShape> getRecursiveLeafShapes() {
        final List<SrlShape> completeList = new ArrayList<>();
        final List<SrlObject> cache = getSubObjects();
        for (int index = 0; index < cache.size(); index++) {
            final SrlObject srlObject = cache.get(index);
            if (srlObject instanceof SrlShape) {
                final List<SrlShape> resultList = ((SrlShape) srlObject).getRecursiveLeafShapes();
                // does not use the isLeafShape because that is an extra recurse through the list.
                if (resultList.isEmpty()) {
                    completeList.add((SrlShape) srlObject);
                } else {
                    completeList.addAll(resultList);
                }
            }
        }
        return completeList;
    }

    /**
     * True if the shape is a leaf Shape.
     * A leaf shape is defined by being a shape that ony contains strokes and no other shapes.
     *
     * <pre>
     * KEY:  Shape = S, Stroke = T
     * S1 is the shape this method is called on.
     *       S1
     *      /| \
     *    S2 S3 S4
     *   / |  |    \
     *  T1 S5 T2   S6
     *     |        |
     *     T3      T4
     *
     * Shapes that are considered leaf shapes.
     * S5, S3, S6
     * </pre>
     * @return True if it is a leaf shape
     */
    public final boolean isLeafShape() {
        final List<SrlObject> cache = getSubObjects();
        for (int index = 0; index < cache.size(); index++) {
            final SrlObject srlObject = cache.get(index);
            if (!(srlObject instanceof SrlStroke)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the list of sub-{@link SrlObject}s.
     * This list is not modifiable to modify the list you must go through the methods presented by this class.
     *
     * @return list of objects that make up this object.  <b>This should never return null.</b>
     */
    @SuppressWarnings("checkstyle:designforextension")
    public List<SrlObject> getSubObjects() {
        return Collections.unmodifiableList(mSubShapes);
    }

    /**
     * Recursively searches to get the first stroke.  Uses only order of insertion.
     * @return The first stroke in this shape. Null if the shape is empty.
     */
    public final SrlStroke getFirstStroke() {
        if (this.getSubObjects().isEmpty()) {
            return null;
        }
        final SrlObject obj = this.get(0);
        if (obj instanceof SrlShape) {
            return ((SrlShape) obj).getFirstStroke();
        } else if (obj instanceof SrlStroke) {
            return (SrlStroke) obj;
        } else {
            return null;
        }
    }

    /**
     * Recursively searches to get the last stroke.  Uses only order of insertion.
     * @return The last stroke in this shape. Null if the shape is empty.
     */
    public final SrlStroke getLastStroke() {
        if (this.getSubObjects().isEmpty()) {
            return null;
        }
        final SrlObject obj = this.get(this.getNumChildren() - 1);
        if (obj instanceof SrlShape) {
            return ((SrlShape) obj).getLastStroke();
        } else if (obj instanceof SrlStroke) {
            return (SrlStroke) obj;
        } else {
            return null;
        }
    }

    /**
     * Removes the ith {@link SrlObject} from this {@link SrlShape}.
     *
     * @param index the ith {@link SrlObject} of the container.
     * @return the value removed.
     */
    public final SrlObject remove(final int index) {
        final SrlObject obj = mSubShapes.remove(index);
        resetBounders();
        return obj;
    }

    /**
     * Removes a subObject from this container.
     *
     * @param subObject subObject to remove
     * @return true if something was removed
     */
    public final boolean remove(final SrlObject subObject) {
        final boolean result = mSubShapes.remove(subObject);
        if (result) {
            resetBounders();
        }
        return result;
    }

    /**
     * Removes a bunch of subObjects from this SContainer.
     *
     * @param subObjects the collection of subObjects
     * @return true if all target subObjects were removed from myContents,
     * false otherwise
     */
    public final boolean removeAll(final Collection<? extends SrlObject> subObjects) {
        final boolean result = mSubShapes.removeAll(subObjects);
        if (result) {
            resetBounders();
        }
        return result;
    }

    /**
     * @return an iterator
     */
    public final Iterator<SrlObject> iterator() {
        return getSubObjects().iterator();
    }

    /**
     * This is the id of the interpretation and should be unique across all interpretations.
     * This value should not be generated at runtime but instead should be linked to a specific interpretation of a certain recognizer
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     * <br>
     * This id may not be unique across multiple recognizers.
     * This id in combination with the {@link #mRecognizerId} is guaranteed to be unique.
     *
     * @return {@link UUID}
     */
    public final UUID getInterpretationId() {
        return mInterpretationId;
    }

    /**
     * This is the id of the recognizer and should be unique across all recognizer.
     * This value should not be generated at runtime but instead should be linked to a specific recognizer.
     * This can be used in the case of interpretations having the same {@link #mInterpretation} (think Arrow) but are created in different ways.
     * (single stroke arrow, multi stroke arrow).
     * <br>
     * Each recognizer may have multiple interpretations so this id may not be unique across multiple interpretations.
     * This id in combination with the {@link #mInterpretationId} is guaranteed to be unique.
     *
     * @return {@link UUID}
     */
    public final UUID getRecognizerId() {
        return mRecognizerId;
    }

    /**
     * A human readable label of the interpretation.
     * This also is used to signify what the shape actually is regardless of where it comes from.
     *
     * @return The interpretation.
     */
    public final String getInterpretation() {
        return mInterpretation;
    }

    /**
     * The confidence of the interpretation (a value between 0 and 1).
     * With 1 being 100% confident.
     *
     * @return confidence level
     */
    public final double getConfidence() {
        return mConfidence;
    }

    /**
     * @param confidence the new confidence.
     */
    public final void setConfidence(final double confidence) {
        this.mConfidence = confidence;
    }

    /**
     * The complexity is not bounded but should always be larger than 0.
     *
     * @return complexity level
     */
    public final double getComplexity() {
        return mComplexity;
    }

    /**
     * True if a user forced this interpretation.
     * This is a signal to the recognizer that this interpretation can not be overwritten with a different possibility.
     * An interpretation shall remain forced until a user performs an action that changes it from forced to not forced.
     *
     * @return true if the shape is forced
     */
    public final boolean isForced() {
        return mIsForced;
    }

    /**
     * True if this interpretation is the final form of this shape.
     * This signifies to the recognizer that a shape with this interpretation
     * should not be used as a possible subshape of an even larger more complex shape.
     * <br>
     * This value should not be used for determining the order of interpretations.
     *
     * @return true if the shape is in its end state.
     */
    public final boolean isEndState() {
        return mIsEndState;
    }

    /**
     * Description of the object. Typically a sentence.
     *
     * @return description of the object
     */
    public final String getDescription() {
        return mDescription;
    }

    /**
     * @return A string representation of the bounding box.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public String toString() {
        return "SHA[" + this.getInterpretation() + " CONF:" + this.getConfidence() + " ID:" + this.getId() + "]";
    }
}
