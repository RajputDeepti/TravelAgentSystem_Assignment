package com.Assignment.TravelAgentSystem.Controller;

import com.Assignment.TravelAgentSystem.Entity.Passenger;
import com.Assignment.TravelAgentSystem.Exceptions.ValidationException;
import com.Assignment.TravelAgentSystem.Service.PassengerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);
    @Autowired
    private PassengerService passengerService;

    @PostMapping("/add/{travelPackageId}")
    public ResponseEntity<String> addPassengerForTravelPackage(
            @PathVariable long travelPackageId,
            @Valid @RequestBody Passenger passenger) {

        logger.info("Adding passenger for travel package with ID: {}", travelPackageId);

        try {
            passengerService.addPassengerForTravelPackage(travelPackageId, passenger);
            logger.info("Passenger added successfully");
            return new ResponseEntity<>("Passenger added successfully", HttpStatus.OK);
        } catch (ValidationException e) {
            logger.error("Validation error while adding passenger: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error while adding passenger: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while adding passenger", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signupActivity/{passengerId}/{activityId}")
    public ResponseEntity<String> signUpForActivity(
            @PathVariable long passengerId,
            @PathVariable long activityId) {

        logger.info("Passenger with ID {} signing up for activity with ID: {}", passengerId, activityId);

        passengerService.signUpForActivity(passengerId, activityId);

        logger.info("Passenger signed up for the activity successfully");
        return new ResponseEntity<>("Passenger signed up for the activity successfully.", HttpStatus.OK);
    }

    @GetMapping("/details/{passengerId}")
    public ResponseEntity<String> getPassengerDetails(@PathVariable long passengerId) {
        logger.info("Fetching details for passenger with ID: {}", passengerId);

        String passengerDetails = passengerService.getPassengerDetails(passengerId);

        logger.info("Fetched passenger details successfully");
        return new ResponseEntity<>(passengerDetails, HttpStatus.OK);
    }
}
