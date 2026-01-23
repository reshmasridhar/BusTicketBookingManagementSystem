package com.busbooking.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class BookingRequest {
	
	@NotNull
	private Long scheduleId;
	
	@NotNull
    private List<PassengerRequest> passengers;
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public List<PassengerRequest> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PassengerRequest> passengers) {
		this.passengers = passengers;
	}
    
    

}
