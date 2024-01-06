package com.Assignment.TravelAgentSystem.controller;

import com.Assignment.TravelAgentSystem.entity.Activity;
import com.Assignment.TravelAgentSystem.exceptions.ValidationException;
import com.Assignment.TravelAgentSystem.service.ActivityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);


    @PostMapping("/add/{destinationId}")
    public ResponseEntity<String> addActivityForDestination(
            @PathVariable long destinationId,
            @Valid @RequestBody Activity activity) {

        logger.info("Adding activity for destination with ID: {}", destinationId);

        try {
            activityService.addActivityForDestination(destinationId, activity);
            logger.info("Activity added successfully");
            return new ResponseEntity<>("Activity added successfully", HttpStatus.OK);
        } catch (ValidationException e) {
            logger.error("Validation error while adding activity: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error while adding activity: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while adding activity", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<String> getAvailableActivities(){
        logger.info("Fetching available activities");

        String availableActivitiesDetails = activityService.getAvailableActivities();

        logger.info("Fetched available activities successfully");
        return new ResponseEntity<>(availableActivitiesDetails, HttpStatus.OK);
    }
}
