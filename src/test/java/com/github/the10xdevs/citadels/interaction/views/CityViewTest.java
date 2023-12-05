package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.City;
import com.github.the10xdevs.citadels.models.District;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityViewTest {
    @SuppressWarnings("DataFlowIssue")
    @Test
    void cannotAddDistrict() {
        CityView cityView = new CityView(new City());
        assertThrows(Exception.class, () -> cityView.getDistricts().add(new District("efazef", Category.NOBLE, 9)));
    }
}