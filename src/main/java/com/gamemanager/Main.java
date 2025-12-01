package com.gamemanager;

import com.gamemanager.gui.GameManagerGUI;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        System.out.println("Starting Game Manager...");
        System.out.println("API: RAWG.io");
        System.out.println("Initializing GUI...");

        GameManagerGUI.main(args);

    }
}
