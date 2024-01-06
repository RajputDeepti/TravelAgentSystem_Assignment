package com.Assignment.TravelAgentSystem.service;

import com.Assignment.TravelAgentSystem.entity.Destination;
import com.Assignment.TravelAgentSystem.entity.TravelPackage;
import com.Assignment.TravelAgentSystem.exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.repository.DestinationRepository;
import com.Assignment.TravelAgentSystem.repository.TravelPackageRepository;
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
