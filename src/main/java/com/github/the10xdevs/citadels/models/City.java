package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A city of districts
 *
 * @see District
 */
public class City implements Iterable<District> {
    private final Set<District> districts = new HashSet<>();

    /**
     * Add a new district to the city
     *
     * @param district The new district
     * @throws DuplicatedDistrictException If the district is already in the city
     */
    public void addDistrict(District district) throws DuplicatedDistrictException {
        if (!this.districts.add(district))
            throw new DuplicatedDistrictException(district);
    }

    /**
     * Returns an immutable collection of all the districts in this City
     *
     * @return An immutable collection of all the districts in this City
     */
    public Set<District> getDistricts() {
        return Collections.unmodifiableSet(this.districts);
    }

    /**
     * Returns how many districts have been built in this city
     *
     * @return How many districts have been built in this city
     */
    public int getSize() {
        return this.districts.size();
    }

    @Override
    public Iterator<District> iterator() {
        return this.districts.iterator();
    }
}