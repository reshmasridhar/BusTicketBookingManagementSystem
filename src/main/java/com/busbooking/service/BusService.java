package com.busbooking.service;

import java.util.List;

import com.busbooking.entity.Bus;

public interface BusService {
	
	Bus addBus(Bus bus);

    Bus updateBus(Long busId, Bus bus);

    void deleteBus(Long busId);

    Bus getBusById(Long busId);

    List<Bus> getAllBuses();

}
