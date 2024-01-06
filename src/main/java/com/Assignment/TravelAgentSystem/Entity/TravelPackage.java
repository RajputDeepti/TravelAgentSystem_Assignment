package com.Assignment.TravelAgentSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Data
@Table(name = "TravelPackage")
@AllArgsConstructor
@NoArgsConstructor
@Immutable
public class TravelPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(value = 1, message = "Capacity must be greater than 0")
    private int capacity;

    @OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL)
    @JsonIgnore
    @Valid
    private List<Destination> itinerary;

    @OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL)
    @JsonIgnore
    @Valid
    private List<Passenger> passengers;
}