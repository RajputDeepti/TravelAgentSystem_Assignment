package com.Assignment.TravelAgentSystem.Controller;

import com.Assignment.TravelAgentSystem.Entity.TravelPackage;
import com.Assignment.TravelAgentSystem.Service.TravelPackageService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travelPackage")
public class TravelPackageController {
    private static final Logger logger = LoggerFactory.getLogger(TravelPackageController.class);

    @Autowired
    private TravelPackageService travelPackageService;

    @PostMapping("/addDetails")
    public ResponseEntity<String> addTravelPackageDetails( @Valid @RequestBody TravelPackage travelPackage) {
        logger.info("Adding details for travel package");

        try {
            travelPackageService.addTravelPackageDetails(travelPackage);
            logger.info("Details added successfully");
            return new ResponseEntity<>("Details added successfully", HttpStatus.OK);
        } catch (ValidationException e) {
            logger.error("Validation error while adding travel package details: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error while adding travel package details: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while adding travel package details", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/itinerary/{travelPackageId}")
    public ResponseEntity<String> getTravelPackageItinerary(@PathVariable long travelPackageId) {
        logger.info("Fetching itinerary for travel package with ID: {}", travelPackageId);

        String itinerary = travelPackageService.getTravelPackageItinerary(travelPackageId);

        logger.info("Fetched itinerary successfully");
        return new ResponseEntity<>(itinerary, HttpStatus.OK);
    }

    @GetMapping("/passengerList/{travelPackageId}")
    public ResponseEntity<String> getTravelPackagePassengerList(@PathVariable long travelPackageId) {
        logger.info("Fetching passenger list for travel package with ID: {}", travelPackageId);

        String passengerList = travelPackageService.getTravelPackagePassengerList(travelPackageId);

        logger.info("Fetched passenger list successfully");
        return new ResponseEntity<>(passengerList, HttpStatus.OK);
    }
}
