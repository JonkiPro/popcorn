package com.jonki.popcorn.common.dto.request.validator.impl;

import com.jonki.popcorn.common.dto.request.validator.ValidImage;
import com.jonki.popcorn.common.exception.ResourcePreconditionException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Validate the image file. Implementation of the ValidImage annotation.
 */
public class ImageValidator implements ConstraintValidator<ValidImage, File> {

    @Override
    public void initialize(ValidImage constraintAnnotation) {}

    @Override
    public boolean isValid(File file, ConstraintValidatorContext context) {
        if(Optional.ofNullable(file).isPresent()) {
            String contentType;
            try {
                contentType = Files.probeContentType(file.toPath());
            } catch (IOException e) {
                throw new ResourcePreconditionException("An I/O error occurs");
            }
            return contentType.equals("image/png")
                    || contentType.equals("image/jpeg");
        }

        return true;
    }
}
