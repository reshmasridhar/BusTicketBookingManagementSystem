package com.busbooking.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.busbooking.enums.BookingStatus;

public class BookingResponse {
	
	 private Long bookingId;
	    private BookingStatus status;
	    private LocalDateTime bookingTime;
	    private List<PassengerResponse> passengers;
		public Long getBookingId() {
			return bookingId;
		}
		public void setBookingId(Long bookingId) {
			this.bookingId = bookingId;
		}
		public BookingStatus getStatus() {
			return status;
		}
		public void setStatus(BookingStatus status) {
			this.status = status;
		}
		public LocalDateTime getBookingTime() {
			return bookingTime;
		}
		public void setBookingTime(LocalDateTime bookingTime) {
			this.bookingTime = bookingTime;
		}
		public List<PassengerResponse> getPassengers() {
			return passengers;
		}
		public void setPassengers(List<PassengerResponse> passengers) {
			this.passengers = passengers;
		}
	    
	    

}
