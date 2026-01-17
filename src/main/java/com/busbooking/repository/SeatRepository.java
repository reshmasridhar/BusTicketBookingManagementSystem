package com.busbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByBus_BusId(Long busId);
}