package com.project2;

import com.project2.scenarios.CatWorkScenario;
import com.project2.scenarios.OwnerWorkScenario;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\nChoose option");
            System.out.println("1. Work with owners");
            System.out.println("2. Work with cats");
            System.out.println("0. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> OwnerWorkScenario.run(scanner);
                case "2" -> CatWorkScenario.run(scanner);
                case "0" -> running = false;
                default -> System.out.println("Incorrect choice.");
            }
        }

        System.out.println("Exiting program.");
    }
}
