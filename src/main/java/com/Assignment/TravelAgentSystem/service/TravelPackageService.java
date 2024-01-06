package com.Assignment.TravelAgentSystem.service;

import com.Assignment.TravelAgentSystem.entity.Activity;
import com.Assignment.TravelAgentSystem.entity.Destination;
import com.Assignment.TravelAgentSystem.entity.Passenger;
import com.Assignment.TravelAgentSystem.entity.TravelPackage;
import com.Assignment.TravelAgentSystem.exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.repository.ActivityRepository;
import com.Assignment.TravelAgentSystem.repository.DestinationRepository;
import com.Assignment.TravelAgentSystem.repository.PassengerRepository;
import com.Assignment.TravelAgentSystem.repository.TravelPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelPackageService {

    @Autowired
    private TravelPackageRepository travelPackageRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    public void addTravelPackageDetails(TravelPackage travelPackage) {

        if(travelPackageRepository.existsById(travelPackage.getId())){
            throw new IdAlreadyExistException("Travel package with ID "+travelPackage.getId()+" already exists.");
        }
        travelPackageRepository.save(travelPackage);
    }

    public String getTravelPackageItinerary(long travelPackageId) {

        TravelPackage travelPackage=travelPackageRepository.findById(travelPackageId).orElseThrow(()->new ResourceNotFoundException("Travel Package not found with this id: "+travelPackageId));

        StringBuilder itinerary=new StringBuilder();
        itinerary.append("Travel Package name: ").append(travelPackage.getName()).append("\n");


        List<Destination> destinations=destinationRepository.findByTravelPackageId(travelPackageId);
        for(Destination destination : destinations){
            itinerary.append("\nDestination: ").append(destination.getName()).append("\n");


            List<Activity> activities=activityRepository.findByDestinationId(destination.getId());
            for(Activity activity : activities){
                itinerary.append("Activity Name: ").append(activity.getName()).append("\n")
                        .append("Cost: ").append(activity.getCost()).append("\n")
                        .append("Capacity: ").append(activity.getCapacity()).append("\n")
                        .append("Description: ").append(activity.getDescription()).append("\n");
            }
        }

        return itinerary.toString();
    }

    public String getTravelPackagePassengerList(long travelPackageId) {

        TravelPackage travelPackage=travelPackageRepository.findById(travelPackageId).orElseThrow(()->new ResourceNotFoundException("Travel Package not found with this id: "+travelPackageId));

        StringBuilder passengerList = new StringBuilder();
        passengerList.append("Travel Package Name: ").append(travelPackage.getName()).append("\n")
                .append("Passenger Capacity: ").append(travelPackage.getCapacity()).append("\n");

        List<Passenger> passengers = passengerRepository.findByTravelPackageId(travelPackageId);
        int numberOfPassengers = passengers.size();

        passengerList.append("Number of Passengers Enrolled: ").append(numberOfPassengers).append("\n");

        for(Passenger passenger : passengers){
            passengerList.append("Passenger Name: ").append(passenger.getName()).append("\n")
                    .append("Passenger Number: ").append(passenger.getNumber()).append("\n");
        }

        return passengerList.toString();
    }
}
