package com.busbooking.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.busbooking.dto.response.BusResponse;
import com.busbooking.dto.response.SeatResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Seat;

public class BusMapper {
	
	public static BusResponse toResponse(Bus bus) {

        BusResponse response = new BusResponse();
        response.setBusId(bus.getBusId());
        response.setBusNumber(bus.getBusNumber());
        response.setBusName(bus.getBusName());
        response.setBusType(bus.getBusType());
        response.setTotalSeats(bus.getTotalSeats());
        response.setAvailableSeats(bus.getAvailableSeats());
        response.setCreatedBy(bus.getCreatedBy());
        response.setCreatedAt(bus.getCreatedAt());

        List<SeatResponse> seatResponses = bus.getSeats()
                .stream()
                .map(BusMapper::mapSeat)
                .collect(Collectors.toList());

        response.setSeats(seatResponses);

        return response;
    }
	
	private static SeatResponse mapSeat(Seat seat) {
        SeatResponse response = new SeatResponse();
        response.setSeatId(seat.getSeatId());
        response.setSeatNumber(seat.getSeatNumber());
        response.setSeatType(seat.getSeatType());
        response.setSeatFare(seat.getSeatFare());
        return response;
    }

}
