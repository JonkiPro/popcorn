package com.service.app.rest.controller.advice;

import com.service.app.exception.ResourceConflictException;
import com.service.app.exception.ResourceForbiddenException;
import com.service.app.exception.ResourceNotFoundException;
import com.service.app.rest.response.ErrorMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessageDTO.builder()
                                                                    .status(404)
                                                                    .code(404)
                                                                    .message(ex.getMessage())
                                                                    .build());
    }

    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceForbiddenException(ResourceForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessageDTO.builder()
                                                                    .status(403)
                                                                    .code(403)
                                                                    .message(ex.getMessage())
                                                                    .build());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorMessageDTO> handleResourceConflictException(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessageDTO.builder()
                                                                    .status(409)
                                                                    .code(409)
                                                                    .message(ex.getMessage())
                                                                    .build());
    }
}
