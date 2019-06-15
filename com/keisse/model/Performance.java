package com.keisse.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.keisse.util.WorkRate.*;

public final class Performance {
    private double untaxedWage;
    private long normalMinutes;
    private long extraMinutes;
    private LocalTime start;
    private LocalTime end;
    private DayOfWeek dayOfPerformance;

    public Performance() {
    }

    public Performance(LocalTime start, LocalTime end, LocalDate date) {
        setStart(start);
        setEnd(end);
        setDayOfPerformance(date.getDayOfWeek());

        determineWage();
    }

    private DayOfWeek getDayOfPerformance() {
        return dayOfPerformance;
    }

    public void setDayOfPerformance(DayOfWeek dayOfPerformance) {
        this.dayOfPerformance = dayOfPerformance;
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

    private void determineWage() {
        long workMinutes = ChronoUnit.MINUTES.between(getStart(), getEnd());

        switch (getDayOfPerformance()) {
            case SATURDAY:
                //     if (getEnd().equals(MIDNIGHT)) workMinutes+=1;
                fillWeekendWage(workMinutes, ZATERDAG.calc(workMinutes));
                break;

            case SUNDAY:
                fillWeekendWage(workMinutes, ZONDAG.calc(workMinutes));
                break;

            default:
                weekWage(workMinutes);
                break;
        }
    }

    private void fillWeekendWage(long extraMinutes, double rate) {
        extraMinutesIfMidnight();
        setExtraMinutes(extraMinutes);
        setUntaxedWage(rate);
    }

    private void weekWage(long workMinutes) {
        //not using else operator here. i don't think it impacts the code execution behaviour, since ifFalse there is no
        //execution of logic, but perhaps i should've done this in a case, how though?
        //there is however the problem of not having a "24:00" Localtime, i could further nest if statements but somehow this doesn't seem like a very good option.

        if (getStart().isBefore(NORMAL_RATE_START) && getEnd().isBefore(NORMAL_RATE_START))
            setExtraMinutes(workMinutes);

        if (getStart().isBefore(NORMAL_RATE_START) && getEnd().isAfter(NORMAL_RATE_START)) {
            setExtraMinutes(ChronoUnit.MINUTES.between(getStart(), NORMAL_RATE_START));
            setNormalMinutes(ChronoUnit.MINUTES.between(NORMAL_RATE_START, getEnd()));
        }

        if (getStart().isAfter(NORMAL_RATE_START) && (getEnd().isBefore(NORMAL_RATE_END) || getEnd().equals(NORMAL_RATE_END)))
            setNormalMinutes(workMinutes);

        if (getStart().isBefore(NORMAL_RATE_END) && getEnd().isAfter(NORMAL_RATE_END)) {
            setNormalMinutes(ChronoUnit.MINUTES.between(NORMAL_RATE_START, NORMAL_RATE_END));
            setExtraMinutes(ChronoUnit.MINUTES.between(getStart(), NORMAL_RATE_START) + ChronoUnit.MINUTES.between(NORMAL_RATE_END, getEnd()));
        }

        if (getStart().isAfter(NORMAL_RATE_END))
            setExtraMinutes(workMinutes);

            extraMinutesIfMidnight();
            setUntaxedWage(NORMAAL.calc(getNormalMinutes()) + OVERUREN.calc(getExtraMinutes()));
    }

    private void extraMinutesIfMidnight() {
        if (getEnd().equals(LocalTime.MAX)) setExtraMinutes(getExtraMinutes() + 1);
    }

    @Override
    public String toString() {
        return String.format("%tR\t%tR\t%tR\t%tR\t\t%.2f€%n", getStart(), getEnd(), normalHours(), extraHours(), getUntaxedWage());
    }
}
