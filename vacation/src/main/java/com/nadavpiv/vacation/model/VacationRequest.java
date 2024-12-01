package com.nadavpiv.vacation.model;


public class VacationRequest {
    private String country; // The destination country of the vacation
    private String city; // The destination city of the vacation
    private int passengers; // The number of passengers of the vacation
    private int minDays; // The minimum days for the vacation request
    private int maxDays; // The maximum days for the vacation request
    private String month; // The month of the vacation
    private int minBudget; // The minimum budget for the vacation request
    private int maxBudget; // The maximum budget for the vacation request
    private String baseCountry; // The base country of the user that request the vacation
    private String roadTrip; // road trip or city trip

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public int getMinDays() {return minDays;}

    public void setMinDays(int minDays) {
        if (minDays <= 0) {
            throw new IllegalArgumentException("Min days must be greater than 0");
        }
        this.minDays = minDays;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(int maxDays) {
        if (maxDays <= 0) {
            throw new IllegalArgumentException("Max days must be greater than 0");
        }
        this.maxDays = maxDays;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        if (passengers <= 0) {
            throw new IllegalArgumentException("Passengers must be greater than 0");
        }
        this.passengers = passengers;

    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getMinBudget() {
        return minBudget;
    }

    public void setMinBudget(int minBudget) {
        if (minBudget < 0) {
            throw new IllegalArgumentException("Min budget must be greater or equal than 0");
        }
        this.minBudget = minBudget;
    }

    public int getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(int maxBudget) {
        if (maxBudget < 0) {
            throw new IllegalArgumentException("Max budget must be greater or equal than 0");
        }
        this.maxBudget = maxBudget;
    }

    public String getBaseCountry() {
        return baseCountry;
    }

    public void setBaseCountry(String baseCountry) {
        this.baseCountry = baseCountry;
    }

    public String getRoadTrip() {
        return roadTrip;
    }

    public void setRoadTrip(String roadTrip) {
        this.roadTrip = roadTrip;
    }

    @Override
    public String toString() {
        return "VacationRequest{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", passengers=" + passengers +
                ", minDays=" + minDays +
                ", maxDays=" + maxDays +
                ", month='" + month + '\'' +
                ", minBudget=" + minBudget +
                ", maxBudget=" + maxBudget +
                ", baseCountry='" + baseCountry + '\'' +
                ", roadTrip='" + roadTrip + '\'' +
                '}';
    }
}




