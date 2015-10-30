package edu.tamu.srl.sketch.core.tobenamedlater;

import java.util.UUID;

/**
 * Created by gigemjt on 11/3/14.
 *
 * <br>
 * Class containing information about an author of a stroke (e.g. by whom it was
 * drawn).
 * The Id of the author should be consistant across the entire sketch.  But it does not have to persists across multiple sketches.
 *
 * <p>Copyright Tracy Hammond, Sketch Recognition Lab, Texas A&amp;M University</p>
 *
 * @author gigemjt
 * @author bpaulson
 */
public class SrlAuthor {

    /**
     * Unique ID of the author.
     */
    private final UUID mId;

    /**
     * Name if applicable.
     */
    private final String mName;

    /**
     * Creates an author with a name and an uuid.
     *
     * @param uuid
     *         the uuid of the author.
     * @param name
     *         The name of the author.
     */
    public SrlAuthor(final UUID uuid, final String name) {
        this.mId = uuid;
        this.mName = name;
    }

    /**
     * @return id of the author.
     */
    public final UUID getId() {
        return mId;
    }

    /**
     * @return the name of the author.
     */
    public final String getName() {
        return mName;
    }

    /**
     * @return A string representation of the author.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public String toString() {
        return "AUTH[" + this.getName() + "]";
    }
}
