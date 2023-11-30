package com.github.the10xdevs.citadels.exceptions;

import com.github.the10xdevs.citadels.models.District;

public class DuplicatedDistrictException extends Exception {
    public DuplicatedDistrictException(District district) {
        super(String.format("District '%s' is already built", district.getName()));
    }
}
