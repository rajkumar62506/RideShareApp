package com.navi.repositories;

import com.navi.models.Ride;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakenRideRepository {
    private Map<Ride, List<String>> takenRides = new HashMap<>();
}
