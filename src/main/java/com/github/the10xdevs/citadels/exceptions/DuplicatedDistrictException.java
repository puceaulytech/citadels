package com.github.the10xdevs.citadels.exceptions;

import com.github.the10xdevs.citadels.models.District;

/**
 * Thrown to indicate that a district already built has been built again
 */
public class DuplicatedDistrictException extends Exception {
    /**
     * Constructs a DuplicatedDistrictException with the district that caused the error
     * @param district The district that caused the error
     */
    public DuplicatedDistrictException(District district) {
        super(String.format("District '%s' is already built", district.getName()));
    }
}
