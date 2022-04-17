package com.navi.strategies;

import com.navi.models.Ride;
import lombok.Data;

import java.util.List;

@Data
public class PreferredVehicleStrategy{
    public Ride matchedRide(String userName, List<Ride> preferredList, int seatsRequired, String strategy) {
        String vehicleName = strategy.substring(18);
        Ride validRide = null;
        for(Ride ride: preferredList) {
            if(ride.getVehicleType().equals(vehicleName) && ride.getAvailableSeats() >= seatsRequired) {
                validRide = ride;
                break;
            }
        }

        return validRide;
    }
}
