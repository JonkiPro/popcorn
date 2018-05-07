package com.jonki.popcorn.web.controller.advice;

import com.jonki.popcorn.common.dto.error.ErrorFieldDTO;
import com.jonki.popcorn.common.dto.error.ValidationErrorDTO;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * A handler to capture erroneous fields in methods parameters.
 */
@RestControllerAdvice
public class IncorrectParametersExceptionHandler {

    /**
     * @param ex {@link ConstraintViolationException}
     * @return Returns an object with a list of incorrect parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDTO processValidationError(final ConstraintViolationException ex) {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();

        final Set<ConstraintViolation<?>> set =  ex.getConstraintViolations();

        for (Iterator<ConstraintViolation<?>> iterator = set.iterator();iterator.hasNext(); ) {
            ConstraintViolation<?> next =  iterator.next();

            validationErrorDTO.getFieldErrors()
                    .add(new ErrorFieldDTO(((PathImpl)next.getPropertyPath()).getLeafNode().getName(),
                                                next.getMessage()));
        }

        return validationErrorDTO;
    }
}
