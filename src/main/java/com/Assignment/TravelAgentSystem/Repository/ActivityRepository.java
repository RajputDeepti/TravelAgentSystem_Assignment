package com.Assignment.TravelAgentSystem.Repository;

import com.Assignment.TravelAgentSystem.Entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByDestinationId(long id);
}
