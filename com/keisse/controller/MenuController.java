package com.keisse.controller;

import com.keisse.model.WorkWeek;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.keisse.util.FormatUtil.TRY_AGAIN;

public class MenuController {
    private final WorkWeek w = new WorkWeek();
    private final CaseController cases = new CaseController();

    public void go() {
        Scanner kb = new Scanner(System.in);
        boolean m = true;

        while (m) {
            System.out.println("1. Start nieuwe werkweek");
            System.out.println("2. Registreer een prestatie");
            System.out.println("3. Toon werkdag");
            System.out.println("4. Verwijder een werkdag");
            System.out.println("5. Print loonfiche");
            System.out.println("6. Herbegin");
            System.out.println("7. Sluit app");

            int input = 0;
            boolean correctInput = true;
            while (correctInput) {
                try {
                    System.out.print("\nGeef menukeuze: ");
                    input = kb.nextInt();
                    correctInput = false;
                } catch (InputMismatchException ex) {
                    System.err.print(TRY_AGAIN);
                    kb.next(); // Read and discard whatever string the user has entered
                }
            }

            correctInput = true;
            while (correctInput) {
                try {
                    m = cases.go(w, kb, input);
                    correctInput = false;

                } catch (DateTimeParseException ex) {
                    System.err.print(TRY_AGAIN);
                }
            }

        }
        kb.close();
    }
}
