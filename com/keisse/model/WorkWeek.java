package com.keisse.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.keisse.util.FormatUtil.PERFORMANCES_HEADER;
import static com.keisse.util.FormatUtil.SEPARATOR;

public final class WorkWeek {
    public final WorkDay[] workWeek = new WorkDay[7];


    public WorkDay[] getWorkWeek() {
        return workWeek;
    }

    public void setWorkWeek(LocalDate date) {
        LocalDate firstOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1L);
        workWeek[0] = new WorkDay(firstOfWeek);
        for (int i = 1; i < 7; i++) workWeek[i] = new WorkDay(firstOfWeek.plusDays(i));
    }

    public WorkWeek() {
        setWorkWeek(LocalDate.of(2019,12,21));
    }

    public WorkWeek(LocalDate date) {
        setWorkWeek(date);
    }

    public void reset() {
        for (WorkDay e : getWorkWeek()) e.clear();
    }

    public boolean clear(LocalDate date) {
        for (WorkDay e : getWorkWeek())
            if (e.getDate().equals(date)) {
                e.clear();
                return true;
            }
        return false;
    }

    public boolean addPerformance(LocalDate date, LocalTime start, LocalTime end) {
        for (WorkDay e : getWorkWeek())
            if (e.getDate().equals(date) && e.addPerformance(start, end)) return true;
        return false;
    }

    public StringBuilder printDay(LocalDate date) {
        //TODO try to see if filters can be used by method references too (make new boolean method which takes param e)
        StringBuilder builder = new StringBuilder(PERFORMANCES_HEADER);
        Stream.of(getWorkWeek())
                .filter(e -> e.getDate().equals(date))
                .map(WorkDay::printPerformances)
                .forEach(builder::append);
        builder.append(printWage(date));
        return builder;

    }

    public StringBuilder printAllDays() {
        StringBuilder builder = new StringBuilder(PERFORMANCES_HEADER);
        Stream.of(getWorkWeek())
                .filter(e -> !e.getPerformances().isEmpty())
                .map(WorkDay::printPerformances)
                .forEach(builder::append);
        builder.append(printTotalWage());
        return builder;
    }

    private StringBuilder printTotalWage() {
        //Function<WorkDay,Double> normalWageFunc = WorkDay::getNormalWage;

        Double normal = deduceTotalWage(WorkDay::getNormalWage);
        Double extra = deduceTotalWage(WorkDay::getExtraWage);
        Double unTaxed = deduceTotalWage(WorkDay::getUntaxedTotal);
        Double btw = deduceTotalWage(WorkDay::getBtw);

        Double sat = workWeek[5].getExtraWage();
        Double sun = workWeek[6].getExtraWage();

        return collectWageData(normal, extra, sat, sun, unTaxed, btw);
    }

    private Double deduceTotalWage(Function<WorkDay, Double> func){
        return Stream.of(getWorkWeek())
                .map(func)
                .reduce(0d, Double::sum);
    }

    public StringBuilder printWage(LocalDate date) {
        for (WorkDay e : workWeek)
            if (e.getDate().equals(date) && e.getDate().getDayOfWeek().getValue() == 6)
                return collectWageData(0d, 0d, e.getExtraWage(), 0d, e.getUntaxedTotal(), e.getBtw());

            else if (e.getDate().equals(date) && e.getDate().getDayOfWeek().getValue() == 7)
                return collectWageData(0d, 0d, 0d, e.getExtraWage(), e.getUntaxedTotal(), e.getBtw());

            else if (e.getDate().equals(date))
                return collectWageData(e.getNormalWage(), e.getExtraWage(), 0d, 0d, e.getUntaxedTotal(), e.getBtw());

        return new StringBuilder("empty?");
    }

    private StringBuilder collectWageData(Double normal, Double extra, Double sat, Double sun, Double unTaxed, Double btw) {
        return new StringBuilder(SEPARATOR)
                .append(String.format("\t\tNormaal:\t%.2f€%n", normal))
                .append(String.format("\t\tOveruren:\t%.2f€%n", extra))
                .append(String.format("\t\tZaterdag:\t%.2f€%n", sat))
                .append(String.format("\t\tZondag:\t\t%.2f€%n", sun))
                .append(SEPARATOR)
                .append(String.format("\t\tBruto:\t\t%.2f€%n", unTaxed))
                .append(String.format("\t\tExtra:\t\t%.2f€%n", btw))
                .append(SEPARATOR)
                .append(String.format("\t\ttotaal:\t\t%.2f€%n", unTaxed - btw));
    }

}
//TODO nog een paar kleine dingskes maar da's voor andere keer