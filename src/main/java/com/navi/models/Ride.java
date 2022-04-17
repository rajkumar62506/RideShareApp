package com.navi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ride {
    private String userName;
    private String origin;
    private String destination;
    private String vehicleType;
    private String vehicleId;
    private int availableSeats;
}
