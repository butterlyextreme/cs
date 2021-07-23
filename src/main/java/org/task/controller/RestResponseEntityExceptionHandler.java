package org.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.stream.Collectors.joining;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  // Default exception handler, in case of unexpected errors, we still need to handle them properly.
  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
    log.error(String
            .format("Request to '%s' finished with error: %s", extractPath(request), ex.getMessage()),
        ex);
    // All unhandled errors must be returned as 500:
    return handleExceptionInternal(ex, "Generic Internal Error", new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(value = {ResourceNotFoundException.class})
  protected ResponseEntity<Object> handleConflict(ResourceNotFoundException ex,
                                                  WebRequest request) {
    log.error("Request to '{}' finished with error: {}", extractPath(request), ex.getMessage());
    return handleExceptionInternal(ex, "Generic NotFound Error", new HttpHeaders(),
        HttpStatus.NOT_FOUND, request);
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    String errors = ex.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
        .collect(joining(";"));
    log.error("Request to '{}' finished with error: {}", extractPath(request), errors);
    return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.error("Request to '{}' finished with error: {}", extractPath(request), ex.getMessage());
    return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  private String extractPath(WebRequest request) {
    if (request instanceof ServletWebRequest) {
      return ((ServletWebRequest) request).getRequest().getRequestURI();
    } else {
      return "";
    }
  }
}
