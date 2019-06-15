package com.keisse.controller;

import com.keisse.model.WorkWeek;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestController {


    public static void main(String[] args) {
        WorkWeek week = new WorkWeek(LocalDate.of(2019, 6, 7));
        week.addPerformance(LocalDate.of(2019, 6, 7), LocalTime.MIDNIGHT, LocalTime.MAX);
        week.addPerformance(LocalDate.of(2019, 6, 4), LocalTime.of(4, 0), LocalTime.of(12, 0));
        week.addPerformance(LocalDate.of(2019, 6, 4), LocalTime.of(13, 0), LocalTime.of(19, 0));
        week.addPerformance(LocalDate.of(2019, 6, 4), LocalTime.of(1, 0), LocalTime.of(2, 0));
        week.addPerformance(LocalDate.of(2019, 6, 5), LocalTime.of(5, 0), LocalTime.of(20, 0));
        week.addPerformance(LocalDate.of(2019, 6, 8), LocalTime.MIDNIGHT, LocalTime.MAX);
        week.addPerformance(LocalDate.of(2019, 6, 8), LocalTime.of(5, 0), LocalTime.of(20, 0));

        System.out.println(week.printAllDays());
    }
}
