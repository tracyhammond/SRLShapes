package edu.tamu.srl.settings;

import java.util.UUID;

/**
 * Generates an {@link UUID} for use when creating new {@link edu.tamu.srl.object.shape.SrlObject}.
 *
 * @author gigemjt
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class IdGenerator {

    /**
     * initialized to 0x4000L (the version -- 4: randomly generated UUID).
     *
     * This version specifies that the UUID will be contain a random element in it.
     */
    private static final long UUID_VERSION = 0x4000L;

    /**
     * 3 bytes of randomness is the starting value of the counter.
     * (0x0 - 0xFFF)
     *
     * This value must always be smaller or equal to 0x1000L as a larger value could potentially change the version of the UUID.
     */
    private static final long MAX_STARTING_RAND_VALUE = 0x1000;

    /**
     * The amount the the counter is incremented by each time a new id is created.
     * This value must follow this constraint: increment >= 0x10000.
     *
     * The reason for this is that a smaller number could potentially change the version of the UUID.
     */
    private static final long COUNTER_INCREMENT = 0x10000L;

    /**
     * I do believe this number was meant to be the max int it is 2>>63.
     * This number is so that the leastSigBits is never zero.
     */
    private static final long MAX_TIME_VALUE = 0x8000000000000000L;

    /**
     * counter will be incremented by {@link #UUID_VERSION} for each new SrlObject that is
     * created counter is used as the most significant bits of the UUID.
     *
     * initialized to {@link #UUID_VERSION} (the version -- 4: randomly generated UUID) along
     * with 3 bytes of randomness: Math.random()*{@link #MAX_STARTING_RAND_VALUE} (0x0 - 0xFFF)
     *
     * the randomness further reduces the chances of collision between multiple
     * sketches created on multiple computers simultaneously
     */
    private static long counter = UUID_VERSION | (long) (Math.random() * MAX_STARTING_RAND_VALUE);

    /**
     * Generates a new UUID: based on a counter + time, version 4, variant bits
     * set time is {@link System#nanoTime()}.
     *
     * @return the UUID.
     */
    public static UUID nextId() {
        counter += COUNTER_INCREMENT;
        return new UUID(counter, System.nanoTime() | MAX_TIME_VALUE);
    }
}
