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
    private UUID mId = UUID.randomUUID();
    /**
     * Name if applicable.
     */
    private String mName;

    /**
     * Creates an author with a name and an id.
     *
     * @param id
     *         the id of the author.
     * @param name
     *         The name of the author.
     */
    public SrlAuthor(final UUID id, final String name) {
        this.mId = id;
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

}
