package com.web.web.util;

import com.common.exception.ResourcePreconditionException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility methods for multipart files.
 */
public final class MultipartFileUtils {

    /**
     * Convenience method to convert into a file.
     *
     * @param multipartFile Multipart file to conversion
     * @return Converted file
     * @throws ResourcePreconditionException if an I/O error occurs
     */
    public static File convert(final MultipartFile multipartFile) throws ResourcePreconditionException {
        final File convFile = new File(multipartFile.getOriginalFilename());
        try {
            convFile.createNewFile();
            final FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (final IOException e) {
            throw new ResourcePreconditionException("An I/O error occurs");
        }
        return convFile;
    }
}
