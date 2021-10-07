package br.com.safecar;

import java.time.Duration;
import java.time.LocalTime;

public class OccupationRegistry {
    private Vehicle vehicle;
    private LocalTime arrival;
    private LocalTime departure;
    private Integer vacancy;

    public OccupationRegistry(Vehicle vehicle, Integer vacancy) {
        this.vehicle = vehicle;
        this.vacancy = vacancy;
        this.arrival = LocalTime.now();

    }
    public Duration getOccupationTime(){
         return Duration.between(arrival, LocalTime.now());
    }

    public void leave() {
        this.departure = LocalTime.now();

    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalTime arrival) {
        this.arrival = arrival;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    public Integer getVacancy() {
        return vacancy;
    }

    public void setVacancy(Integer vacancy) {
        this.vacancy = vacancy;
    }
}



