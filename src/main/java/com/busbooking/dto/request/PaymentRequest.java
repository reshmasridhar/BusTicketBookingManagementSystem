package com.busbooking.dto.request;

import com.busbooking.enums.PaymentMode;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
	
	@NotNull
	private Long bookingId;
	
    private PaymentMode paymentMode;
	public Long getBookingId() {
		return bookingId;
	}
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}
	public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}
    
    

}
