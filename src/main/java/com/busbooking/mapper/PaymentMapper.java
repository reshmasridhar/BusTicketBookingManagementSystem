package com.busbooking.mapper;

import com.busbooking.dto.response.PaymentResponse;
import com.busbooking.entity.Payment;

public class PaymentMapper {
	
	public static PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setBookingId(payment.getBooking().getBookingId());
        response.setAmount(payment.getAmount());
        response.setPaymentMode(payment.getPaymentMode());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setPaymentTime(payment.getPaymentTime());

        return response;
    }

}
