package com.insha.World.Atlas.service;

import com.insha.World.Atlas.entity.Country;
import com.insha.World.Atlas.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getAllCountries() {
        // This method should return all countries from the database.
        // return countryRepository.findAll();

        return countryRepository.findAll();
    }


    public void addAll(List<Country> countries) {
        // This method should save the list of countries to the database.
        // Implementation will depend on the repository or data access layer used.
        // For example, if using a JPA repository, you might call:
        // countryRepository.saveAll(countries);

        countryRepository.saveAll(countries);
    }

    public Optional<Country> getCountryByCode(String code) {
        // This method should return a country by its code.
        // return countryRepository.findById(code);

        return countryRepository.findById(code);
    }

    public List<Country> searchCountries(String name) {
        // This method should search for countries by name.
        // The implementation will depend on the repository or data access layer used.
        // For example, if using a JPA repository, you might have a method like:
        // return countryRepository.findByNameContainingIgnoreCase(name);

        return countryRepository.findByNameContainingIgnoreCase(name);
    }

    public Country addCountry(Country country) {
        // This method should add a new country to the database.
        // return countryRepository.save(country);

        return countryRepository.save(country);
    }
}
