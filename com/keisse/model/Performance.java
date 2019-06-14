package com.keisse.model;

import com.keisse.util.WorkRate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.keisse.util.FormatUtil.MIDNIGHT;
import static com.keisse.util.WorkRate.*;

public final class Performance {
    private double untaxedWage;
    private long normalMinutes;
    private long extraMinutes;
    private LocalTime start;
    private LocalTime end;

    public Performance() {
    }

    public Performance(LocalTime start, LocalTime end, LocalDate date) {
        setStart(start);
        setEnd(end);

        determineWage(date);
    }


    public double getUntaxedWage() {
        return untaxedWage;
    }

    public void setUntaxedWage(double untaxedWage) {
        this.untaxedWage = untaxedWage;
    }

    public long getNormalMinutes() {
        return normalMinutes;
    }

    public void setNormalMinutes(long normalMinutes) {
        this.normalMinutes = normalMinutes;
    }

    public long getExtraMinutes() {
        return extraMinutes;
    }

    public void setExtraMinutes(long extraMinutes) {
        this.extraMinutes = extraMinutes;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public LocalTime normalHours() {
        return LocalTime.of(0, 0).plusMinutes(getNormalMinutes());
    }

    public LocalTime extraHours() {
        return LocalTime.of(0, 0).plusMinutes(getExtraMinutes());
    }

    public double normalWage() {
        return NORMAAL.calc(getNormalMinutes());
    }

    public double extraWage() {
        return OVERUREN.calc(getExtraMinutes());
    }

    public double btw() {
        return (getUntaxedWage() / 100) * 21;
    }


    private void determineWage(LocalDate date) {
        long normalMin = ChronoUnit.MINUTES.between(getStart(), getEnd());

        switch (date.getDayOfWeek().getValue()) {
            default:
                weekWage(normalMin, getEnd());
                break;

            case 6:
                weekendWage(normalMin, ZATERDAG.calc(normalMin), getEnd(), 6);
                break;

            case 7:
                weekendWage(normalMin, ZONDAG.calc(normalMin), getEnd(), 7);
                break;

        }
    }

    private void weekendWage(long minutes, double rate, LocalTime end, int dayOfWeek) {
        if (dayOfWeek == 6) {
            if (end.equals(MIDNIGHT)) {
                setExtraMinutes(minutes + 1);
                setUntaxedWage(rate+ ZATERDAG.calc(1));
            } else {
                setExtraMinutes(minutes);
                setUntaxedWage(rate);
            }
        } else if (end.equals(MIDNIGHT)) {
            setExtraMinutes(minutes + 1);
            setUntaxedWage(rate+ ZONDAG.calc(1));
        } else {
            setExtraMinutes(minutes);
            setUntaxedWage(rate);
        }
    }

    private void weekWage(long normalMin, LocalTime end) {
        long extraMin = getStart().isBefore(NORMAL_RATE_START) ? ChronoUnit.MINUTES.between(start, NORMAL_RATE_START) : 0;

        if (end.isAfter(NORMAL_RATE_END)) {
            if (end.equals(MIDNIGHT)) extraMin = ChronoUnit.MINUTES.between(NORMAL_RATE_END, getEnd()) + 1;
            else extraMin = ChronoUnit.MINUTES.between(NORMAL_RATE_END, getEnd());
        }

        normalMin -= extraMin;
        setNormalMinutes(normalMin);
        setExtraMinutes(extraMin);
        setUntaxedWage(NORMAAL.calc(normalMin));
        setUntaxedWage(OVERUREN.calc(extraMin) + getUntaxedWage());
    }

    @Override
    public String toString() {
        return String.format("%tR\t%tR\t%tR\t%tR\t\t%.2f\n", getStart(), getEnd(), normalHours(), extraHours(), getUntaxedWage());
    }
}
