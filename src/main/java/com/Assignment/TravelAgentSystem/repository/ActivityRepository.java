package com.Assignment.TravelAgentSystem.repository;

import com.Assignment.TravelAgentSystem.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByDestinationId(long id);
}
