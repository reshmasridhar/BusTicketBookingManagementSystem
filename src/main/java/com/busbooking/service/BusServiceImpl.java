package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.response.BusResponse;
import com.busbooking.entity.Bus;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.mapper.BusMapper;
import com.busbooking.repository.BusRepository;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public BusResponse addBus(Bus bus) {

        bus.setAvailableSeats(bus.getTotalSeats());
        bus.setCreatedAt(LocalDateTime.now());

        Bus savedBus = busRepository.save(bus);

        return BusMapper.toResponse(savedBus);
    }

    @Override
    public BusResponse updateBus(Long busId, Bus bus) {

        Bus existingBus = busRepository.findById(busId)
                .orElseThrow(() -> new BusNotFoundException("Bus not found"));

        existingBus.setBusName(bus.getBusName());
        existingBus.setBusType(bus.getBusType());
        existingBus.setTotalSeats(bus.getTotalSeats());
        existingBus.setAvailableSeats(bus.getAvailableSeats());
        existingBus.setUpdatedBy(bus.getUpdatedBy());
        existingBus.setUpdatedAt(LocalDateTime.now());

        Bus updatedBus = busRepository.save(existingBus);

        return BusMapper.toResponse(updatedBus);
    }

    @Override
    public void deleteBus(Long busId) {

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new BusNotFoundException("Bus not found"));

        busRepository.delete(bus);
    }

    @Override
    public BusResponse getBusById(Long busId) {
    	Bus bus = busRepository.findById(busId)
                .orElseThrow(() ->
                        new BusNotFoundException("Bus not found with id: " + busId));

        return BusMapper.toResponse(bus);

        
    }

    @Override
    public List<BusResponse> getAllBuses() {
        return busRepository.findAll()
                .stream()
                .map(BusMapper::toResponse)
                .toList();
    }
}
