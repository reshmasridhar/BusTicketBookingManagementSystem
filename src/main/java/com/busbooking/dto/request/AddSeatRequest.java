package com.busbooking.dto.request;

import com.busbooking.enums.SeatType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddSeatRequest {

	    @NotBlank
	    private String seatNumber;

	    @NotNull
	    private SeatType seatType;

	    @NotNull
	    private Double seatFare;

		public String getSeatNumber() {
			return seatNumber;
		}

		public void setSeatNumber(String seatNumber) {
			this.seatNumber = seatNumber;
		}

		public SeatType getSeatType() {
			return seatType;
		}

		public void setSeatType(SeatType seatType) {
			this.seatType = seatType;
		}

		public Double getSeatFare() {
			return seatFare;
		}

		public void setSeatFare(Double seatFare) {
			this.seatFare = seatFare;
		}

	    
}
