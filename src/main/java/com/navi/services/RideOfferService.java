package com.navi.services;

import com.navi.models.Ride;
import com.navi.models.User;
import com.navi.repositories.RideRepository;
import com.navi.repositories.UserRepository;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class RideOfferService {
    private RideRepository rideRepository;
    private UserRepository userRepository;

    public void offerRide(String userName, String origin, int availableSeats, String vehicleName, String vehicleId, String destination) {
        Map<Pair<String, String>, List<Ride>> userMap = rideRepository.getRideMap();
        Pair<String, String> key = new Pair<>(origin, destination);
        Ride newRide = new Ride(userName, origin, destination, vehicleName, vehicleId, availableSeats);
        if(userMap.containsKey(key)) {
            userMap.get(key).add(newRide);
        }
        else {
            List<Ride> rideList = new ArrayList<>();
            rideList.add(newRide);
            userMap.put(key, rideList);
        }
    }
}
