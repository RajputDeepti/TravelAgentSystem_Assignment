package com.Assignment.TravelAgentSystem.service;

import com.Assignment.TravelAgentSystem.entity.Activity;
import com.Assignment.TravelAgentSystem.entity.Destination;
import com.Assignment.TravelAgentSystem.exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.repository.ActivityRepository;
import com.Assignment.TravelAgentSystem.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    public void addActivityForDestination(long destinationId, Activity activity) {
        if(activityRepository.existsById(activity.getId())){
            throw new IdAlreadyExistException("Activity with ID "+activity.getId()+ " already exists.");
        }
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + destinationId));
        if (destination != null) {
            activity.setDestination(destination);
            activityRepository.save(activity);
        } else {
            throw new ResourceNotFoundException("Destination not found with id: " + destinationId);
        }
    }

    public String getAvailableActivities() {
        List<Activity> availableActivities = activityRepository.findAll()
                .stream()
                .filter(activity -> activity.getCapacity() > activity.getPassengers().size())
                .collect(Collectors.toList());

        StringBuilder availableActivitiesDetails= new StringBuilder();

        if(!availableActivities.isEmpty()){
            availableActivitiesDetails.append("Activities with available spaces:\n");

            for(Activity activity: availableActivities){
                int spacesAvailable = activity.getCapacity() - activity.getPassengers().size();
                availableActivitiesDetails.append("Activity Name: ").append(activity.getName()).append("\n")
                        .append("Destination: ").append(activity.getDestination().getName()).append("\n")
                        .append("Spaces Available: ").append(spacesAvailable).append("\n");
            }
        }
        else {
            availableActivitiesDetails.append("No activities with available spaces.\n");
        }
        return availableActivitiesDetails.toString();
    }
}
