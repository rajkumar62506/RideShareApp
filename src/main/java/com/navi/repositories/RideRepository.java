package com.navi.repositories;

import com.navi.models.Ride;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRepository {
    private Map<Pair<String, String>, List<Ride>> rideMap = new HashMap<>();
}
