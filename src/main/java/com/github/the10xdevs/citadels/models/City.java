package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;

import java.util.*;

public class City {
    private final Set<District> districts = new HashSet<>();

    /**
     * Add a new district to the city
     * @param district The new district
     * @throws DuplicatedDistrictException If the district is already in the city
     */
    public void addDistrict(District district) throws DuplicatedDistrictException {
        if (!this.districts.add(district))
            throw new DuplicatedDistrictException(district);
    }

    public Set<District> getDistricts() {
        return Collections.unmodifiableSet(this.districts);
    }

    public int getSize() {
        return this.districts.size();
    }
}
