package com.jonki.popcorn.core.util;

import com.jonki.popcorn.common.exception.ResourcePreconditionException;
import org.apache.commons.lang3.StringUtils;

/**
 * Class utils for enum.
 */
public final class EnumUtils {

    /**
     * A common method for all enums since they can't have another base class.
     *
     * @param <T> Enum type
     * @param c Enum type. All enums must be all caps
     * @param string Case insensitive
     * @return Corresponding enum
     * @throws IllegalArgumentException Illegal Argument
     * @throws ResourcePreconditionException Unacceptable value
     */
    public static <T extends Enum<T>> T getEnumFromString(final Class<T> c, final String string)
            throws ResourcePreconditionException {
        if(c != null && StringUtils.isNotBlank(string)) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase());
            } catch(IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Illegal Argument"
                );
            }
        }
        throw new ResourcePreconditionException(
                "Unacceptable value"
        );
    }
}
