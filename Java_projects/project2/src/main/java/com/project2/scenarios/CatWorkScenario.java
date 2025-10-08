package com.project2.scenarios;

import com.project2.models.Cats;
import com.project2.models.Color;
import com.project2.models.Owners;
import com.project2.repositories.CatRepository;
import com.project2.repositories.OwnerRepository;
import com.project2.services.CatService;
import com.project2.services.OwnerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class CatWorkScenario {
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("entityManagerFactory");
    static EntityManager em = emf.createEntityManager();

    private static final CatRepository catRepository = new CatRepository(em);
    private static final OwnerRepository ownerRepository = new OwnerRepository(em);
    private static final CatService catService = new CatService(catRepository);
    private static final OwnerService ownerService = new OwnerService(ownerRepository);

    public static void run(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("Choose an option:");
            System.out.println("1. Add a Cat");
            System.out.println("2. Show all Cats");
            System.out.println("3. Delete a Cat by ID");
            System.out.println("4. Show Owner's cats by ID");
            System.out.println("0. Back");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addCat(scanner);
                case "2" -> showAllCats();
                case "3" -> deleteCatById(scanner);
                case "4" -> showCatsByOwnerId(scanner);
                case "0" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addCat(Scanner scanner) {
        System.out.print("Name of cat: ");
        String name = scanner.nextLine();

        System.out.print("Birthdate (yyyy-mm-dd): ");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Breed: ");
        String breed = scanner.nextLine();

        System.out.print("Color (WHITE, BLACK, GREY, BROWN): ");
        Color color = Color.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Owner id: ");
        Long ownerId = Long.parseLong(scanner.nextLine());
        Owners owner = ownerService.getOwnerById(ownerId);

        if (owner == null) {
            System.out.println("There is no owner with id " + ownerId);
            return;
        }

        Cats cat = new Cats();
        cat.setName(name);
        cat.setBirthDate(birthDate);
        cat.setBreed(breed);
        cat.setColor(color);
        cat.setOwner(owner);

        catService.saveCat(cat);
        System.out.println("Cat added.");
    }

    private static void showAllCats() {
        List<Cats> cats = catService.getAllCats();
        if (cats.isEmpty()) {
            System.out.println("There are no cats.");
        } else {
            for (Cats cat : cats) {
                System.out.printf("ID: %d | Name: %s | Color: %s | Breed: %s | Owner: %s%n",
                        cat.getId(), cat.getName(), cat.getColor(), cat.getBreed(),
                        cat.getOwner() != null ? cat.getOwner().getName() : "There is no owner");
            }
        }
    }

    private static void deleteCatById(Scanner scanner) {
        System.out.print("Enter cat ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        catService.deleteCatById(id);
        System.out.println("Cat deleted.");
    }

    private static void showCatsByOwnerId(Scanner scanner) {
        System.out.print("Enter owner ID: ");
        long ownerId = Long.parseLong(scanner.nextLine());

        Owners owner = ownerService.getOwnerById(ownerId);
        if (owner == null) {
            System.out.println("There is no owner with id .");
            return;
        }

        List<Cats> cats = owner.getCats();
        if (cats == null || cats.isEmpty()) {
            System.out.println("This owner don't have any cats.");
            return;
        }

        System.out.println("Owner's cats " + owner.getName() + ":");
        for (Cats cat : cats) {
            System.out.println("ID: " + cat.getId() + ", Name: " + cat.getName() + ", Breed: " + cat.getBreed() + ", Color: " + cat.getColor() + ", Birthdate: " + cat.getBirthDate());
        }
    }
}
