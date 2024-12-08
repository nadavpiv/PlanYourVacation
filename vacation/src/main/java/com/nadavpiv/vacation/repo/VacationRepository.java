package com.nadavpiv.vacation.repo;

import com.nadavpiv.vacation.model.Vacation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface VacationRepository extends MongoRepository<Vacation, String> {

    // Custom query method to find vacation duplicates based on city, month, number of passengers, and price.
    // The @Query annotation defines a MongoDB query that matches documents based on the parameters provided.
    @Query("{'city': ?0, 'month': ?1, 'passengers': ?2, 'price': ?3}")
    List<Vacation> findDuplicate(String city, String month, Integer passengers, Integer price);

    // Custom query method to find vacations by various criteria, including country, number of passengers, month, 
    // number of days, price range, base country, and whether it's a road trip
    // The query method name is automatically used to generate the query based on the method signature
    List<Vacation> findByCountryAndPassengersAndMonthAndNumberOfDaysBetweenAndPriceBetweenAndBaseCountryAndRoadTrip(
            String country,
            Integer passengers,
            String month,
            Integer minDays,
            Integer maxDays,
            Integer minBudget,
            Integer maxBudget,
            String baseCountry,
            String roadTrip
    );
}


