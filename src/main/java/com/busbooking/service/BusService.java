package com.busbooking.service;

import java.util.List;

import com.busbooking.dto.response.BusResponse;
import com.busbooking.entity.Bus;

public interface BusService {
	
	BusResponse addBus(Bus bus);

    BusResponse updateBus(Long busId, Bus bus);

    void deleteBus(Long busId);

    BusResponse getBusById(Long busId);

    List<BusResponse> getAllBuses();
    
    

}
