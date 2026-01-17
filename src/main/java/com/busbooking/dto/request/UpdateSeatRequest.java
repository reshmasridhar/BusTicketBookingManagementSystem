package com.busbooking.dto.request;

import jakarta.validation.constraints.NotNull;

public class UpdateSeatRequest {
	
	
	@NotNull
	private Double seatFare;
	
	public UpdateSeatRequest() {
    }

	public Double getSeatFare() {
		return seatFare;
	}

	public void setSeatFare(Double seatFare) {
		this.seatFare = seatFare;
	}

	
	
	

}
