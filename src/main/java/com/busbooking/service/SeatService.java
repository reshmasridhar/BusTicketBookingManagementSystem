package com.busbooking.service;

import java.util.List;

import com.busbooking.dto.response.SeatListResponse;
import com.busbooking.dto.response.SeatResponse;
import com.busbooking.entity.Seat;

public interface SeatService {
	
	Seat addSeat(Long busId, Seat seat);

    Seat updateSeat(Long busId, Long seatId, Double seatFare);

    void deleteSeat(Long busId, Long seatId);

    List<Seat> getSeatsByBus(Long busId);

	List<SeatListResponse> getSeatsByBusId(Long busId);

}
