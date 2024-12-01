package com.nadavpiv.vacation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "vacation_options")
public class Vacation {

    @Id
    private String id; // MongoDB will auto-generate this ID

    private String travelName;
    private String country;
    private String city;
    private String month;
    private Integer passengers;
    private Integer numberOfDays;
    private Integer price;
    private String baseCountry;
    private String roadTrip;
    private Integer foodPrice;
    private Integer attractionsPrice;
    private Integer carTransportationPrice;
    private Flight flight;
    private Hotel hotel;
    private List<Restaurant> restaurants;
    private List<DayOption> days;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTravelName() {
        return travelName;
    }

    public void setTravelName(String travelName) {
        this.travelName = travelName;
    }

    public Integer getPassengers() {
        return passengers;
    }

    public void setPassengers(Integer passengers) {
        if (passengers <= 0) {
            throw new IllegalArgumentException("Passengers must be greater than 0");
        }
        this.passengers = passengers;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("Number of days must be greater than 0");
        }
        this.numberOfDays = numberOfDays;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be greater or equal to 0");
        }
        this.price = price;
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

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Integer getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Integer foodPrice) {
        this.foodPrice = foodPrice;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public Integer getAttractionsPrice() {
        return attractionsPrice;
    }

    public void setAttractionsPrice(Integer attractionsPrice) {
        this.attractionsPrice = attractionsPrice;
    }

    public Integer getCarTransportationPrice() {
        return carTransportationPrice;
    }

    public void setCarTransportationPrice(Integer carTransportationPrice) {
        this.carTransportationPrice = carTransportationPrice;
    }

    public List<DayOption> getDays() {
        return days;
    }

    public void setDays(List<DayOption> days) {
        this.days = days;
    }


    public static class DayOption {
        private String dayNumber;
        private List<Attraction> attractions;

        public String getDayNumber() {
            return dayNumber;
        }

        public void setDayNumber(String dayNumber) {
            this.dayNumber = dayNumber;
        }

        public List<Attraction> getAttractions() {
            return attractions;
        }

        public void setAttractions(List<Attraction> attractions) {
            this.attractions = attractions;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Day Number: ").append(dayNumber).append('\n')
                    .append("Attractions: \n");

            if (attractions != null && !attractions.isEmpty()) {
                for (Attraction attraction : attractions) {
                    sb.append(attraction).append("\n");
                }
            }
            return sb.toString();
        }
    }

    public static class Attraction {
        private String attractionName;
        private String attractionDetails;
        private Integer attractionPrice;
        private double latitude;
        private double longitude;
        private List<String> photos;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getAttractionName() {
            return attractionName;
        }

        public void setAttractionName(String attractionName) {
            this.attractionName = attractionName;
        }

        public String getAttractionDetails() {
            return attractionDetails;
        }

        public void setAttractionDetails(String attractionDetails) {
            this.attractionDetails = attractionDetails;
        }

        public Integer getAttractionPrice() {
            return attractionPrice;
        }

        public void setAttractionPrice(Integer attractionPrice) {
            if (attractionPrice < 0) {
                throw new IllegalArgumentException("Attraction price must be greater or equal than 0");
            }
            this.attractionPrice = attractionPrice;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Attraction Name: ").append(attractionName).append('\n')
                    .append("Attraction Details: ").append(attractionDetails).append('\n')
                    .append("Attraction Price: ").append(attractionPrice).append('$').append('\n');

            if (photos != null && !photos.isEmpty()) {
                sb.append("Photos: \n");
                for (String photo : photos) {
                    sb.append(photo).append("\n");
                }
            }

            return sb.toString();
        }
    }

    public void updateVacation(Vacation vacation){
        // Update the vacation details
        vacation.setTravelName(getTravelName());
        vacation.setCountry(getCountry());
        vacation.setMonth(getMonth());
        vacation.setPassengers(getPassengers());
        vacation.setNumberOfDays(getNumberOfDays());
        vacation.setPrice(getPrice());
        vacation.setBaseCountry(getBaseCountry());
        vacation.setFoodPrice(getFoodPrice());
        vacation.setAttractionsPrice(getAttractionsPrice());
        vacation.setCarTransportationPrice(getCarTransportationPrice());
        vacation.setDays(getDays());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vacation Details:\n")
                .append("Travel Name: ").append(travelName).append('\n')
                .append("Country: ").append(country).append('\n')
                .append("City: ").append(city).append('\n')
                .append("Month: ").append(month).append('\n')
                .append("Passengers: ").append(passengers).append('\n')
                .append("Number of Days: ").append(numberOfDays).append('\n')
                .append("Price: ").append(price).append('$').append('\n')
                .append("Base Country: ").append(baseCountry).append('\n')
                .append("Road Trip: ").append(roadTrip).append('\n')
                .append("Food Price: ").append(foodPrice).append('$').append('\n')
                .append("Attractions Price: ").append(attractionsPrice).append('$').append('\n')
                .append("Car and Transportation Price: ").append(carTransportationPrice).append('$').append('\n')
                .append("Flight: \n").append(flight).append('\n')
                .append("Hotel: \n").append(hotel).append('\n');

        if (restaurants != null && !restaurants.isEmpty()) {
            sb.append("Restaurants: \n");
            for (Restaurant restaurant : restaurants) {
                sb.append(restaurant).append('\n');
            }
        }

        if (days != null && !days.isEmpty()) {
            sb.append("Days:\n");
            for (DayOption day : days) {
                sb.append(day).append("\n");
            }
        }

        return sb.toString();
    }
}



