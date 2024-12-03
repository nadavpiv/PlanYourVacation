package com.nadavpiv.vacation.prompts;

import com.nadavpiv.vacation.model.VacationRequest;

public class ChatGPTPromptBuilder {
    public static String buildVacationPrompt(VacationRequest request) {
        return String.format(
                "Provide vacation option for the following criteria: " +
                        "Country: %s " +
                        getCityPrompt(request) +
                        "Passengers: %d " +
                        "Min Days: %d " +
                        "Max Days: %d " +
                        "Min Budget: %d " +
                        "Max Budget: %d " +
                        "Month: %s " +
                        getRoadTripPrompt(request) +
                        "Base Country: %s " +
                        "Each vacation option should include: (All amounts will be calculated in USD) " +
                        "- Name " +
                        "- Country " +
                        "- City " +
                        "- Month " +
                        "- Passengers " +
                        "- Number of Days " +
                        "- Price (Total vacation price for all the passengers) " +
                        "- Base Country " +
                        "- Road Trip " +
                        "- Flight Details: The flight must be a future flight (no past flights allowed, we are in November 2024 right now!) " +
                        "- Company Name " +
                        "- Inbound flight : departure city year, month, hour, arrival city year, month, hour " +
                        "- Outbound flight : departure city year, month, hour, arrival city year, month, hour " +
                        "- Flight price (price per person) " +
                        "- Hotel Details " +
                        "- Hotel name " +
                        "- Hotel address " +
                        "- Hotel price (per person per night) " +
                        "- 5 recommended restaurants (not the most expensive ones) " +
                        "- Restaurant name " +
                        "- Restaurant address " +
                        "- Restaurant price per person " +
                        "- List of itinerary days: Generate a list of itinerary days where the number of days exactly equals the number of days of the vacation. For instance, if the vacation number of days is 4 days, the itinerary must include 4 distinct days with planned activities for each day " +
                        "- Day Number " +
                        "- list of attractions (minimum 3 attractions per day) " +
                        "- Attraction name " +
                        "- Attraction details " +
                        "- Attraction Price " +
                        "- Attraction latitude " +
                        "- Attraction longitude " +
                        "- Total food price per person (calculated for 3 meals per day for the entire vacation) " +
                        "- Total attractions price per person (include prices of all attractions) " +
                        getCarTransportationPrice(request) +
                        " Please follow this JSON structure:" +
                        "{" +
                        "vacationOptions: [" +
                        "{" +
                        "travelName: Holiday in Paris," +
                        "country: France," +
                        "city: Paris," +
                        "month: December," +
                        "Passengers: 2," +
                        "numberOfDays: 5," +
                        "price: 1200" +
                        "baseCountry: USA," +
                        "roadTrip: Yes, " +
                        "flight: {" +
                        "companyName: EL Al," +
                        "inboundFlight: String," +
                        "outboundFlight: String," +
                        "flightPrice: int" +
                        "}," +
                        "hotel: {" +
                        "hotelName: String," +
                        "hotelAddress: String," +
                        "hotelPrice: int" +
                        "}," +
                        "restaurants:  [" +
                        "{" +
                        "restaurantName: String," +
                        "restaurantAddress : String," +
                        "restaurantPricePerPerson : int" +
                        "}," +
                        "{" +
                        "restaurantName: String," +
                        "restaurantAddress : String," +
                        "restaurantPricePerPerson : int" +
                        "}" +
                        "]," +
                        "foodPrice: 150," +
                        "attractionsPrice: 150," +
                        "carTransportationPrice : 250," +
                        "days: [" +
                        "{" +
                        "dayNumber: 1," +
                        "attractions: [" +
                        "{" +
                        "attractionName: Eiffel Tower, " +
                        "attractionDetails: Iconic Parisian landmark with panoramic views," +
                        "attractionPrice: 50," +
                        "latitude : 35.213210," +
                        "longitude: 31.778498" +
                        "}," +
                        "{" +
                        "attractionName: Louvre Museum," +
                        "attractionDetails: World's largest art museum, home to the Mona Lisa," +
                        "attractionPrice: 30," +
                        "latitude: 35.216410," +
                        "longitude: 31.772498" +
                        "}" +
                        "]" +
                        "}," +
                        "{" +
                        "dayNumber: 2," +
                        "attractions: [" +
                        "{" +
                        "attractionName: Notre-Dame Cathedral," +
                        "attractionDetails: Gothic cathedral with stunning architecture," +
                        "attractionPrice: 25," +
                        "latitude: 55.213250," +
                        "longitude: 31.778498" +
                        "}" +
                        "]" +
                        "}" +
                        "]" +
                        "}" +
                        "]" +
                        "}",
                request.getCountry(),
                request.getCity(),
                request.getPassengers(),
                request.getMinDays(),
                request.getMaxDays(),
                request.getMinBudget(),
                request.getMaxBudget(),
                request.getMonth(),
                request.getRoadTrip(),
                request.getBaseCountry()
        );
    }

    public static String getCityPrompt(VacationRequest request){
        if(!request.getCity().equals("Surprise me") && request.getRoadTrip().equals("No"))
            return "City: %s" + " (the entire vacation should take place in this city. No travel to other cities is required) ";
        else if (!request.getCity().equals("Surprise me") && request.getRoadTrip().equals("Yes"))
            return "City: %s" + " (this is the starting city. The road trip must include travel to multiple cities starting from here. Driving between cities should be limited to a maximum of 5 hours per day) ";
        else if (request.getCity().equals("Surprise me") && request.getRoadTrip().equals("No"))
            return "City: %s" + " (choose a single city randomly for the vacation, with all activities and accommodations located in this city) ";
        else if (request.getCity().equals("Surprise me") && request.getRoadTrip().equals("Yes"))
            // City surprise and road trip yes
            return "City: %s" + " cities within the same country or region, with driving limited to a maximum of 5 hours per day) ";
        else
            return "%s";
    }

    public static String getRoadTripPrompt(VacationRequest request){
        if(!request.getCity().equals("Surprise me") && request.getRoadTrip().equals("No"))
            return "Road Trip: %s" + " (all activities and accommodations should be in the specified city) ";
        else if (!request.getCity().equals("Surprise me") && request.getRoadTrip().equals("Yes"))
            return "Road Trip: %s" + " (the vacation must include stops in other cities. Staying in the starting city for the entire duration is not allowed) ";
        else if (request.getCity().equals("Surprise me") && request.getRoadTrip().equals("No"))
            return "Road Trip: %s" + " (the vacation must be confined to a single city). ";
        else if (request.getCity().equals("Surprise me") && request.getRoadTrip().equals("Yes"))
            // City surprise and road trip yes
            return "Road Trip: %s" + " (ensure the vacation includes travel to multiple cities. Staying in one city is not allowed) ";
        else
            return "%s";
    }
    public static String getCarTransportationPrice(VacationRequest request){
        if(request.getRoadTrip().equals("No"))
            return "Please calculate the total public transportation price for this vacation, including the cost of taxis, buses, and trains. Make sure to account for the number of passengers and the duration of the trip. ";
        else if (request.getRoadTrip().equals("Yes"))
            return "Please calculate the total car rental price for this vacation, including daily rental cost, fuel, and parking.";
        else
            return "";
    }
}

