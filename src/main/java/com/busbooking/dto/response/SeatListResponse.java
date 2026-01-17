package com.busbooking.dto.response;

import com.busbooking.enums.SeatType;

public class SeatListResponse {

    private Long seatId;
    private String seatNumber;
    private SeatType seatType;
    private Double seatFare;

    public SeatListResponse(Long seatId, String seatNumber,
                            SeatType seatType, Double seatFare) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.seatFare = seatFare;
    }

    public Long getSeatId() {
        return seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public Double getSeatFare() {
        return seatFare;
    }
}
