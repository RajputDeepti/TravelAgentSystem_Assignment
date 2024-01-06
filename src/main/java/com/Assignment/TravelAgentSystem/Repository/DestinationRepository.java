package com.Assignment.TravelAgentSystem.Repository;

import com.Assignment.TravelAgentSystem.Entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination,Long> {
    List<Destination> findByTravelPackageId(long travelPackageId);
}