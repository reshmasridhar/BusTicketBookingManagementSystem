package com.busbooking.dto.response;

import java.time.LocalDateTime;

import com.busbooking.enums.SeatType;

public class SeatResponse {

   
    private String message;
    private Long seatId;
    private String createdBy;
    private LocalDateTime createdAt;
    public SeatResponse(String message, Long seatId, String createdBy, LocalDateTime createdAt) {
        this.message = message;
        this.seatId = seatId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }


    public SeatResponse() {
    }

    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
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
}
