package com.Assignment.TravelAgentSystem.Service;

import com.Assignment.TravelAgentSystem.Entity.Destination;
import com.Assignment.TravelAgentSystem.Entity.TravelPackage;
import com.Assignment.TravelAgentSystem.Exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.Exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.Repository.DestinationRepository;
import com.Assignment.TravelAgentSystem.Repository.TravelPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private TravelPackageRepository travelPackageRepository;


    public void addDestinationForTravelPackage(long travelPackageId, Destination destination) {
        if(destinationRepository.existsById(destination.getId())){
            throw new IdAlreadyExistException("Destination with ID "+destination.getId()+" already exists.");
        }
        TravelPackage travelPackage = travelPackageRepository.findById(travelPackageId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Package not found with id: " + travelPackageId));

        destination.setTravelPackage(travelPackage);
        destinationRepository.save(destination);
    }
}
