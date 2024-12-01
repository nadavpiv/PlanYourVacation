package com.nadavpiv.vacation.repo;

import com.nadavpiv.vacation.model.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VacationService {

    @Autowired
    VacationRepository vacationRepository;
    public Iterable<Vacation> getAllVacations() {
        return vacationRepository.findAll();
    }
    public Vacation getVacationById(String vacationId) {
        return vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("Vacation not found"));
    }
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

    // Check if the vacation is a duplicate
    public boolean isDuplicateVacation(Vacation vacation) {
        List<Vacation> duplicates = vacationRepository.findDuplicate(
                vacation.getCity(),
                vacation.getMonth(),
                vacation.getPassengers(),
                vacation.getPrice()
        );
        return !duplicates.isEmpty(); // If the list is not empty, it's a duplicate
    }

    // Save a vacation, if it's not a duplicate
    public Vacation saveVacation(Vacation vacation) {
        if (isDuplicateVacation(vacation)) {
            throw new IllegalArgumentException("Duplicate vacation detected");
        }
        return vacationRepository.save(vacation);
    }
    public void delete(Vacation vacation) {
        vacationRepository.delete(vacation);
    }
}



