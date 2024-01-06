package com.Assignment.TravelAgentSystem.service;

import com.Assignment.TravelAgentSystem.entity.Activity;
import com.Assignment.TravelAgentSystem.entity.Destination;
import com.Assignment.TravelAgentSystem.exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.repository.ActivityRepository;
import com.Assignment.TravelAgentSystem.repository.DestinationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ActivityServiceTest {
    @Mock
    private DestinationRepository destinationRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void testAddActivityForDestination_Successful() {
        long destinationId = 1L;

        Activity newActivity = new Activity();
        newActivity.setId(2L);
        newActivity.setName("New Activity");
        newActivity.setCapacity(10);

        Destination destination = new Destination();
        destination.setId(destinationId);

        when(destinationRepository.findById(destinationId)).thenReturn(Optional.of(destination));
        when(activityRepository.existsById(newActivity.getId())).thenReturn(false);

        assertDoesNotThrow(() -> {
            activityService.addActivityForDestination(destinationId, newActivity);
        });

        verify(destinationRepository, times(1)).findById(destinationId);
        verify(activityRepository, times(1)).existsById(newActivity.getId());
        verify(activityRepository, times(1)).save(newActivity);
    }

    @Test
    void testAddActivityForDestination_DestinationNotFound() {
        long destinationId = 1L;

        Activity newActivity = new Activity();
        newActivity.setId(2L);
        newActivity.setName("New Activity");
        newActivity.setCapacity(10);

        when(destinationRepository.findById(destinationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            activityService.addActivityForDestination(destinationId, newActivity);
        });

        verify(destinationRepository, times(1)).findById(destinationId);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void testAddActivityForDestination_ActivityAlreadyExists() {
        long destinationId = 1L;

        Activity existingActivity = new Activity();
        existingActivity.setId(2L);
        existingActivity.setName("Existing Activity");
        existingActivity.setCapacity(5);

        when(destinationRepository.findById(destinationId)).thenReturn(Optional.of(new Destination()));
        when(activityRepository.existsById(existingActivity.getId())).thenReturn(true);

        assertThrows(IdAlreadyExistException.class, () -> {
            activityService.addActivityForDestination(destinationId, existingActivity);
        });

        verify(activityRepository, times(1)).existsById(existingActivity.getId());
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void testGetAvailableActivities_Success() {
        Activity activity1 = new Activity();
        activity1.setName("Sightseeing");
        activity1.setId(1L);
        activity1.setCapacity(10);

        Destination destination1 = new Destination();
        destination1.setName("City Tour");
        destination1.setId(1L);
        activity1.setDestination(destination1);

        Activity activity2 = new Activity();
        activity2.setName("Hiking");
        activity2.setId(2L);
        activity2.setCapacity(5);

        Destination destination2 = new Destination();
        destination2.setName("Mountain Trail");
        destination2.setId(2L);
        activity2.setDestination(destination2);

        List<Activity> allActivities = new ArrayList<>();
        allActivities.add(activity1);
        allActivities.add(activity2);

        when(activityRepository.findAll()).thenReturn(allActivities);

        String availableActivitiesDetails = activityService.getAvailableActivities();

        assertTrue(availableActivitiesDetails.contains("Activities with available spaces:"));
        assertTrue(availableActivitiesDetails.contains("Activity Name: Sightseeing"));
        assertTrue(availableActivitiesDetails.contains("Destination: City Tour"));
        assertTrue(availableActivitiesDetails.contains("Spaces Available: 10"));
        assertTrue(availableActivitiesDetails.contains("Activity Name: Hiking"));
        assertTrue(availableActivitiesDetails.contains("Destination: Mountain Trail"));
        assertTrue(availableActivitiesDetails.contains("Spaces Available: 5"));
    }


    @Test
    void testGetAvailableActivities_NoActivitiesAvailable() {
        when(activityRepository.findAll()).thenReturn(Collections.emptyList());

        String result = activityService.getAvailableActivities();

        assertTrue(result.contains("No activities with available spaces"));
    }
}
