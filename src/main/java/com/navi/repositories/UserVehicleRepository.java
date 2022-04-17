package com.navi.repositories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVehicleRepository {
    Map<String, Set<String>> userVehicle = new HashMap<>();
}
