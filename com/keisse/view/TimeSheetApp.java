package com.keisse.view;

import com.keisse.controller.MenuController;
import com.keisse.controller.TestController;

public class TimeSheetApp {
    public static void main(String[] args) {
        MenuController menu = new MenuController();
        TestController test = new TestController();

        //menu.go();
        test.go();
    }
}
