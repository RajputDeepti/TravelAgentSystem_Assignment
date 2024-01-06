package com.Assignment.TravelAgentSystem.repository;

import com.Assignment.TravelAgentSystem.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination,Long> {
    List<Destination> findByTravelPackageId(long travelPackageId);
}
