package com.Assignment.TravelAgentSystem.Service;

import com.Assignment.TravelAgentSystem.Entity.Activity;
import com.Assignment.TravelAgentSystem.Entity.Destination;
import com.Assignment.TravelAgentSystem.Exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.Exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.Repository.ActivityRepository;
import com.Assignment.TravelAgentSystem.Repository.DestinationRepository;
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
        activity.setDestination(destination);
        activityRepository.save(activity);
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