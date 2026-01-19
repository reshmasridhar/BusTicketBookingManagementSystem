package com.busbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.request.AddSeatRequest;
import com.busbooking.dto.request.UpdateSeatRequest;
import com.busbooking.dto.response.GenericResponse;
import com.busbooking.dto.response.SeatListResponse;
import com.busbooking.dto.response.SeatResponse;
import com.busbooking.entity.Seat;
import com.busbooking.service.SeatService;

@RestController
@RequestMapping("/api/admin/buses")
public class SeatController {

    @Autowired
    private SeatService seatService;
    
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{busId}/seats")
    public ResponseEntity<List<SeatListResponse>> getSeatsByBusId(
            @PathVariable Long busId) {

        List<SeatListResponse> seats = seatService.getSeatsByBusId(busId);
        return ResponseEntity.ok(seats);
    }




    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{busId}/seats")
    public ResponseEntity<SeatResponse> addSeat(
            @PathVariable Long busId,
            @RequestBody AddSeatRequest request) {

        Seat seat = new Seat();
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
        seat.setSeatFare(request.getSeatFare());

        Seat savedSeat = seatService.addSeat(busId, seat);

        SeatResponse response = new SeatResponse();
       // response.setMessage("Seat added successfully");
        response.setSeatId(savedSeat.getSeatId());
        response.setSeatNumber(savedSeat.getSeatNumber());
        response.setSeatType(savedSeat.getSeatType());
        response.setSeatFare(savedSeat.getSeatFare());
        
       // response.setCreatedBy(savedSeat.getCreatedBy());
      //  response.setCreatedAt(savedSeat.getCreatedAt());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{busId}/seats/{seatId}")
    public ResponseEntity<GenericResponse> updateSeat(
            @PathVariable Long busId,
            @PathVariable Long seatId,
            @RequestBody UpdateSeatRequest request) {

        Seat seat = seatService.updateSeat(busId, seatId, request.getSeatFare());

        return ResponseEntity.ok(
                new GenericResponse(
                        "Seat updated successfully",
                        seat.getUpdatedBy(),
                        seat.getUpdatedAt()
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{busId}/seats/{seatId}")
    public ResponseEntity<GenericResponse> deleteSeat(
            @PathVariable Long busId,
            @PathVariable Long seatId) {

        seatService.deleteSeat(busId, seatId);

        return ResponseEntity.ok(
                new GenericResponse("Seat deleted successfully")
        );
    }
}
