package com.nadavpiv.vacation.model;

public class Restaurant {
    private String restaurantName;
    private String restaurantAddress;
    private Integer restaurantPricePerPerson;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Integer getRestaurantPricePerPerson() {
        return restaurantPricePerPerson;
    }

    public void setRestaurantPricePerPerson(Integer restaurantPricePerPerson) {
        if (restaurantPricePerPerson < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.restaurantPricePerPerson = restaurantPricePerPerson;
    }

    @Override
    public String toString() {
        return
                "Restaurant Name: " + restaurantName + '\n' +
                        "Restaurant Address: " + restaurantAddress + '\n' +
                        "Restaurant Price Per Person: " + restaurantPricePerPerson + '$' + '\n';
    }
}

