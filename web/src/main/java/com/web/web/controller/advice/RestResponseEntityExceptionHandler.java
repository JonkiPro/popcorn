package com.web.web.controller.advice;

import com.common.exception.*;
import com.common.dto.error.ErrorMessageDTO;
import org.springframework.http.HttpStatus;
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
     * @param ex {@link ResourceBadRequestException}
     * @return Returns an error status message to the customer
     */
    @ExceptionHandler(ResourceBadRequestException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceBadRequestException(final ResourceBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessageDTO.builder()
                                                                    .status(400)
                                                                    .code(400)
                                                                    .message(ex.getMessage())
                                                                    .build());
    }

    /**
     * @param ex {@link ResourceForbiddenException}
     * @return Returns an error status message to the customer
     */
    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceForbiddenException(final ResourceForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessageDTO.builder()
                                                                    .status(403)
                                                                    .code(403)
                                                                    .message(ex.getMessage())
                                                                    .build());
    }

    /**
     * @param ex {@link ResourceNotFoundException}
     * @return Returns an error status message to the customer
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessageDTO.builder()
                                                                    .status(404)
                                                                    .code(404)
                                                                    .message(ex.getMessage())
                                                                    .build());
    }

    /**
     * @param ex {@link ResourceConflictException}
     * @return Returns an error status message to the customer
     */
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceConflictException(final ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessageDTO.builder()
                                                                    .status(409)
                                                                    .code(409)
                                                                    .message(ex.getMessage())
                                                                    .build());
    }
}
