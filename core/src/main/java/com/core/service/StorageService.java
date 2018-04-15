package com.core.service;

import com.common.dto.StorageDirectory;
import com.common.exception.ResourcePreconditionException;
import com.common.exception.ResourceServerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Interface for dealing with files.
 */
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public interface StorageService {

    /**
     * Save the given file.
     *
     * @param file The file to save
     * @param storageDirectory Directory of file storage
     * @return The ID of the file saved in the cloud
     * @throws ResourcePreconditionException if an I/O error occurs
     * @throws ResourceServerException if incorrect authorization or the initialization of the request fails
     */
    String save (
            @NotNull final File file,
            @NotNull final StorageDirectory storageDirectory
    ) throws ResourcePreconditionException, ResourceServerException;

    /**
     * Delete the given file.
     *
     * @param fileId The file ID in the cloud
     * @throws ResourcePreconditionException if an I/O error occurs
     * @throws ResourceServerException if incorrect authorization or the initialization of the request fails
     */
    void delete(
            @NotBlank final String fileId
    ) throws ResourcePreconditionException, ResourceServerException;
}
