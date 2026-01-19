package com.busbooking.dto.response;

import java.time.LocalDateTime;

import com.busbooking.enums.SeatType;

public class SeatResponse {

   
//    private String message;
//    private Long seatId;
//    private String createdBy;
//    private LocalDateTime createdAt;
   
	 private Long seatId;
	    private String seatNumber;
	    private SeatType seatType;
	    private double seatFare;
		public Long getSeatId() {
			return seatId;
		}
		public void setSeatId(Long seatId) {
			this.seatId = seatId;
		}
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
		public double getSeatFare() {
			return seatFare;
		}
		public void setSeatFare(double seatFare) {
			this.seatFare = seatFare;
		}
	    
	    
	    
}
