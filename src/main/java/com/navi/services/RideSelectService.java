package com.navi.services;

import com.navi.models.Ride;
import com.navi.models.User;
import com.navi.repositories.RideRepository;
import com.navi.repositories.TakenRideRepository;
import com.navi.repositories.UserRepository;
import com.navi.strategies.MostVacantStrategy;
import com.navi.strategies.PreferredVehicleStrategy;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class RideSelectService {
    private RideRepository rideRepository;
    private PreferredVehicleStrategy preferredVehicleStrategy;
    private MostVacantStrategy mostVacantStrategy;
    private UserRepository userRepository;
    private TakenRideRepository takenRideRepository;

    public Ride selectRide(String userName, String origin, String destination, int seatsRequired, String strategy) {
        Map<Pair<String, String>, List<Ride>> rideMap = rideRepository.getRideMap();
        List<Ride> preferredList = rideMap.get(new Pair<>(origin, destination));
        if(preferredList == null) {
            return null;
        }

        Map<Ride, List<String>> takenMap = takenRideRepository.getTakenRides();
        Ride matchedRide = null;

        if(strategy.charAt(0) == 'M') {
            matchedRide = mostVacantStrategy.matchedRide(userName, preferredList, seatsRequired);

        }
        else {
            matchedRide = preferredVehicleStrategy.matchedRide(userName, preferredList, seatsRequired, strategy);
        }

        if(matchedRide != null) {
            matchedRide.setAvailableSeats(matchedRide.getAvailableSeats() - seatsRequired);

            //Update taken map
            if(takenMap.containsKey(matchedRide)) {
                takenMap.get(matchedRide).add(userName);
            } else {
                List<String> names = new ArrayList<>();
                names.add(userName);
                takenMap.put(matchedRide, names);
            }
        }

        return matchedRide;
    }
}
