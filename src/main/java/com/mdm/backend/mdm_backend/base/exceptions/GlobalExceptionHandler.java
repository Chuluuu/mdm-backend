package com.mdm.backend.mdm_backend.base.exceptions;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle AccessDeniedException (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        logger.error("Serialization error occurred: {}", ex.getMessage(), ex);
        // Optionally, log specific details such as the method, file, and line number
        for (StackTraceElement element : ex.getStackTrace()) {
            logger.error("Error at {}:{}.{}", element.getClassName(), element.getMethodName(), element.getLineNumber());
            break;  // Log only the first relevant stack trace element
        }
        return new ResponseEntity<>("Access Denied: You don't have permission to access this resource.", HttpStatus.FORBIDDEN);
    }

    // Handle AuthenticationException (401 Unauthorized)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.error("Serialization error occurred: {}", ex.getMessage(), ex);
        // Optionally, log specific details such as the method, file, and line number
        for (StackTraceElement element : ex.getStackTrace()) {
            logger.error("Error at {}:{}.{}", element.getClassName(), element.getMethodName(), element.getLineNumber());
            break;  // Log only the first relevant stack trace element
        }
        return new ResponseEntity<>("Unauthorized: Please log in to access this resource.", HttpStatus.UNAUTHORIZED);
    }

    // Catch other unhandled exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Error occurred: {}", ex.getMessage(), ex);
        // Optionally, log specific details such as the method, file, and line number
        for (StackTraceElement element : ex.getStackTrace()) {
            logger.error("Error at {}:{}.{}", element.getClassName(), element.getMethodName(), element.getLineNumber());
            break;  // Log only the first relevant stack trace element
        }
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<String> handleInvalidDefinitionException(InvalidDefinitionException ex, WebRequest request) {
        logger.error("Serialization error occurred: {}", ex.getMessage(), ex);
        // Optionally, log specific details such as the method, file, and line number
        for (StackTraceElement element : ex.getStackTrace()) {
            logger.error("Error at {}:{}.{}", element.getClassName(), element.getMethodName(), element.getLineNumber());
            break;  // Log only the first relevant stack trace element
        }
        String errorMessage = "Serialization error: " + ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
