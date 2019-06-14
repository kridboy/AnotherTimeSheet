package com.keisse.view;

import com.keisse.model.Performance;
import com.keisse.model.WorkWeek;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSheetApp {
    public static void main(String[] args) {
        Performance p = new Performance(LocalTime.of(0,0),LocalTime.of(0,0), LocalDate.of(1992,12,21));

        WorkWeek w = new WorkWeek(LocalDate.of(1992,12,21));
        w.addPerformance(LocalDate.of(1992,12,21),LocalTime.of(0,0),LocalTime.of(12,0));

        System.out.println(w.printDay(LocalDate.of(1992,12,21)));
        System.out.println(w.printAllDays());
    }
}
