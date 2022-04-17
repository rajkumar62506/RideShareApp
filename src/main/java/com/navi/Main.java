package com.navi;

import com.navi.enums.Gender;
import com.navi.exceptions.NoRidesFoundException;
import com.navi.exceptions.SeatsRequiredException;
import com.navi.exceptions.UserVehicleAlreadyExistsException;
import com.navi.models.Ride;
import com.navi.models.User;
import com.navi.models.Vehicle;
import com.navi.repositories.RideRepository;
import com.navi.repositories.TakenRideRepository;
import com.navi.repositories.UserRepository;
import com.navi.repositories.UserVehicleRepository;
import com.navi.services.RideOfferService;
import com.navi.services.RideSelectService;
import com.navi.strategies.MostVacantStrategy;
import com.navi.strategies.PreferredVehicleStrategy;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Main {
    
    private static final UserRepository userRepository = new UserRepository();
    private static final RideRepository rideRepository = new RideRepository();
    private static final MostVacantStrategy mostVacantStrategy = new MostVacantStrategy();
    private static final PreferredVehicleStrategy preferredVehicleStrategy = new PreferredVehicleStrategy();
    private static final RideOfferService rideOfferService = new RideOfferService(rideRepository, userRepository);
    private static final TakenRideRepository takenRideRepository = new TakenRideRepository();
    private static final RideSelectService rideSelectService = new RideSelectService(rideRepository, preferredVehicleStrategy, mostVacantStrategy, userRepository, takenRideRepository);
    private static final UserVehicleRepository userVehicleRepository = new UserVehicleRepository();

    static void add_user(String name, Gender gender, int age) {
        List<Ride> ride1 = new ArrayList<>();
        List<Ride> ride2 = new ArrayList<>();
        User user = new User(name, gender, age, 0, 0, ride1, ride2);
        userRepository.saveUser(user);
    }

    static void add_vehicle(String userName, String vehicleName, String vehicleId) {
        Vehicle vehicle = new Vehicle(userName, vehicleName, vehicleId);
    }

    static void offer_ride(String userName, String origin, int availableSeats, String vehicleName, String vehicleId, String destination) {
        Map<String, Set<String>> userVehicle = userVehicleRepository.getUserVehicle();
        if(userVehicle.containsKey(userName)) {
            Set<String> vehicles = userVehicle.get(userName);
            if(vehicles.contains(vehicleId)) {
                throw new UserVehicleAlreadyExistsException("Ride has already been offered by this user for this vehicle");
            }
            else userVehicle.get(userName).add(vehicleId);
        }
        else {
            Set<String> vehicles = new HashSet<>();
            vehicles.add(vehicleId);
            userVehicle.put(userName, vehicles);
        }
        rideOfferService.offerRide(userName, origin, availableSeats, vehicleName, vehicleId, destination);
    }

    static void select_ride(String userName, String origin, String destination, int requiredSeats, String strategy) {
        if(requiredSeats <= 0 || requiredSeats > 2) {
            throw new SeatsRequiredException("Required seats must be among 1 and 2");
        }
        Ride ride = rideSelectService.selectRide(userName, origin, destination, requiredSeats, strategy);
        if(ride == null) {
            throw new NoRidesFoundException("No rides found for the selected origin and destination!");
        }
        log.info(String.valueOf(ride));
    }

//    static void printRideMap() {
//        System.out.println("------------------------------------------------------------");
//        Map<Pair<String, String>, List<Ride>> rideMap = rideRepository.getRideMap();
//        for(Map.Entry entry: rideMap.entrySet()) {
//            List<Ride> rides = (List<Ride>) entry.getValue();
//           // System.out.println("key : " + entry.getKey() + "  ride : " + entry.getValue());
//        }
//    }

    static void end_ride(Ride ride) {
        //printRideMap();
        Map<Pair<String, String>, List<Ride>> rideMap = rideRepository.getRideMap();
        List<Ride> rides = rideMap.get(new Pair<>(ride.getOrigin(), ride.getDestination()));
        Map<Ride, List<String>> takenRides = takenRideRepository.getTakenRides();

        for(Ride r: rides) {
            if(r.getOrigin().equals(ride.getOrigin()) &&
               r.getVehicleType().equals(ride.getVehicleType()) &&
               r.getDestination().equals(ride.getDestination()) &&
               r.getUserName().equals(ride.getUserName()) &&
               r.getVehicleId().equals(ride.getVehicleId())) {
                rides.remove(r);
                break;
            }
        }

        for(Map.Entry entry: takenRides.entrySet()) {
            Ride r = (Ride) entry.getKey();
            if(r.getOrigin().equals(ride.getOrigin()) &&
                    r.getVehicleType().equals(ride.getVehicleType()) &&
                    r.getDestination().equals(ride.getDestination()) &&
                    r.getUserName().equals(ride.getUserName()) &&
                    r.getVehicleId().equals(ride.getVehicleId())) {
                for(String name: (List<String>) entry.getValue()) {
                    User user = userRepository.getUserByName(name);
                    user.setTotalRidesTaken(user.getTotalRidesTaken() + 1);
                    userRepository.saveUser(user);
                }
                takenRides.remove(r);
                break;
            }
        }

            String name = ride.getUserName();
            User user = userRepository.getUserByName(name);
            user.setTotalRidesOffered(user.getTotalRidesOffered() + 1);
            userRepository.saveUser(user);
    }

    static void print_ride_stats() {
        Map<String, User> userMap = userRepository.getUserMap();
        for(Map.Entry entry: userMap.entrySet()) {
            String name = (String) entry.getKey();
            User user = (User) entry.getValue();
            int numberOfRidesOffered = user.getTotalRidesOffered();
            int numberofRidesTaken = user.getTotalRidesTaken();

            System.out.println(name + " : " + numberofRidesTaken + " Taken, " + numberOfRidesOffered + " Offered");
        }
    }
    public static void main(String[] args) {
        add_user("Rohan", Gender.M, 36);
        add_vehicle("Rohan", "Swift", "KA-01-12345");
        add_user("Shashank", Gender.M, 29);
        add_vehicle("Shashank", "Baleno", "TS-05-62395");
        add_user("Nandini", Gender.F, 29);
        add_user("Shipra", Gender.F, 27) ;
        add_vehicle("Shipra", "Polo", "KA-05-41491");
        add_vehicle("Shipra", "Activa", "KA-12-12332");
        add_user("Gaurav", Gender.M, 29);
        add_user("Rahul", Gender.M, 35);
        add_vehicle("Rahul", "XUV", "KA-05-1234");


        offer_ride("Rohan", "Hyderabad", 1, "Swift", "KA-01-12345", "Bangalore");
        offer_ride("Shipra","Bangalore", 1, "Activa","KA-12-12332", "Mysore");
        offer_ride("Shipra", "Bangalore", 2, "Polo", "KA-05-41491", "Mysore");
        offer_ride("Shashank", "Hyderabad", 2, "Baleno", "TS-05-62395", "Bangalore");
        offer_ride("Rahul", "Hyderabad", 5, "XUV", "KA-05-1234", "Bangalore");
        try {
            offer_ride("Rohan", "Bangalore", 1, "Swift", "KA-01-12345", "Pune");
        } catch (Exception e) {
            e.printStackTrace();
        }
        select_ride("Nandini", "Bangalore", "Mysore", 1, "Most Vacant");
        select_ride("Gaurav", "Bangalore", "Mysore", 1, "Preferred Vehicle=Activa");
        try {
            select_ride("Shashank", "Mumbai", "Bangalore", 1, "Most Vacant");
        } catch (Exception e) {
            e.printStackTrace();
        }
        select_ride("Rohan", "Hyderabad", "Bangalore", 1, "Preferred Vehicle=Baleno");
        try {
            select_ride("Shashank", "Hyderabad", "Bangalore", 1, "Preferred Vehicle=Polo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            select_ride("Shashank", "Hyderabad", "Bangalore", 5, "Preferred Vehicle=Polo");
        } catch (Exception e) {
            e.printStackTrace();
        }

        end_ride(new Ride("Rohan", "Hyderabad", "Bangalore", "Swift", "KA-01-12345", 1));
        end_ride(new Ride("Shipra","Bangalore", "Mysore", "Activa","KA-12-12332", 1));
        end_ride(new Ride("Shipra", "Bangalore", "Mysore", "Polo", "KA-05-41491", 2));
        end_ride(new Ride("Shashank", "Hyderabad", "Bangalore", "Baleno", "TS-05-62395", 2));

        print_ride_stats();
    }


}
