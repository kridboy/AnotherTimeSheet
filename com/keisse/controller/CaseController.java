package com.keisse.controller;

import com.keisse.model.WorkWeek;
import com.keisse.util.FormatUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.keisse.util.FormatUtil.*;

public class CaseController {

    public boolean go(WorkWeek w, Scanner kb, int input) {
        switch (input) {
            case 1:
                newWeek(w, kb);
                break;
            case 2:
                newPrestation(w, kb);
                break;
            case 3:
                showPrestation(w, kb);
                break;
            case 4:
                removeDay(w, kb);
                break;
            case 5:
                showAll(w);
                break;
            case 6:
                restart(w);
                break;
            case 7:
                return false;
            default:
                inputMismatch(w, kb, input);
                break;
        }
        return true;
    }

    private void newWeek(WorkWeek w, Scanner kb) {
        System.out.print("\ngeef start van werkweek: ");
        LocalDate dag = LocalDate.parse(kb.next(), FormatUtil.DATE_FORMATTER);
        w.reset();
        w.setWorkWeek(dag);
        System.out.println("Werkweek gegenereerd\n");
    }

    private void newPrestation(WorkWeek w, Scanner kb) {
        System.out.print(NEW_DAY);
        LocalDate dag = LocalDate.parse(kb.next(), FormatUtil.DATE_FORMATTER);
        System.out.print("geef start uur: ");
        LocalTime start = LocalTime.parse(kb.next(), FormatUtil.TIME_FORMATTER);
        System.out.print("geef eind uur: ");
        if (w.addPerformance(dag, start, endTime(kb)))
            System.out.println("Prestatie toegevoegd!");
        else {
            System.err.println(DAY_NOT_IN_WEEK);
            System.err.println(TRY_AGAIN);
        }
    }

    private void showPrestation(WorkWeek w, Scanner kb) {
        System.out.print(NEW_DAY);
        LocalDate dag = LocalDate.parse(kb.next(), FormatUtil.DATE_FORMATTER);
        System.out.println(w.printDay(dag));
    }

    private void removeDay(WorkWeek w, Scanner kb) {
        System.out.print(NEW_DAY);
        LocalDate dag = LocalDate.parse(kb.next(), FormatUtil.DATE_FORMATTER);
        if (w.clear(dag))
            System.out.println(String.format("\nDe gegevens van  %s %d %s werden verwijderd.\n", dag.getDayOfWeek().toString().toLowerCase(), dag.getDayOfMonth(), dag.format(MONTH_FORMATTER)));
        else {
            System.err.println(DAY_NOT_IN_WEEK);
            System.err.println(TRY_AGAIN);
        }
    }

    private void showAll(WorkWeek w) {
        System.out.println(w.printAllDays());
    }

    private void restart(WorkWeek w) {
        w.reset();
    }

    private void inputMismatch(WorkWeek w, Scanner kb, int input) {
        System.err.print(TRY_AGAIN);
        try {
            System.out.print("Geef menukeuze: ");
            input = kb.nextInt();
            go(w, kb, input);
        } catch (InputMismatchException ex) {
            kb.next();
            inputMismatch(w, kb, input);

        }
    }

    private LocalTime endTime(Scanner kb) {
        String str = kb.next();
        if (str.equals("24:00")) return LocalTime.of(23, 59);
        else return LocalTime.parse(str, FormatUtil.TIME_FORMATTER);
    }
}
