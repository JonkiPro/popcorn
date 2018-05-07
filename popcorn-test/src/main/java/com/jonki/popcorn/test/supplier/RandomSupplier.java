package com.jonki.popcorn.test.supplier;

import java.util.Date;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Supply random types.
 */
public final class RandomSupplier {

    /**
     * Get a random String.
     */
    public static final Supplier<String> STRING = UUID.randomUUID()::toString;
    /**
     * Get a random integer.
     */
    public static final Supplier<Integer> INT
            = () -> (int) (Math.random() * (Integer.MAX_VALUE - 1)) + 1;
    /**
     * Get a random long.
     */
    public static final Supplier<Long> LONG
            = () -> (long) (Math.random() * (Long.MAX_VALUE - 1)) + 1;
    /**
     * Get a random date.
     */
    public static final Supplier<Date> DATE = () -> new Date(INT.get());

    /**
     * Utility class.
     */
    private RandomSupplier() {
    }
}
