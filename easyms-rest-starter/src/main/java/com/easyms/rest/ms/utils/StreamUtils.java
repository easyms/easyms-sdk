package com.easyms.rest.ms.utils;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author khames.
 */
public class StreamUtils {

    public static <T> Stream<T> ofNullable(final Iterable<T> iterable) {
        return Optional.ofNullable(iterable).map(itr -> StreamSupport.stream(itr.spliterator(), false)).orElseGet(Stream::empty);
    }
}
