package com.busbooking.dto.response;

public class PassengerResponse {

	
	private Long passengerId;
    private String name;
    private String gender;
    private String seatNumber;
    private double seatFare;
	public Long getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(Long passengerId) {
		this.passengerId = passengerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public double getSeatFare() {
		return seatFare;
	}
	public void setSeatFare(double seatFare) {
		this.seatFare = seatFare;
	}
    
    
}
