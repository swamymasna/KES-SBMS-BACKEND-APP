package com.kes.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.kes.dto.ErrorMessage;
import com.kes.exception.DepartmentServiceBusinessException;
import com.kes.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest webRequest) {

		ErrorMessage errorMessage = ErrorMessage.builder().message(exception.getMessage()).status(HttpStatus.NOT_FOUND)
				.statusCode(HttpStatus.NOT_FOUND.value()).timeStamp(new Date().toString())
				.path(webRequest.getDescription(false)).build();

		return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DepartmentServiceBusinessException.class)
	public ResponseEntity<ErrorMessage> handleEmployeeServiceBusinessException(
			DepartmentServiceBusinessException exception, WebRequest webRequest) {

		ErrorMessage errorMessage = ErrorMessage.builder().message(exception.getMessage())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.timeStamp(new Date().toString()).path(webRequest.getDescription(false)).build();

		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			WebRequest request) {

		Map<String, String> errorsMap = new HashMap<>();

		exception.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();

			errorsMap.put(message, field);
		});

		return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
	}
}
