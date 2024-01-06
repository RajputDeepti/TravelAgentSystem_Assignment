package com.Assignment.TravelAgentSystem.Service;

import com.Assignment.TravelAgentSystem.Entity.Activity;
import com.Assignment.TravelAgentSystem.Entity.Destination;
import com.Assignment.TravelAgentSystem.Entity.Passenger;
import com.Assignment.TravelAgentSystem.Enums.PassengerType;
import com.Assignment.TravelAgentSystem.Exceptions.AlreadySignedUpException;
import com.Assignment.TravelAgentSystem.Exceptions.IdAlreadyExistException;
import com.Assignment.TravelAgentSystem.Exceptions.ResourceNotFoundException;
import com.Assignment.TravelAgentSystem.Repository.ActivityRepository;
import com.Assignment.TravelAgentSystem.Repository.PassengerRepository;
import com.Assignment.TravelAgentSystem.Repository.TravelPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TravelPackageRepository travelPackageRepository;
    public void addPassengerForTravelPackage(long travelPackageId, Passenger passenger) {
        if(passengerRepository.existsById(passenger.getId())){
            throw new IdAlreadyExistException("Passenger with ID "+passenger.getId()+" already exists.");
        }
        var travelPackage= travelPackageRepository.findById(travelPackageId).orElseThrow(()->new ResourceNotFoundException("Travel Package not found with this id: "+travelPackageId));
        if(travelPackage.getPassengers().size()<travelPackage.getCapacity()){
            passenger.setTravelPackage(travelPackage);
            passengerRepository.save(passenger);
        }
        else {
            throw new RuntimeException("Cannot add passenger.Travel Package capacity exceeded.");
        }
    }


    public void signUpForActivity(long passengerId, long activityId) {
        var passenger=passengerRepository.findById(passengerId).orElseThrow(()->new ResourceNotFoundException("Passenger not found with this id: "+passengerId));
        var activity=activityRepository.findById(activityId).orElseThrow(()-> new ResourceNotFoundException("Activity not found with this id: "+activityId));

        Destination destination=activity.getDestination();
        if((destination.getTravelPackage().getId())!=(passenger.getTravelPackage().getId())){
            throw new ResourceNotFoundException("Invalid activity for the current travel package.");
        }

        if (passenger.getActivities().contains(activity)) {
            throw new AlreadySignedUpException("Passenger has already signed up for this activity.");
        }

        List<Passenger> passengers=activity.getPassengers();
        if(activity.getCapacity()>passengers.size()) {
            switch (passenger.getType()) {
                case STANDARD:
                    signUpStandard(passenger, activity);
                    break;

                case GOLD:
                    signUpGold(passenger, activity);
                    break;

                case PREMIUM:
                    signUpPremium(passenger, activity);
                    break;

                default:
                    throw new ResourceNotFoundException("Unsupported passenger type");
            }
        }
        else{
                throw new ResourceNotFoundException("Cannot sign up for activity. Activity capacity exceeded.");
        }
    }

    private void signUpPremium(Passenger passenger, Activity activity) {
        passenger.getActivities().add(activity);
        passengerRepository.save(passenger);
    }

    private void signUpGold(Passenger passenger, Activity activity) {
        double cost=activity.getCost();
        double balance= passenger.getBalance();
        double discountCost= cost * 0.9;

        if(balance>=discountCost){
            passenger.setBalance(balance-discountCost);
            passenger.getActivities().add(activity);
            passengerRepository.save(passenger);
        }
        else {
            throw new ResourceNotFoundException("Insufficient balance for signing up for the activity.");
        }
    }

    private void signUpStandard(Passenger passenger, Activity activity) {
        double cost = activity.getCost();
        double balance= passenger.getBalance();

        if(balance>= cost){
            passenger.setBalance(balance-cost);
            passenger.getActivities().add(activity);
            passengerRepository.save(passenger);
        }
        else{
            throw new ResourceNotFoundException("Insufficient balance for signing up for the activity.");
        }
    }

    public String getPassengerDetails(long passengerId) {
        Passenger passenger=passengerRepository.findById(passengerId).orElseThrow(()->new ResourceNotFoundException("Passenger not found with this id: "+passengerId));

        StringBuilder passengerDetails= new StringBuilder();
        passengerDetails.append("Passenger Name: ").append(passenger.getName()).append("\n")
                .append("Passenger Number: ").append(passenger.getNumber()).append("\n");

        if(passenger.getType() == PassengerType.STANDARD || passenger.getType()==PassengerType.GOLD){
            passengerDetails.append("Balance: ").append(passenger.getBalance()).append("\n");
        }

        List<Activity> activities = passenger.getActivities();
        if(!activities.isEmpty()){
            passengerDetails.append("Activities signed up:\n");
            for(Activity activity: activities){
                passengerDetails.append("Activity Name: ").append(activity.getName()).append("\n")
                        .append("Destination: ").append(activity.getDestination().getName()).append("\n")
                        .append("Price Paid: ").append(calculatePricePaid(passenger,activity)).append("\n");
            }
        }
        else {
            passengerDetails.append("No activities signed up.\n");
        }
        return passengerDetails.toString();
    }

    private double calculatePricePaid(Passenger passenger, Activity activity) {
        double cost = activity.getCost();

        if(passenger.getType()==PassengerType.GOLD){
            cost *= 0.9;
        }
        return cost;
    }
}
