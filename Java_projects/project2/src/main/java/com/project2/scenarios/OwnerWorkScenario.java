package com.project2.scenarios;

import com.project2.models.Owners;
import com.project2.repositories.CatRepository;
import com.project2.repositories.OwnerRepository;
import com.project2.services.CatService;
import com.project2.services.OwnerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class OwnerWorkScenario {
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("entityManagerFactory");
    static EntityManager em = emf.createEntityManager();

    private static final CatRepository catRepository = new CatRepository(em);
    private static final OwnerRepository ownerRepository = new OwnerRepository(em);
    private static final CatService catService = new CatService(catRepository);
    private static final OwnerService ownerService = new OwnerService(ownerRepository);

    public static void run(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n Choose action");
            System.out.println("1. Add owner");
            System.out.println("2. Show all owners");
            System.out.println("3. Delete owner by ID");
            System.out.println("0. Back");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addOwner(scanner);
                case "2" -> showAllOwners();
                case "3" -> deleteOwnerById(scanner);
                case "0" -> back = true;
                default -> System.out.println("Incorrect choice");
            }
        }
    }

    private static void addOwner(Scanner scanner) {
        System.out.print("Owner's name: ");
        String name = scanner.nextLine();

        System.out.print("Birthdate (yyyy-MM-dd): ");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine());

        Owners owner = new Owners();
        owner.setName(name);
        owner.setBirthDate(birthDate);

        ownerService.saveOwner(owner);
        System.out.println("Owner added.");
    }

    private static void showAllOwners() {
        List<Owners> owners = ownerService.getAllOwners();
        if (owners.isEmpty()) {
            System.out.println("There are no owners.");
        } else {
            for (Owners owner : owners) {
                System.out.printf("ID: %d | Name: %s | Birthdate: %s%n",
                        owner.getId(), owner.getName(), owner.getBirthDate());
            }
        }
    }

    private static void deleteOwnerById(Scanner scanner) {
        System.out.print("Enter ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        ownerService.deleteOwnerById(id);
        System.out.println("Owner deleted.");
    }
}


