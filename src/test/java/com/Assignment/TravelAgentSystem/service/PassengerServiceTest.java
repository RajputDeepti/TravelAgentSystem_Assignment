package com.Assignment.TravelAgentSystem.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.Assignment.TravelAgentSystem.entity.Activity;
import com.Assignment.TravelAgentSystem.entity.Destination;
import com.Assignment.TravelAgentSystem.entity.Passenger;
import com.Assignment.TravelAgentSystem.entity.TravelPackage;
import com.Assignment.TravelAgentSystem.enums.PassengerType;
import com.Assignment.TravelAgentSystem.exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.repository.ActivityRepository;
import com.Assignment.TravelAgentSystem.repository.PassengerRepository;
import com.Assignment.TravelAgentSystem.repository.TravelPackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PassengerServiceTest {
    @InjectMocks
    private PassengerService passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private TravelPackageRepository travelPackageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPassengerForTravelPackage_Success() {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(1L);
        travelPackage.setCapacity(2);

        Passenger newPassenger = new Passenger();
        newPassenger.setId(2L);
        newPassenger.setNumber(200);
        newPassenger.setName("Passenger 1");
        newPassenger.setType(PassengerType.STANDARD);

        when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
        when(passengerRepository.existsById(2L)).thenReturn(false);

        passengerService.addPassengerForTravelPackage(1L, newPassenger);

        verify(passengerRepository, times(1)).save(newPassenger);
    }

    @Test
    void testAddPassengerForTravelPackage_IdAlreadyExists() {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(1L);
        travelPackage.setCapacity(2);

        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(2L);
        existingPassenger.setNumber(200);
        existingPassenger.setName("Passenger 1");
        existingPassenger.setType(PassengerType.STANDARD);

        when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
        when(passengerRepository.existsById(2L)).thenReturn(true);

        assertThrows(IdAlreadyExistException.class, () ->
                passengerService.addPassengerForTravelPackage(1L, existingPassenger));
    }

    @Test
    void testAddPassengerForTravelPackage_TravelPackageFull() {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setId(1L);
        travelPackage.setCapacity(0);

        Passenger newPassenger = new Passenger();
        newPassenger.setId(2L);
        newPassenger.setNumber(200);
        newPassenger.setName("Passenger 1");
        newPassenger.setType(PassengerType.STANDARD);

        when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
        when(passengerRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                passengerService.addPassengerForTravelPackage(1L, newPassenger));
    }


    @Test
    void testGetPassengerDetails_Success() {
        long passengerId = 1L;
        Passenger passenger = new Passenger();
        passenger.setId(passengerId);
        passenger.setName("Passenger 1");
        passenger.setNumber(101);
        passenger.setType(PassengerType.STANDARD);
        passenger.setBalance(100.0);

        Activity activity = new Activity();
        activity.setName("Sightseeing");
        activity.setId(1L);

        Destination destination = new Destination();
        destination.setName("City Tour");
        destination.setId(1L);

        activity.setDestination(destination);

        List<Activity> activities = new ArrayList<>();
        activities.add(activity);

        passenger.setActivities(activities);

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        String passengerDetails = passengerService.getPassengerDetails(passengerId);

        assertTrue(passengerDetails.contains("Passenger Name: Passenger 1"));
        assertTrue(passengerDetails.contains("Passenger Number: 101"));
        assertTrue(passengerDetails.contains("Balance: 100.0"));
        assertTrue(passengerDetails.contains("Activities signed up:"));
        assertTrue(passengerDetails.contains("Activity Name: Sightseeing"));
        assertTrue(passengerDetails.contains("Destination: City Tour"));
    }


    @Test
    void testGetPassengerDetails_PassengerNotFound() {
        when(passengerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            passengerService.getPassengerDetails(2L);
        }, "Expected ResourceNotFoundException for a non-existent passenger");

        verify(passengerRepository).findById(2L);
    }
}
