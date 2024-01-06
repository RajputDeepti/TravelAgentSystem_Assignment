package com.Assignment.TravelAgentSystem.entity;

import com.Assignment.TravelAgentSystem.enums.PassengerType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Passenger")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(value = 1, message = "Number must be greater than 0")
    private int number;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Passenger type cannot be null")
    private PassengerType type;

    @Positive(message = "Balance must be positive")
    private double balance;

    @ManyToMany
    @JoinTable(name = "passenger_activity",
            joinColumns = @JoinColumn(name = "passenger_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    @JsonIgnore
    @Valid
    private List<Activity> activities;

    @ManyToOne
    @JoinColumn(name = "travel_package_id")
    @JsonIgnore
    @Valid
    private TravelPackage travelPackage;
}
