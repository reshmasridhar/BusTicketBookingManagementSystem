package com.busbooking.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.busbooking.enums.BusType;

public class BusResponse {
	
	private Long busId;
    private String busNumber;
    private String busName;
    private BusType busType;
    private int totalSeats;
    private int availableSeats;

    private String createdBy;
    private LocalDateTime createdAt;

    private List<SeatResponse> seats;

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public String getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public BusType getBusType() {
		return busType;
	}

	public void setBusType(BusType busType) {
		this.busType = busType;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<SeatResponse> getSeats() {
		return seats;
	}

	public void setSeats(List<SeatResponse> seats) {
		this.seats = seats;
	}
    
    

}
