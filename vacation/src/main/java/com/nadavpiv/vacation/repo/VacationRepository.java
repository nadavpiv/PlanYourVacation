package com.nadavpiv.vacation.repo;

import com.nadavpiv.vacation.model.Vacation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface VacationRepository extends MongoRepository<Vacation, String> {
    @Query("{'city': ?0, 'month': ?1, 'passengers': ?2, 'price': ?3}")
    List<Vacation> findDuplicate(String city, String month, Integer passengers, Integer price);

    // Custom query method to find vacations by the given criteria including price range
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


