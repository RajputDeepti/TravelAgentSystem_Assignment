package com.Assignment.TravelAgentSystem.repository;

import com.Assignment.TravelAgentSystem.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    List<Passenger> findByTravelPackageId(long travelPackageId);
}
