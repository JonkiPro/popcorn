package com.core.utils;

import com.common.exception.ResourcePreconditionException;
import org.apache.commons.lang3.StringUtils;

/**
 * Class utils for enum.
 */
public class EnumUtils {

    /**
     * A common method for all enums since they can't have another base class.
     *
     * @param <T> Enum type
     * @param c Enum type. All enums must be all caps
     * @param string Case insensitive
     * @return Corresponding enum
     */
    public static <T extends Enum<T>> T getEnumFromString(final Class<T> c, final String string) {
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
