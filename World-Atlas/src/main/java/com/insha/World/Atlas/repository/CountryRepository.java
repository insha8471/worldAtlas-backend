package com.insha.World.Atlas.repository;

import com.insha.World.Atlas.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, String> {

    List<Country> findByNameContainingIgnoreCase(String name);
}
