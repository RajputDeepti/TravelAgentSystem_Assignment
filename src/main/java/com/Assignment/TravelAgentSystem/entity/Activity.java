package com.Assignment.TravelAgentSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Positive(message = "Cost must be positive")
    private double cost;

    @Positive(message = "Capacity must be positive")
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    @JsonIgnore
    @Valid
    private Destination destination;

    @ManyToMany(mappedBy = "activities")
    @JsonIgnore
    @Valid
    private List<Passenger> passengers = new ArrayList<>();


}
