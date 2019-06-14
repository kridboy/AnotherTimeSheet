package com.keisse.util;

import java.time.LocalTime;

/**
 * The enum Work rate.
 */
public enum WorkRate {
    /**
     * Normaal work rate.
     */
    NORMAAL(15),
    /**
     * Overuren work rate.
     */
    OVERUREN(20),
    /**
     * Zaterdag work rate.
     */
    ZATERDAG(25),
    /**
     * Zondag work rate.
     */
    ZONDAG(35);

    /**
     * The constant NORMAL_RATE_START which returns the start of normal working hours.
     */
    public static final LocalTime NORMAL_RATE_START = LocalTime.of(6, 0);
    /**
     * The constant NORMAL_RATE_END which returns the end of normal working hours.
     */
    public static final LocalTime NORMAL_RATE_END = LocalTime.of(19, 0);

    private final double workingRate;

    WorkRate(double rate) {
        workingRate = rate;
    }

    private double getWorkingRate() {
        return workingRate;
    }

    /**
     * Calc double.
     *
     * @param min Minutes worked
     * @return The untaxed wage for a Workday
     */
    public double calc(long min) {
        return (getWorkingRate() / 60) * min;
    }


}