package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.models.City;
import com.github.the10xdevs.citadels.models.District;

import java.util.Collections;
import java.util.Set;

/**
 * An immutable view of a City
 * @see City
 */
public final class CityView {
    private final City city;

    public CityView(City city) {
        this.city = city;
    }

    public Set<District> getDistricts() {
        return Collections.unmodifiableSet(this.city.getDistricts());
    }
}
