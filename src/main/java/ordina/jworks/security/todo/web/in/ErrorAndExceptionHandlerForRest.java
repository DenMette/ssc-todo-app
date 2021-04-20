package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.web.in.resource.ErrorResource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@ControllerAdvice(annotations = RestController.class)
public class ErrorAndExceptionHandlerForRest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ConstraintViolationException.class)
    public HttpEntity<List<ErrorResource>> handleConstraintViolationException(ConstraintViolationException exception) {
        final List<ErrorResource> errorResources = exception.getConstraintViolations().stream()
                .peek(constraintViolation -> logger.debug("Constraint violation has occurred: {} {}",
                        constraintViolation.getMessage(), constraintViolation.getPropertyPath()))
                .map(violation -> new ErrorResource(HttpStatus.BAD_REQUEST, exception, violation.getMessage(), violation.getPropertyPath().toString()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(errorResources, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpEntity<List<ErrorResource>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final List<ErrorResource> errorResources = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errorResources.add(new ErrorResource(HttpStatus.BAD_REQUEST, exception, error.getDefaultMessage(), error.getField()))
        );
        exception.getBindingResult().getGlobalErrors().forEach(error ->
                errorResources.add(new ErrorResource(HttpStatus.BAD_REQUEST, exception, error.getDefaultMessage(), error.getObjectName()))
        );
        return new ResponseEntity<>(errorResources, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public HttpEntity<ErrorResource> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return handleException(HttpStatus.BAD_REQUEST, exception, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public HttpEntity<ErrorResource> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return handleException(HttpStatus.BAD_REQUEST, exception, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public HttpEntity<ErrorResource> handleIllegalArgumentException(IllegalArgumentException exception) {
        return handleException(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(NullPointerException.class)
    public HttpEntity<ErrorResource> handleNullPointerException(NullPointerException exception) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public HttpEntity<ErrorResource> handleAnyOtherException(Exception exception) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getMessage());
    }

    private HttpEntity<ErrorResource> handleException(HttpStatus status, Throwable exception) {
        return handleException(status, exception, null);
    }

    private HttpEntity<ErrorResource> handleException(HttpStatus status, Throwable exception, String message) {
        return handleException(status, exception, message, null);
    }

    private HttpEntity<ErrorResource> handleException(HttpStatus status, Throwable exception, String message, String code) {
        this.logger.error("An Exception was encountered: {}", ExceptionUtils.getStackTrace(exception));
        final ErrorResource errorResource = new ErrorResource(status, exception, message, code);
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResource);
    }
}
