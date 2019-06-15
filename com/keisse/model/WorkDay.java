package com.keisse.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.keisse.util.FormatUtil.DATE_FORMATTER;

public final class WorkDay {
    private double extraWage;
    private double normalWage;
    private double btw;
    private double untaxedTotal;
    private LocalDate date;
    private final List<Performance> performances = new ArrayList<>();

    public WorkDay() {
    }

    public WorkDay(LocalDate date) {
        setDate(date);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public double getExtraWage() {
        return extraWage;
    }

    public void setExtraWage(double extraWage) {
        this.extraWage = extraWage;
    }

    public double getNormalWage() {
        return normalWage;
    }

    public void setNormalWage(double normalWage) {
        this.normalWage = normalWage;
    }

    public double getBtw() {
        return btw;
    }

    public void setBtw(double btw) {
        this.btw = btw;
    }

    public double getUntaxedTotal() {
        return untaxedTotal;
    }

    public void setUntaxedTotal(double untaxedTotal) {
        this.untaxedTotal = untaxedTotal;
    }

    public boolean addPerformance(LocalTime start, LocalTime end) {
        if (start.isBefore(end) && notBetweenPerformance(start, end)) {
            Performance performance = new Performance(start, end, getDate());
            performances.add(performance);

            setUntaxedTotal(performance.getUntaxedWage() + getUntaxedTotal());
            setBtw(performance.btw() + getBtw());
            setNormalWage(performance.normalWage() + getNormalWage());
            setExtraWage(performance.extraWage() + getExtraWage());
            return true;
        }
        return false;
    }

    public boolean notBetweenPerformance(LocalTime start, LocalTime end) {
        if (!getPerformances().isEmpty())
            for (Performance e : getPerformances()) {
                if (end.isBefore(e.getStart()) || start.isAfter(e.getEnd()))
                    return true;
                else
                    return false;
            }
            return true;

    }

    public void clear() {
        performances.clear();
    }

    public StringBuilder printPerformances() {
        StringBuilder builder = new StringBuilder();
        getPerformances().stream()
                .map(e -> getDate().format(DATE_FORMATTER) + "\t" + e.toString())
                .forEach(builder::append);
        return builder;
    }
}
