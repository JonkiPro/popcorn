package com.jonki.popcorn.core.util;

import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Utility methods for collectors.
 */
public final class CollectorUtils {

    /**
     * It applies an extra finisher at the end, that throws an exception,
     * or if no exception, returns the first element of the list.
     *
     * @param <T> Type
     * @return The first element of the list
     */
    public static <T> Collector<T, ?, T> singletonCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
