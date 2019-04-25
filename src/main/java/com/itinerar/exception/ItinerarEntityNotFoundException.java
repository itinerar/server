package com.itinerar.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // if we dont't put this, the exception will return an exception fo type InternalServerError
public class ItinerarEntityNotFoundException extends RuntimeException {
	public ItinerarEntityNotFoundException(String message) {
		super(message);
	}
}

