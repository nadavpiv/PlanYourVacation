package com.nadavpiv.vacation.service;

import com.nadavpiv.vacation.model.Vacation;
import com.nadavpiv.vacation.repo.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VacationService {

    @Autowired
    VacationRepository vacationRepository; // Injects the repository for accessing vacation data.

    // Method to get all vacations from the database.
    public Iterable<Vacation> getAllVacations() {
        return vacationRepository.findAll(); // Returns all vacation records.
    }

    // Method to get a vacation by its ID.
    public Vacation getVacationById(String vacationId) {
        // Find vacation by ID, throws an exception if not found.
        return vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("Vacation not found"));
    }

    // Method to find vacations by various filters such as country, number of passengers, month, etc.
    public List<Vacation> findVacationsByFilters(
            String country,
            Integer passengers,
            String month,
            Integer minDays,
            Integer maxDays,
            Integer minBudget,
            Integer maxBudget,
            String baseCountry,
            String roadTrip
    ) {
        // Queries the repository using multiple parameters to filter vacation records.
        return vacationRepository.findByCountryAndPassengersAndMonthAndNumberOfDaysBetweenAndPriceBetweenAndBaseCountryAndRoadTrip(
                country,
                passengers,
                month,
                minDays,
                maxDays,
                minBudget,
                maxBudget,
                baseCountry,
                roadTrip
        );
    }

    // Method to check if the given vacation is a duplicate (based on city, month, passengers, and price).
    public boolean isDuplicateVacation(Vacation vacation) {
        // Calls repository to find vacations that match the specified criteria.
        List<Vacation> duplicates = vacationRepository.findDuplicate(
                vacation.getCity(),
                vacation.getMonth(),
                vacation.getPassengers(),
                vacation.getPrice()
        );
        // If duplicates are found, return true (indicating it's a duplicate).
        return !duplicates.isEmpty(); // If the list is not empty, it's a duplicate
    }

    // Method to save a vacation if it's not a duplicate.
    public Vacation saveVacation(Vacation vacation) {
        // Checks if the vacation is a duplicate before saving it.
        if (isDuplicateVacation(vacation)) {
            throw new IllegalArgumentException("Duplicate vacation detected"); // Throws exception if it's a duplicate.
        }
        return vacationRepository.save(vacation); // Saves the vacation to the database.
    }

    // Method to delete a vacation from the database.
    public void delete(Vacation vacation) {
        vacationRepository.delete(vacation); // Deletes the specified vacation record.
    }
}


