package com.Assignment.TravelAgentSystem.Repository;

import com.Assignment.TravelAgentSystem.Entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    List<Passenger> findByTravelPackageId(long travelPackageId);
}
