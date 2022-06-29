package com.example.taxiapp.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaxiDriverTest {

    @Test
    public void compareTo() {
        TaxiDriver driver1 = new TaxiDriver("example", 100L, "class");
        TaxiDriver driver2 = new TaxiDriver("example", 200L, "class");
        TaxiDriver driver3 = new TaxiDriver("example", 200L, "class");
        assert (driver2.compareTo(driver3) == 0);
        assert (driver3.compareTo(driver1) > 0);
        assert (driver1.compareTo(driver3) < 0);
    }
}