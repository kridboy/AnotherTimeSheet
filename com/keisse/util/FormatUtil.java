package com.keisse.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * The type Format util.
 */
public abstract class FormatUtil {
    /**
     * The constant PERFORMANCES_HEADER returns a header string for printing prestations.
     */
    public static final StringBuilder PERFORMANCES_HEADER =
            new StringBuilder("\nDatum\t\tBegin\tEinde\tNormaal\tOveruren\tWage\n");

    /**
     * The constant SEPARATOR returns a header string for printing prestations.
     */
    public static final StringBuilder SEPARATOR = new StringBuilder("\t\t\t\t=====\n");

    /**
     * The constant DATE_FORMATTER formats a date to follow the given pattern.
     */
    public static final DateTimeFormatter DATE_FORMATTER = ofPattern("dd/MM/yyyy");
    /**
     * The constant MONTH_FORMATTER formats a date to follow the given pattern.
     */
    public static final DateTimeFormatter MONTH_FORMATTER = ofPattern("MMMM");
    /**
     * The constant TIME_FORMATTER formats a time to follow the given pattern.
     */
    public static final DateTimeFormatter TIME_FORMATTER = ofPattern("HH:mm");
    /**
     * The constant TRY_AGAIN returns an error message.
     */
    public static final String TRY_AGAIN = "\nIncorrect input, please try again.\n";
    /**
     * The constant NEW_DAY returns a string which asks for user input.
     */
    public static final String NEW_DAY = "Geef de gewenste dag in: ";
    /**
     * The constant DAY_NOT_IN_WEEK returns a string that tells if the day was not within a workweek.
     */
    public static final String DAY_NOT_IN_WEEK = "\nDeze dag bevind zich niet in de werkweek.\n";

    private FormatUtil() {
        throw new IllegalStateException("Utility class");
    }
}

