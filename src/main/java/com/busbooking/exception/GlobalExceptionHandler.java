package com.busbooking.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.busbooking.dto.response.ErrorResponse;
import com.busbooking.dto.response.GenericResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
	
	
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(
	        UserNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}


	
	@ExceptionHandler(BusNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleBusNotFound(
	        BusNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	
	@ExceptionHandler(SeatNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleSeatNotFound(
	        SeatNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DriverNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleDriverNotFound(
	        DriverNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	
	

	@ExceptionHandler(ScheduleNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleScheduleNotFound(
	        ScheduleNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DriverAlreadyScheduledException.class)
	public ResponseEntity<ErrorResponse> handleDriverAlreadyScheduled(
	        DriverAlreadyScheduledException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BusAlreadyScheduledException.class)
	public ResponseEntity<ErrorResponse> handleBusAlreadyScheduled(
	        BusAlreadyScheduledException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DriverAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleDriverExists(
	        DriverAlreadyExistsException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.CONFLICT.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleBookingNotFound(
	        BookingNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePaymentNotFound(
	        PaymentNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PaymentAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handlePaymentAlreadyExists(
	        PaymentAlreadyExistsException ex,
	        HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI()
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	
	

}
