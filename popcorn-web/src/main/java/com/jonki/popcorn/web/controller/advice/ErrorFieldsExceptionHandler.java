package com.jonki.popcorn.web.controller.advice;

import com.jonki.popcorn.common.dto.error.ValidationErrorDTO;
import com.jonki.popcorn.common.exception.FormBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

/**
 * A handler to capture erroneous fields in DTO.
 */
@RestControllerAdvice
public class ErrorFieldsExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Constructor.
     *
     * @param messageSource The message source to use
     */
    @Autowired
    public ErrorFieldsExceptionHandler(
            final MessageSource messageSource
    ) {
        this.messageSource = messageSource;
    }

    /**
     * @param ex {@link MethodArgumentNotValidException}
     * @return Returns an object with a list of error fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDTO processValidationError(final MethodArgumentNotValidException ex) {
        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }

    /**
     * @param fieldErrors List with fields containing errors. {@link FieldError}
     * @return Returns an object containing a list of error fields
     */
    private ValidationErrorDTO processFieldErrors(final List<FieldError> fieldErrors) {
        final ValidationErrorDTO dto = new ValidationErrorDTO();

        for (final FieldError fieldError: fieldErrors) {
            final String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return dto;
    }

    /**
     * @param fieldError Encapsulates a field error, that is, a reason for rejecting a specific field value. {@link FieldError}
     * @return Returns an error message for the current location
     */
    private String resolveLocalizedErrorMessage(final FieldError fieldError) {
        final Locale currentLocale =  LocaleContextHolder.getLocale();

        return messageSource.getMessage(fieldError, currentLocale);
    }


    /**
     * @param ex {@link FormBadRequestException}
     * @return Returns an object containing a list of error fields
     */
    @ExceptionHandler(FormBadRequestException.class)
    public ResponseEntity<ValidationErrorDTO> handleFormBadRequestException(final FormBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrors());
    }
}
