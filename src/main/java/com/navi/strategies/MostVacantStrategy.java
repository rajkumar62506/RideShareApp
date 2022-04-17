package com.navi.strategies;

import com.navi.models.Ride;

import java.util.List;

public class MostVacantStrategy {
    public Ride matchedRide(String userName, List<Ride> preferredList, int seatsRequired) {

        Ride maximumVacantSeatsRide = null;
        int maximumSeats = 0;

        for(Ride ride: preferredList) {
            if(ride.getAvailableSeats() > maximumSeats) {
                maximumVacantSeatsRide = ride;
                maximumSeats = ride.getAvailableSeats();
            }
        }
        return maximumVacantSeatsRide;
    }
}
