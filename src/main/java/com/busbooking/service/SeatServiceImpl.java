package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.response.SeatListResponse;
import com.busbooking.dto.response.SeatResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Seat;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.exception.SeatNotFoundException;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.SeatRepository;

@Service
public class SeatServiceImpl implements SeatService{

	@Autowired
	private SeatRepository seatRepository;
	
	@Autowired
	private BusRepository busRepository;
	
	
	
	@Override
	public Seat addSeat(Long busId, Seat seat) {
		// TODO Auto-generated method stub
		Bus bus = busRepository.findById(busId)
                .orElseThrow(() ->
                        new BusNotFoundException("Bus not found with id: " + busId));

        seat.setBus(bus);
        seat.setCreatedAt(LocalDateTime.now());
        seat.setCreatedBy("admin@bus.com");

        return seatRepository.save(seat);
	}

	@Override
	public Seat updateSeat(Long busId, Long seatId, Double seatFare) {
		// TODO Auto-generated method stub
		 Bus bus = busRepository.findById(busId)
	                .orElseThrow(() ->
	                        new BusNotFoundException("Bus not found with id: " + busId));

	        Seat seat = seatRepository.findById(seatId)
	                .orElseThrow(() ->
	                        new SeatNotFoundException("Seat not found with id: " + seatId));

	        if (!seat.getBus().getBusId().equals(bus.getBusId())) {
	            throw new SeatNotFoundException("Seat does not belong to this bus");
	        }

	        seat.setSeatFare(seatFare);
	        seat.setUpdatedAt(LocalDateTime.now());
	        seat.setUpdatedBy("admin@bus.com");

	        return seatRepository.save(seat);
	}

	@Override
	public void deleteSeat(Long busId, Long seatId) {
		// TODO Auto-generated method stub
		Bus bus = busRepository.findById(busId)
                .orElseThrow(() ->
                        new BusNotFoundException("Bus not found with id: " + busId));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() ->
                        new SeatNotFoundException("Seat not found with id: " + seatId));

        if (!seat.getBus().getBusId().equals(bus.getBusId())) {
            throw new SeatNotFoundException("Seat does not belong to this bus");
        }

        seatRepository.delete(seat);
		
		
	}

	@Override
	public List<Seat> getSeatsByBus(Long busId) {
		// TODO Auto-generated method stub
		if (!busRepository.existsById(busId)) {
            throw new BusNotFoundException("Bus not found with id: " + busId);
        }

        return seatRepository.findByBus_BusId(busId);
	}

	@Override
	public List<SeatListResponse> getSeatsByBusId(Long busId) {

	    Bus bus = busRepository.findById(busId)
	            .orElseThrow(() ->
	                    new BusNotFoundException("Bus not found with id: " + busId));

	    return bus.getSeats().stream()
	            .map(seat -> new SeatListResponse(
	                    seat.getSeatId(),
	                    seat.getSeatNumber(),
	                    seat.getSeatType(),
	                    seat.getSeatFare()
	            ))
	            .toList();
	}


}
