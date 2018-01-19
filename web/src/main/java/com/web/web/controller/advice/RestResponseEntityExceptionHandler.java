package com.web.web.controller.advice;

import com.common.dto.error.ErrorMessageDTO;
import com.common.exception.ResourceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A handler to capture HTTP statuses errors.
 */
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @param ex {@link ResourceException}
     * @return Returns an error status message to the customer
     */
    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceException(final ResourceException ex) {
        return ResponseEntity.status(ex.getErrorCode()).body(ErrorMessageDTO.builder()
                .status(ex.getErrorCode())
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build());
    }
}
