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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
public class TravelPackageServiceTest {

    @Autowired
    private TravelPackageService travelPackageService;

    @MockBean
    private TravelPackageRepository travelPackageRepository;

    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private DestinationRepository destinationRepository;

    @MockBean
    private PassengerRepository passengerRepository;

    @Test
    public void testAddTravelPackageDetails_Success() {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(1L);
        travelPackage.setName("Test Package");
        travelPackage.setCapacity(10);

        when(travelPackageRepository.existsById(1L)).thenReturn(false);
        when(travelPackageRepository.save(any(TravelPackage.class))).thenReturn(travelPackage);

        travelPackageService.addTravelPackageDetails(travelPackage);

        verify(travelPackageRepository, times(1)).save(travelPackage);
    }

    @Test
    public void testGetTravelPackageItinerary_Success() {
        long travelPackageId = 1L;
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(travelPackageId);
        travelPackage.setName("Test Package");
        travelPackage.setCapacity(10);

        Destination destination = new Destination();
        destination.setId(1);
        destination.setName("Destination 1");
        destination.setTravelPackage(travelPackage);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("Activity 1");
        activity.setCost(50.0);
        activity.setCapacity(20);
        activity.setDescription("Description for Activity 1");
        activity.setDestination(destination);

        when(travelPackageRepository.findById(travelPackageId)).thenReturn(Optional.of(travelPackage));
        when(destinationRepository.findByTravelPackageId(travelPackageId)).thenReturn(Collections.singletonList(destination));
        when(activityRepository.findByDestinationId(destination.getId())).thenReturn(Collections.singletonList(activity));

        String itinerary = travelPackageService.getTravelPackageItinerary(1L);

        StringBuilder expectedItinerary = new StringBuilder();
        expectedItinerary.append("Travel Package name: Test Package\n")
                .append("\nDestination: Destination 1\n")
                .append("Activity Name: Activity 1\n")
                .append("Cost: 50.0\n")
                .append("Capacity: 20\n")
                .append("Description: Description for Activity 1\n");

        assertEquals(expectedItinerary.toString(), itinerary);
    }

    @Test
    public void testGetTravelPackagePassengerList_Success() {
        long travelPackageId = 1L;
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(travelPackageId);
        travelPackage.setName("Test Package");
        travelPackage.setCapacity(10);

        Passenger passenger1 = new Passenger();
        passenger1.setId(1L);
        passenger1.setName("Passenger 1");
        passenger1.setNumber(101);
        passenger1.setTravelPackage(travelPackage);

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setName("Passenger 2");
        passenger2.setNumber(102);
        passenger2.setTravelPackage(travelPackage);

        List<Passenger> passengers = Arrays.asList(passenger1, passenger2);

        when(travelPackageRepository.findById(travelPackageId)).thenReturn(Optional.of(travelPackage));
        when(passengerRepository.findByTravelPackageId(travelPackageId)).thenReturn(passengers);

        String passengerList = travelPackageService.getTravelPackagePassengerList(travelPackageId);

        StringBuilder expectedPassengerList = new StringBuilder();
        expectedPassengerList.append("Travel Package Name: Test Package\n")
                .append("Passenger Capacity: 10\n")
                .append("Number of Passengers Enrolled: 2\n")
                .append("Passenger Name: Passenger 1\n")
                .append("Passenger Number: 101\n")
                .append("Passenger Name: Passenger 2\n")
                .append("Passenger Number: 102\n");

        assertEquals(expectedPassengerList.toString(), passengerList);
    }

    @Test
    void testGetTravelPackagePassengerList_TravelPackageNotFound() {
        long travelPackageId = 1L;

        when(travelPackageRepository.findById(travelPackageId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            travelPackageService.getTravelPackagePassengerList(travelPackageId);
        });
    }

    @Test
    void testAddTravelPackageDetails_IdAlreadyExists() {
        TravelPackage existingTravelPackage = new TravelPackage();
        existingTravelPackage.setId(1L);
        existingTravelPackage.setName("Existing Package");
        existingTravelPackage.setCapacity(5);

        when(travelPackageRepository.existsById(1L)).thenReturn(true);

        assertThrows(IdAlreadyExistException.class, () -> {
            travelPackageService.addTravelPackageDetails(existingTravelPackage);
        });
    }
}
