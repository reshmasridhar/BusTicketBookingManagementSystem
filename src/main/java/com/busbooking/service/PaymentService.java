package com.busbooking.service;

import java.util.List;

import com.busbooking.dto.request.PaymentRequest;
import com.busbooking.dto.response.PaymentResponse;

public interface PaymentService {
	
	PaymentResponse makePayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByBookingId(Long bookingId);
    
    List<PaymentResponse> getAllPayments();

}
