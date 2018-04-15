package com.web.web.util;

import com.common.dto.movie.request.ImageRequest;
import com.common.exception.ResourcePreconditionException;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Utility methods for maps.
 */
public final class MapUtils {

    /**
     * Convenience method for merging a set and list into a map.
     *
     * @param set Set with IDs
     * @param list List with files
     * @return A map containing an identifier with an assigned object
     * @throws ResourcePreconditionException if the set and list are of different sizes
     */
    public static HashMap<Long, ImageRequest> merge(final Set<Long> set, final List<MultipartFile> list)
            throws ResourcePreconditionException {
        final HashMap<Long, ImageRequest> map = new HashMap<>();
        if(set.size() == list.size()) {
            ImageRequest.Builder builder;
            final Iterator<Long> longIterator = set.iterator();
            final Iterator<MultipartFile> multipartFileIterator = list.iterator();
            while(longIterator.hasNext() && multipartFileIterator.hasNext()) {
                builder = new ImageRequest.Builder().withFile(MultipartFileUtils.convert(multipartFileIterator.next()));
                map.put(longIterator.next() , builder.build());
            }
        } else {
            throw new ResourcePreconditionException("The set and list are of different sizes");
        }
        return map;
    }
}
