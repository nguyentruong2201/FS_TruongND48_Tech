package TruongND48_JPL.L.A203.src.main.java.fa.training.services;

import TruongND48_JPL.L.A203.src.main.java.fa.training.utils.Constants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {
    static Scanner scanner = new Scanner(System.in);

    private List<String> getAllPhones(List<String> allCustomers, List<String> sessionCustomers) {
        List<String> phones = new ArrayList<>();
        for (String line : allCustomers) {
            String[] parts = line.split(",", 4);
            if (parts.length > 2) phones.add(parts[2]);
        }
        for (String line : sessionCustomers) {
            String[] parts = line.split(",", 4);
            if (parts.length > 2) phones.add(parts[2]);
        }
        return phones;
    }

    private List<String> getAllOrderNumbers(List<String> allCustomers, List<String> sessionCustomers) {
        List<String> orderNumbers = new ArrayList<>();
        for (String line : allCustomers) {
            String[] parts = line.split(",", 4);
            if (parts.length > 3 && !parts[3].isEmpty()) {
                String[] orders = parts[3].split(";");
                for (String o : orders) {
                    String[] orderParts = o.split(":");
                    if (orderParts.length > 0) orderNumbers.add(orderParts[0]);
                }
            }
        }
        for (String line : sessionCustomers) {
            String[] parts = line.split(",", 4);
            if (parts.length > 3 && !parts[3].isEmpty()) {
                String[] orders = parts[3].split(";");
                for (String o : orders) {
                    String[] orderParts = o.split(":");
                    if (orderParts.length > 0) orderNumbers.add(orderParts[0]);
                }
            }
        }
        return orderNumbers;
    }

    private String enterOrder(List<String> allCustomers, List<String> sessionCustomers, Scanner scanner) {
        StringBuilder orderList = new StringBuilder();
        String addOrder;
        do {
            String orderNumber;
            while (true) {
                System.out.print("Enter order number: ");
                orderNumber = scanner.nextLine();
                List<String> allOrderNumbers = getAllOrderNumbers(allCustomers, sessionCustomers);
                if (TruongND48_JPL.L.A203.src.main.java.fa.training.utils.Validator.isValidOrderNumber(orderNumber, allOrderNumbers)) break;
                System.out.println("Invalid or duplicate order number!");
            }
            System.out.print("Enter order date (yyyy-MM-dd): ");
            String orderDate = scanner.nextLine();
            orderList.append(orderNumber).append(":").append(orderDate).append(";");
            System.out.print("Add another order? (y/n): ");
            addOrder = scanner.nextLine();
        } while (!addOrder.equalsIgnoreCase("n"));
        if (orderList.length() > 0) orderList.deleteCharAt(orderList.length() - 1);
        return orderList.toString();
    }

    public List<String> createCustomer() {
        List<String> customers = new ArrayList<>();
        // Lấy toàn bộ khách hàng đã có từ file
        List<String> allCustomers = findAll();
        Scanner scanner = new Scanner(System.in);
        String continueInput;
        do {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine();
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            String phone;
            while (true) {
                System.out.print("Enter phone number: ");
                phone = scanner.nextLine();
                List<String> allPhones = getAllPhones(allCustomers, customers);
                if (TruongND48_JPL.L.A203.src.main.java.fa.training.utils.Validator.isValidPhoneNumber(phone, allPhones)) break;
                System.out.println("Invalid or duplicate phone number!");
            }
            String orderList = enterOrder(allCustomers, customers, scanner);
            String customerLine = name + "," + address + "," + phone + "," + orderList;
            customers.add(customerLine);
            System.out.print("Add another customer? (y/n): ");
            continueInput = scanner.nextLine();
        } while (!continueInput.equalsIgnoreCase("n"));
        return customers;
    }

    public String save(List<String> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.FILE_NAME))) {
            for (String line : customers) {
                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            return "Save failed!";
        }
        return "Save successful!";
    }

    public List<String> findAll() {
        List<String> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                customers.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return customers;
    }

    public void display(List<String> customers) {
        System.out.printf("%-20s %-20s %-15s %-30s\n", "Customer Name", "Address", "Phone Number", "Order");
        for (String line : customers) {
            String[] parts = line.split(",", 4);
            String name = parts.length > 0 ? parts[0] : "";
            String address = parts.length > 1 ? parts[1] : "";
            String phone = parts.length > 2 ? parts[2] : "";
            String orderList = parts.length > 3 ? parts[3] : "";
            if (!orderList.isEmpty()) {
                String[] orders = orderList.split(";");
                for (int i = 0; i < orders.length; i++) {
                    if (i == 0) {
                        System.out.printf("%-20s %-20s %-15s %-30s\n", name, address, phone, orders[i]);
                    } else {
                        System.out.printf("%-20s %-20s %-15s %-30s\n", "", "", "", orders[i]);
                    }
                }
            } else {
                System.out.printf("%-20s %-20s %-15s %-30s\n", name, address, phone, "");
            }
        }
    }

    public List<String> search(String phone) {
        List<String> result = new ArrayList<>();
        List<String> customers = findAll();
        for (String line : customers) {
            String[] parts = line.split(",", 4);
            if (parts.length > 2 && parts[2].equals(phone)) {
                result.add(line);
            }
        }
        return result;
    }

    public boolean remove(String phone) {
        List<String> customers = findAll();
        boolean removed = false;
        for (int i = 0; i < customers.size(); i++) {
            String[] parts = customers.get(i).split(",", 4);
            if (parts.length > 2 && parts[2].equals(phone)) {
                customers.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) {
            save(customers);
        }
        return removed;
    }

}
