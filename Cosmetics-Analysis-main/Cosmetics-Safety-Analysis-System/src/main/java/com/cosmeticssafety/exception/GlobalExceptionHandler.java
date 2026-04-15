package com.cosmeticssafety.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
				.orElse("Validation failed");
		LOGGER.warn("Validation failed for path {}: {}", request.getRequestURI(), message);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception, HttpServletRequest request) {
		LOGGER.error("Unhandled exception for path {}", request.getRequestURI(), exception);
		String message = exception.getMessage() == null || exception.getMessage().isBlank()
				? "Unexpected server error"
				: exception.getMessage();
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request.getRequestURI());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
			HttpServletRequest request) {
		LOGGER.warn("Resource not found for path {}: {}", request.getRequestURI(), exception.getMessage());
		return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidTokenException(InvalidTokenException exception,
			HttpServletRequest request) {
		LOGGER.warn("Invalid token for path {}: {}", request.getRequestURI(), exception.getMessage());
		return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception,
			HttpServletRequest request) {
		LOGGER.warn("Illegal argument for path {}: {}", request.getRequestURI(), exception.getMessage());
		return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException exception,
			HttpServletRequest request) {
		LOGGER.warn("Authentication failed for path {}: {}", request.getRequestURI(), exception.getMessage());
		return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request.getRequestURI());
	}

	private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String message, String path) {
		ApiErrorResponse errorResponse = new ApiErrorResponse(LocalDateTime.now(), status.value(),
				status.getReasonPhrase(), message, path);
		return ResponseEntity.status(status).body(errorResponse);
	}
}
