package edu.tamu.srl.sketch.core.abstracted;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 *
 * Oversees all javaobjects and virtual objects and centralizes all code used by everything.
 *
 */
public abstract class SrlComponent {

    /**
     * Each object has a unique ID associated with it.
     */
    private final UUID mId;

    /**
     * The creation time of the object.
     */
    private final long mTime;

    /**
     * The name of the object, such as "triangle1" or stroke1.
     */
    private String mName = "";
}
