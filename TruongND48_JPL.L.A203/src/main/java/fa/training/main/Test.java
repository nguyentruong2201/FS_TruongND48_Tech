package TruongND48_JPL.L.A203.src.main.java.fa.training.main;

import TruongND48_JPL.L.A203.src.main.java.fa.training.entities.Customer;
import TruongND48_JPL.L.A203.src.main.java.fa.training.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        CustomerService service = new CustomerService();
        Scanner sc = new Scanner(System.in);
        int choice;
        List<Customer> customers = service.findAll();
        do {
            System.out.println("\n===== Choose function: =====");
            System.out.println("1. Add a new Customer");
            System.out.println("2. Show all Customers");
            System.out.println("3. Search Customer");
            System.out.println("4. Remove Customer");
            System.out.println("5. Exit");
            System.out.print("Your choice: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    List<Customer> newCustomers = service.createCustomers();
                    customers.addAll(newCustomers);
                    service.save(customers);
                    break;
                case 2:
                    service.display(customers);
                    break;
                case 3:
                    System.out.print("Enter phone number to search: ");
                    String searchPhone = sc.nextLine();
                    List<Customer> found = new ArrayList<>();
                    for (Customer c : customers) {
                        if (c.getPhoneNumber().equals(searchPhone)) {
                            found.add(c);
                        }
                    }
                    if (found.isEmpty()) {
                        System.out.println("No customer found with that phone number.");
                    } else {
                        service.display(found);
                    }
                    break;
                case 4:
                    System.out.print("Enter phone number to remove: ");
                    String removePhone = sc.nextLine();
                    boolean removed = false;
                    for (int i = 0; i < customers.size(); i++) {
                        if (customers.get(i).getPhoneNumber().equals(removePhone)) {
                            customers.remove(i);
                            removed = true;
                            break;
                        }
                    }
                    if (removed) {
                        service.save(customers);
                        System.out.println("Customer removed.");
                    } else {
                        System.out.println("No customer found with that phone number.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }
}
