package com.Assignment.TravelAgentSystem.controller;

import com.Assignment.TravelAgentSystem.entity.Destination;
import com.Assignment.TravelAgentSystem.exceptions.ValidationException;
import com.Assignment.TravelAgentSystem.service.DestinationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/destination")
public class DestinationController {
    private static final Logger logger = LoggerFactory.getLogger(DestinationController.class);
    @Autowired
    private DestinationService destinationService;

    @PostMapping("/addDestination/{travelPackageId}")
    public ResponseEntity<String> addDestinationForTravelPackage(
            @PathVariable long travelPackageId,
            @Valid @RequestBody Destination destination) {

        logger.info("Adding destination for travel package with ID: {}", travelPackageId);

        try {
            destinationService.addDestinationForTravelPackage(travelPackageId, destination);
            logger.info("Destination added successfully");
            return new ResponseEntity<>("Destination added successfully", HttpStatus.OK);
        }catch (ValidationException e){
            logger.error("Validation error while adding destination: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
