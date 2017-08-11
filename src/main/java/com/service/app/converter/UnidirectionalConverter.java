package com.service.app.converter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface UnidirectionalConverter<F, T> {

    T convert(F from);

    default T convertRequireNonNull(F from) {
        Objects.requireNonNull(from, "'from' cannot be null");
        return convert(from);
    }

    default List<T> convertAll(Collection<F> elementsToConvert) {
        return elementsToConvert.stream().map(this::convertRequireNonNull).collect(Collectors.toList());
    }
}
