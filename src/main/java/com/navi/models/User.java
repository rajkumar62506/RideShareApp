package com.navi.models;

import com.navi.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
public class User {
    private String userName;
    private Gender gender;
    private int age;
    private int totalRidesOffered;
    private int totalRidesTaken;
    List<Ride> offeredRides;
    List<Ride> takenRides;

    public void addOfferedRide(Ride ride) {
        this.offeredRides.add(ride);
    }
    public void addTakenRide(Ride ride) {
        this.takenRides.add(ride);
    }
}
