package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.entity.Bus;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.repository.BusRepository;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public Bus addBus(Bus bus) {

        bus.setAvailableSeats(bus.getTotalSeats());
        bus.setCreatedAt(LocalDateTime.now());

        return busRepository.save(bus);
    }

    @Override
    public Bus updateBus(Long busId, Bus bus) {

        Bus existingBus = busRepository.findById(busId)
                .orElseThrow(() -> new BusNotFoundException("Bus not found"));

        existingBus.setBusName(bus.getBusName());
        existingBus.setBusType(bus.getBusType());
        existingBus.setTotalSeats(bus.getTotalSeats());
        existingBus.setAvailableSeats(bus.getAvailableSeats());
        existingBus.setUpdatedBy(bus.getUpdatedBy());
        existingBus.setUpdatedAt(LocalDateTime.now());

        return busRepository.save(existingBus);
    }

    @Override
    public void deleteBus(Long busId) {

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new BusNotFoundException("Bus not found"));

        busRepository.delete(bus);
    }

    @Override
    public Bus getBusById(Long busId) {

        return busRepository.findById(busId)
                .orElseThrow(() -> new BusNotFoundException("Bus not found"));
    }

    @Override
    public List<Bus> getAllBuses() {

        return busRepository.findAll();
    }
}
