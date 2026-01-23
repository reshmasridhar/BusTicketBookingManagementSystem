package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.dto.response.BusResponse;
import com.busbooking.entity.Bus;
import com.busbooking.exception.BusNotFoundException;
import com.busbooking.mapper.BusMapper;
import com.busbooking.repository.BusRepository;

@Service
public class BusServiceImpl implements BusService {

    private static final Logger logger =
            LoggerFactory.getLogger(BusServiceImpl.class);

    @Autowired
    private BusRepository busRepository;

    
    @Override
    public BusResponse addBus(Bus bus) {

        logger.info("Adding new bus with busNumber: {}", bus.getBusNumber());

        bus.setCreatedAt(LocalDateTime.now());

        Bus savedBus = busRepository.save(bus);

        logger.info("Bus added successfully with busId: {}", savedBus.getBusId());

        return BusMapper.toResponse(savedBus);
    }

    
    @Override
    public BusResponse updateBus(Long busId, Bus bus) {

        logger.info("Updating bus with id: {}", busId);

        Bus existingBus = busRepository.findById(busId)
                .orElseThrow(() -> {
                    logger.warn("Bus not found with id: {}", busId);
                    return new BusNotFoundException("Bus not found");
                });

        existingBus.setBusName(bus.getBusName());
        existingBus.setBusType(bus.getBusType());
        existingBus.setTotalSeats(bus.getTotalSeats());
        existingBus.setUpdatedBy(bus.getUpdatedBy());
        existingBus.setUpdatedAt(LocalDateTime.now());

        Bus updatedBus = busRepository.save(existingBus);

        logger.info("Bus updated successfully with id: {}", busId);

        return BusMapper.toResponse(updatedBus);
    }

    
    @Override
    public void deleteBus(Long busId) {

        logger.info("Deleting bus with id: {}", busId);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> {
                    logger.warn("Bus not found with id: {}", busId);
                    return new BusNotFoundException("Bus not found");
                });

        busRepository.delete(bus);

        logger.info("Bus deleted successfully with id: {}", busId);
    }

    
    @Override
    public BusResponse getBusById(Long busId) {

        logger.info("Fetching bus with id: {}", busId);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> {
                    logger.warn("Bus not found with id: {}", busId);
                    return new BusNotFoundException(
                            "Bus not found with id: " + busId);
                });

        logger.info("Bus fetched successfully with id: {}", busId);

        return BusMapper.toResponse(bus);
    }

    
    @Override
    public List<BusResponse> getAllBuses() {

        logger.info("Fetching all buses");

        List<BusResponse> buses = busRepository.findAll()
                .stream()
                .map(BusMapper::toResponse)
                .toList();

        logger.info("Total buses found: {}", buses.size());

        return buses;
    }
}
