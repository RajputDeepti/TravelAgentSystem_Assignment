package com.Assignment.TravelAgentSystem.service;

import com.Assignment.TravelAgentSystem.entity.Destination;
import com.Assignment.TravelAgentSystem.entity.TravelPackage;
import com.Assignment.TravelAgentSystem.exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.repository.DestinationRepository;
import com.Assignment.TravelAgentSystem.repository.TravelPackageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DestinationServiceTest {

    @Mock
    private DestinationRepository destinationRepository;

    @Mock
    private TravelPackageRepository travelPackageRepository;

    @InjectMocks
    private DestinationService destinationService;

    @Test
    void testAddDestinationForTravelPackage_Success() {
        long travelPackageId = 1L;
        Destination destination = new Destination();
        destination.setId(1L);
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(travelPackageId);

        when(destinationRepository.existsById(destination.getId())).thenReturn(false);
        when(travelPackageRepository.findById(travelPackageId)).thenReturn(Optional.of(travelPackage));

        destinationService.addDestinationForTravelPackage(travelPackageId, destination);

        Mockito.verify(destinationRepository, Mockito.times(1)).save(destination);
    }

    @Test
    void testAddDestinationForTravelPackage_DestinationExists() {
        long travelPackageId = 1L;
        Destination destination = new Destination();
        destination.setId(1L);

        when(destinationRepository.existsById(destination.getId())).thenReturn(true);

        assertThrows(IdAlreadyExistException.class,
                () -> destinationService.addDestinationForTravelPackage(travelPackageId, destination));
    }

    @Test
    void testAddDestinationForTravelPackage_TravelPackageNotFound() {
        long travelPackageId = 1L;
        Destination destination = new Destination();
        destination.setId(1L);

        when(destinationRepository.existsById(destination.getId())).thenReturn(false);
        when(travelPackageRepository.findById(travelPackageId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> destinationService.addDestinationForTravelPackage(travelPackageId, destination));
    }
}
