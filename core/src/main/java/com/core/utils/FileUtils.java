package com.core.utils;

import com.common.exception.ResourcePreconditionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Utility methods for files.
 */
public final class FileUtils {

    private static final String PNG_MIME_TYPE = "image/png";
    private static final String JPEG_MIME_TYPE = "image/jpeg";

    /**
     * Convenience method of validation of an image file.
     *
     * @param file The file for validation
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     */
    public static void validImage(final File file) throws ResourcePreconditionException {
        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new ResourcePreconditionException("An I/O error occurs");
        }
        if(!contentType.equals(PNG_MIME_TYPE)
                && !contentType.equals(JPEG_MIME_TYPE)) {
            throw new ResourcePreconditionException("Incorrect content type");
        }
    }

    /**
     * Convenience method of validation of an image files.
     *
     * @param files Files for validation
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     */
    public static void validImage(final List<File> files) throws ResourcePreconditionException {
        for(final File file : files) {
            String contentType;
            try {
                contentType = Files.probeContentType(file.toPath());
            } catch (IOException e) {
                throw new ResourcePreconditionException("An I/O error occurs");
            }
            if(!contentType.equals(PNG_MIME_TYPE)
                    && !contentType.equals(JPEG_MIME_TYPE)) {
                throw new ResourcePreconditionException("Incorrect content type");
            }
        }
    }
}
