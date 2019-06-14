package com.keisse.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

import static com.keisse.util.FormatUtil.PERFORMANCES_HEADER;

public class WorkWeek {
    WorkDay[] workWeek = new WorkDay[7];

    public void setWorkWeek(LocalDate date) {
        LocalDate firstOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1L);
        workWeek[0] = new WorkDay(firstOfWeek);
        for (int i = 1; i < 7; i++) workWeek[i] = new WorkDay(firstOfWeek.plusDays(i));
    }

    public WorkWeek() {
    }

    public WorkWeek(LocalDate date) {
        setWorkWeek(date);
    }

    public WorkDay[] getWorkWeek() {
        return workWeek;
    }

    public void reset() {
        for (WorkDay e : getWorkWeek()) e.clear();
    }

    public boolean clear(LocalDate date) {
        for (WorkDay e : getWorkWeek()) if (e.getDate().equals(date)) return true;
        return false;
    }

    public boolean addPerformance(LocalDate date, LocalTime start, LocalTime end) {
        for (WorkDay e : getWorkWeek()) if (e.getDate().equals(date) && e.addPerformance(start, end)) return true;
        return false;
    }

    public StringBuilder printDay(LocalDate date) {
        StringBuilder builder = PERFORMANCES_HEADER;
        Stream.of(getWorkWeek())
                .filter(e -> e.getDate().equals(date))
                .map(WorkDay::printPerformances)
                .forEach(builder::append);
        if (builder.equals(PERFORMANCES_HEADER)) return new StringBuilder("\nEr zijn geen prestaties voor deze dag.\n");
        builder.append(printWage(date));
        return builder;

    }

    public StringBuilder printAllDays() {
        StringBuilder builder = PERFORMANCES_HEADER;
        Stream.of(getWorkWeek())
                .filter(e -> e.getPerformances().size() != 0)
                .map(WorkDay::printPerformances)
                .forEach(builder::append);
        builder.append(printTotalWage());
        return builder;
    }

    public StringBuilder printTotalWage() {
        StringBuilder builder = new StringBuilder();

        Double normal = Stream.of(getWorkWeek())
                .map(WorkDay::getNormalWage)
                .reduce(0d, Double::sum);

        Double extra = Stream.of(getWorkWeek())
                .map(WorkDay::getExtraWage)
                .reduce(0d, Double::sum);

        Double unTaxed = Stream.of(getWorkWeek())
                .map(WorkDay::getUntaxedTotal)
                .reduce(0d, Double::sum);

        Double btw = Stream.of(getWorkWeek())
                .map(WorkDay::getBtw)
                .reduce(0d, Double::sum);

        Double sat = workWeek[5].getExtraWage();
        Double sun = workWeek[6].getExtraWage();

        builder = collectWageData(normal, extra, sat, sun, unTaxed, btw);

        return builder;
    }

    public StringBuilder printWage(LocalDate date) {
        WorkDay workDay = new WorkDay();
        for (WorkDay e : workWeek)
            if (e.getDate().equals(date) && e.getDate().getDayOfWeek().getValue() == 6)
                return collectWageData(0d, 0d, e.getExtraWage(), 0d, e.getUntaxedTotal(), e.getBtw());

            else if (e.getDate().equals(date) && e.getDate().getDayOfWeek().getValue() == 6)
                return collectWageData(0d, 0d, 0d, e.getExtraWage(), e.getUntaxedTotal(), e.getBtw());

            else
                return collectWageData(e.getNormalWage(), e.getExtraWage(), 0d, 0d, e.getUntaxedTotal(), e.getBtw());

        return new StringBuilder("empty?");
    }

    private StringBuilder collectWageData(Double normal, Double extra, Double sat, Double sun, Double unTaxed, Double btw) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t\t=====\n")
                .append(String.format("Normaal:\t%.2f€\n", normal))
                .append(String.format("Overuren:\t%.2f€\n", extra))
                .append(String.format("Zaterdag:\t%.2f€\n", sat))
                .append(String.format("Zondag:\t%.2f€\n", sun))
                .append("\t\t=====\n")
                .append(String.format("Bruto:\t%.2f€\n", unTaxed))
                .append(String.format("Extra:\t%.2f€\n", btw))
                .append("\t\t=====\n")
                .append(String.format("totaal:\t%.2f€\n", unTaxed - btw));
        return builder;
    }

}
